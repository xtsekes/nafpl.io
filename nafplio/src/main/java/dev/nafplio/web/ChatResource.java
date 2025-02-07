package dev.nafplio.web;

import dev.nafplio.data.entity.Chat;
import dev.nafplio.projectScanner.ProjectScanner;
import dev.nafplio.service.IngestService;
import dev.nafplio.service.ChatService;
import dev.nafplio.service.model.IngestModel;
import dev.nafplio.web.model.CreateProjectPayload;
import dev.nafplio.web.model.ProjectView;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Path("/chats")
public class ChatResource {
    private static final String DEFAULT_EXCLUDES = """
            .git/
            .gitignore
            .idea/
            test/
            migration/mssql/
            migration/mysql/
            *.db
            mvnw.cmd
            Dockerfile
            mvnw
            lombok.config
            application-development.properties
            application-test.properties
            application-development-mssql.properties
            .DS_Store
            .gitattributes
            .mvn/
            package-lock.json
            
            **/.git/
            **/.DS_Store
            **/.gitignore
            **/.idea/
            **/test/
            **/migration/mssql/
            **/migration/mysql/
            **/*.db
            **/mvnw.cmd
            **/Dockerfile
            **/mvnw
            **/lombok.config
            **/application-development.properties
            **/application-test.properties
            **/application-development-mssql.properties
            **/.gitattributes
            **/.mvn/
            **/package-lock.json
            """;

    private final ChatService chatService;

    @Inject
    IngestService ingestService;

    public ChatResource(ChatService chatService) {
        this.chatService = chatService;
    }

    @GET
    @Path("/")
    public List<ProjectView> list() {
        return chatService.get()
                .stream()
                .map(this::toProjectView)
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        if (id == null || id.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var project = chatService.get(id);

        return project.isPresent()
                ? Response.status(Response.Status.OK).entity(toProjectView(project.get())).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreateProjectPayload createProjectPayload) {
        if (createProjectPayload.title() == null || createProjectPayload.rootDirectory() == null
        ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var inputDirectory = Paths.get(createProjectPayload.rootDirectory()).toAbsolutePath().normalize();

        if (!Files.exists(inputDirectory)
                || !Files.isDirectory(inputDirectory)
        ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var entity = chatService.get(createProjectPayload.title());

        if (entity.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var projectEntity = new Chat();
        projectEntity.setRootDirectory(createProjectPayload.rootDirectory());
        projectEntity.setTitle(createProjectPayload.title());
        projectEntity.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));

        try {
            ingestProject(ingestService, inputDirectory, createProjectPayload.title());

            var result = chatService.create(projectEntity);

            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ProjectView toProjectView(Chat chat) {
        if (chat == null) {
            return null;
        }

        return new ProjectView(
                chat.getId(),
                chat.getRootDirectory(),
                chat.getTitle()
        );
    }

    private static void ingestProject(IngestService ingestService, java.nio.file.Path inputDirectory, String title) throws IOException {
        Objects.requireNonNull(title);

        java.nio.file.Path outputDirectory;

        outputDirectory = resolveOutputDirectory(title);

        scanProject(inputDirectory, outputDirectory);

        ingestService.startIngestion(IngestModel.of(title, outputDirectory));
    }

    private static void scanProject(java.nio.file.Path inputDirectory, java.nio.file.Path outputDirectory) throws IOException {
        var outputPath = outputDirectory.resolve("codebase.txt");

        try (var writer = new StringWriter()) {
            ProjectScanner.scan(inputDirectory.toAbsolutePath().normalize().toString(), writer, DEFAULT_EXCLUDES);

            Files.write(outputPath, writer.toString().getBytes());
        }
    }

    private static java.nio.file.Path resolveOutputDirectory(String title) throws IOException {
        var currentDirectory = Paths.get(System.getProperty("user.dir")).getParent().toAbsolutePath().normalize();

        var outputDirectory = currentDirectory.resolve("documents").resolve(title);

        if (!Files.exists(outputDirectory)) {
            Files.createDirectory(outputDirectory);
        }

        return outputDirectory;
    }
}
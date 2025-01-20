package dev.nafplio.web;

import dev.nafplio.data.ProjectEntity;
import dev.nafplio.directoryscanner.project.ProjectScanner;
import dev.nafplio.service.IngestService;
import dev.nafplio.service.ProjectService;
import dev.nafplio.service.model.IngestModel;
import dev.nafplio.web.model.CreateProjectPayload;
import dev.nafplio.web.model.ProjectView;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Path("/project")
public class ProjectResource {
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

    private final ProjectService projectService;

    @Inject
    IngestService ingestService;

    public ProjectResource(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GET
    @Path("/get-all")
    public List<ProjectView> getAllProjects() {
        return projectService.getAllProjects()
                .stream()
                .map(this::toProjectView)
                .toList();
    }

    @POST
    @Path("/create-project")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProject(CreateProjectPayload createProjectPayload) {
        if (createProjectPayload.nickname() == null || createProjectPayload.rootDirectory() == null
        ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var inputDirectory = Paths.get(createProjectPayload.rootDirectory()).toAbsolutePath().normalize();

        if (!Files.exists(inputDirectory)
                || !Files.isDirectory(inputDirectory)
        ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var entity = projectService.getProjectByNickname(createProjectPayload.nickname());

        if (entity != null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var projectEntity = new ProjectEntity();
        projectEntity.setRootDirectory(createProjectPayload.rootDirectory());
        projectEntity.setNickname(createProjectPayload.nickname());
        projectEntity.setCreatedAt(LocalDateTime.now());

        try {
            ingestProject(ingestService, inputDirectory, createProjectPayload.nickname());

            projectService.createProject(projectEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/get-project/{id}")
    public ProjectView getProjectById(Long id) {
        return toProjectView(projectService.getProjectById(id));
    }

    @GET
    @Path("/get-project/{nickname}")
    public ProjectView getProjectByNickname(String nickname) {
        return toProjectView(projectService.getProjectByNickname(nickname));
    }


    private ProjectView toProjectView(ProjectEntity projectEntity) {
        return new ProjectView(
                projectEntity.getId(),
                projectEntity.getRootDirectory(),
                projectEntity.getNickname()
        );
    }

    private static void ingestProject(IngestService ingestService, java.nio.file.Path inputDirectory, String nickname) throws IOException {
        Objects.requireNonNull(nickname);

        java.nio.file.Path outputDirectory = null;

        outputDirectory = resolveOutputDirectory(nickname);

        scanProject(inputDirectory, outputDirectory);

        ingestService.startIngestion(IngestModel.of(nickname, outputDirectory));
    }

    private static void scanProject(java.nio.file.Path inputDirectory, java.nio.file.Path outputDirectory) throws IOException {
        var outputPath = outputDirectory.resolve("codebase.txt");

        try (var writer = new StringWriter()) {
            ProjectScanner.scan(inputDirectory.toAbsolutePath().normalize().toString(), writer, DEFAULT_EXCLUDES);

            Files.write(outputPath, writer.toString().getBytes());
        }
    }

    private static java.nio.file.Path resolveOutputDirectory(String nickname) throws IOException {
        var currentDirectory = Paths.get(System.getProperty("user.dir")).getParent().toAbsolutePath().normalize();

        var outputDirectory = currentDirectory.resolve("documents").resolve(nickname);

        if (!Files.exists(outputDirectory)) {
            Files.createDirectory(outputDirectory);
        }

        return outputDirectory;
    }
}


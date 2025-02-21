package dev.nafplio.web.rest;

import dev.nafplio.domain.PageResult;
import dev.nafplio.domain.chat.Chat;
import dev.nafplio.domain.chat.ChatHistoryService;
import dev.nafplio.domain.chat.ChatService;
import dev.nafplio.projectScanner.ProjectScanner;
import dev.nafplio.service.IngestService;
import dev.nafplio.service.model.IngestModel;
import dev.nafplio.web.UserResolver;
import dev.nafplio.web.model.CreateProjectPayload;
import dev.nafplio.web.rest.model.PageParameters;
import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport;
import io.quarkus.runtime.util.StringUtil;
import io.quarkus.security.Authenticated;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Path("/chats")
@Authenticated
@AllArgsConstructor
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
    private final ChatHistoryService chatHistoryService;
    private final IngestService ingestService;
    private final UserResolver userResolver;

    @GET
    @Path("/")
    @APIResponses({
            @APIResponse(responseCode = "200"),
            @APIResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(anyOf = {ViolationReport.class, Void.class})
            )),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public PageResult<Chat> list(@Valid @BeanParam PageParameters pageParameters) {
        var user = userResolver.resolve().orElseThrow(BadRequestException::new);

        var chats = chatService.get(user.getId(), (pageParameters.getPage() - 1) * pageParameters.getPageSize(), pageParameters.getPageSize());

        return PageResult.of(pageParameters.getPage(), pageParameters.getPageSize(), chats.totalElements(), chats.data());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
            @APIResponse(responseCode = "200"),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Chat get(@PathParam("id") String id) {
        if (StringUtil.isNullOrEmpty(id)) {
            throw new BadRequestException();
        }

        var user = userResolver.resolve().orElseThrow(BadRequestException::new);

        var project = chatService.get(user.getId(), id);

        if (project.isEmpty()) {
            throw new BadRequestException();
        }

        return project.get();
    }

    @POST
    @Path("/")
    @ResponseStatus(201)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Created", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Chat.class)
            )),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Chat create(CreateProjectPayload createProjectPayload) {
        if (createProjectPayload.title() == null || createProjectPayload.rootDirectory() == null) {
            throw new BadRequestException();
        }

        var inputDirectory = Paths.get(createProjectPayload.rootDirectory()).toAbsolutePath().normalize();

        if (!Files.exists(inputDirectory) || !Files.isDirectory(inputDirectory)) {
            throw new BadRequestException();
        }

        var user = userResolver.resolve().orElseThrow(BadRequestException::new);

        var entity = chatService.get(user.getId(), createProjectPayload.title());

        if (entity.isPresent()) {
            throw new BadRequestException();
        }

        try {
            var chat = chatService.create(Chat.builder()
                    .userId(user.getId())
                    .rootDirectory(createProjectPayload.rootDirectory())
                    .title(createProjectPayload.title())
                    .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                    .build());

            ingestProject(ingestService, inputDirectory, chat.getId());

            return chat;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @Path("/{id}")
    @ResponseStatus(204)
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Deleted"),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    @Transactional
    public void delete(@PathParam("id") String id) {
        if (StringUtil.isNullOrEmpty(id)) {
            throw new BadRequestException();
        }

        var user = userResolver.resolve().orElseThrow(BadRequestException::new);

        var entity = chatService.get(user.getId(), id);

        if (entity.isEmpty()) {
            throw new BadRequestException();
        }

        chatHistoryService.delete(entity.get());
        chatService.delete(entity.get());
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
package dev.nafplio.web;

import dev.nafplio.directoryscanner.project.ProjectScanner;
import dev.nafplio.service.IngestService;
import dev.nafplio.service.model.IngestModel;
import dev.nafplio.web.model.IngestPayload;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/ingest")
public class IngestResource {

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
            """;

    @Inject
    IngestService ingestService;

    @POST
    @Path("/ingest-data")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ingestData(IngestPayload ingestDataPayload) {
        if (ingestDataPayload.ingestDirectory() == null
                || ingestDataPayload.outputDirectory() == null
                || ingestDataPayload.nickname() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        var fileToWrite = ingestDataPayload.outputDirectory() + "/" + ingestDataPayload.nickname() + ".txt";

        try (var writer = new StringWriter()) {
            ProjectScanner.scan(ingestDataPayload.ingestDirectory(), writer, DEFAULT_EXCLUDES);

            Files.write(Paths.get(fileToWrite), writer.toString().getBytes());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        ingestService
                .startIngestion(toIngestModel(ingestDataPayload));
        return Response.status(Response.Status.CREATED).build();

    }

    private IngestModel toIngestModel(IngestPayload ingestPayload) {
        IngestModel ingestModel = new IngestModel();
        ingestModel.setOutputDirectory(ingestPayload.outputDirectory()+"/"+ingestPayload.nickname()+".txt");
        return ingestModel;
    }

}

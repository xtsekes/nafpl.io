package dev.nafplio.web;

import dev.nafplio.service.IngestService;
import dev.nafplio.service.model.IngestModel;
import dev.nafplio.web.model.IngestPayload;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/ingest")
public class IngestResource {

    @Inject
    IngestService ingestService;

    @POST
    @Path("/ingest-data")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ingestData(IngestPayload ingestDataPayload) {
        if (ingestDataPayload == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        ingestService
                .startIngestion(toIngestModel(ingestDataPayload));
        return Response.status(Response.Status.CREATED).build();

    }
    private IngestModel toIngestModel(IngestPayload ingestPayload) {
        IngestModel ingestModel = new IngestModel();
        ingestModel.setRootDirectory(ingestPayload.rootDirectory());
        ingestModel.setNickname(ingestPayload.nickname());
        return ingestModel;
    }

}

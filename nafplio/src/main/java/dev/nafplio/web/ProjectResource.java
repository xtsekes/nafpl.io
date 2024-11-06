package dev.nafplio.web;

import dev.nafplio.service.ProjectService;
import dev.nafplio.data.ProjectEntity;
import dev.nafplio.web.model.CreateProjectPayload;
import dev.nafplio.web.model.ProjectView;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@Path("/project")
public class ProjectResource {

    private final ProjectService projectService;

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
    public Response createProject(CreateProjectPayload createProjectPayload) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setRootDirectory(createProjectPayload.rootDirectory());
        projectEntity.setNickname(createProjectPayload.nickname());
        projectEntity.setCreatedAt(LocalDateTime.now());

        projectService.createProject(projectEntity);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/get-project/{id}")
    public ProjectView getProjectById(Long id) {
        return toProjectView(projectService.getProjectById(id));
    }


    private ProjectView toProjectView(ProjectEntity projectEntity) {
        return new ProjectView(
                projectEntity.getRootDirectory(),
                projectEntity.getNickname()
        );
    }

}

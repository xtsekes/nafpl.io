package dev.nafplio.service;

import dev.nafplio.data.ProjectEntity;
import dev.nafplio.data.ProjectEntityRepository;

import java.util.List;

public class ProjectService {

    private final ProjectEntityRepository projectEntityRepository;

    public ProjectService(ProjectEntityRepository projectEntityRepository) {
        this.projectEntityRepository = projectEntityRepository;
    }

    public void createProject(ProjectEntity projectEntity) {
        projectEntityRepository.persist(projectEntity);
    }

    public List<ProjectEntity> getAllProjects() {
        return projectEntityRepository.listAll();
    }

    public ProjectEntity getProjectById(Long id) {
        return projectEntityRepository.findById(id);
    }

}

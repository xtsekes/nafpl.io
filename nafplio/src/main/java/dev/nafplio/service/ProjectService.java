package dev.nafplio.service;

import dev.nafplio.data.ProjectEntity;
import dev.nafplio.data.ProjectEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProjectService {

    private final ProjectEntityRepository projectEntityRepository;

    public ProjectService(ProjectEntityRepository projectEntityRepository) {
        this.projectEntityRepository = projectEntityRepository;
    }

    @Transactional
    public void createProject(ProjectEntity projectEntity) {
        projectEntityRepository.persist(projectEntity);
    }

    public List<ProjectEntity> getAllProjects() {
        return projectEntityRepository.listAll();
    }

    public ProjectEntity getProjectById(Long id) {
        return projectEntityRepository.findById(id);
    }

    public ProjectEntity getProjectByNickname(String nickname) {
        return projectEntityRepository.findByNickname(nickname);
    }
}

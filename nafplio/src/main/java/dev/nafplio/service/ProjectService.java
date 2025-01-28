package dev.nafplio.service;

import dev.nafplio.data.entity.ProjectEntity;
import dev.nafplio.data.repository.ProjectEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Optional<ProjectEntity> getProjectById(Long id) {
        return projectEntityRepository.findByIdOptional(id);
    }

    public Optional<ProjectEntity> getProjectByNickname(String nickname) {
        return projectEntityRepository.findByNickname(nickname);
    }
}

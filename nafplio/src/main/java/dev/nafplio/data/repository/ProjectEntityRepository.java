package dev.nafplio.data.repository;

import dev.nafplio.data.entity.ProjectEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectEntityRepository implements PanacheRepository<ProjectEntity> {

    public ProjectEntity findByNickname(String nickname) {
        return find("nickname", nickname).firstResult();
    }
}

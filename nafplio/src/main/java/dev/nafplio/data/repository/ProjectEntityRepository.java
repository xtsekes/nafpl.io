package dev.nafplio.data.repository;

import dev.nafplio.data.entity.ProjectEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ProjectEntityRepository implements PanacheRepository<ProjectEntity> {

    public Optional<ProjectEntity> findByNickname(String nickname) {
        return find("nickname", nickname).firstResultOptional();
    }
}

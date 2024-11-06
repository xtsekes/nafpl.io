package dev.nafplio.data;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectEntityRepository implements PanacheRepository<ProjectEntity> {
}

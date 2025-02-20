package dev.nafplio.data;

import dev.nafplio.auth.RoleStore;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
final class RoleRepository implements PanacheRepositoryBase<Role, String>, RoleStore {
    @Override
    public Optional<dev.nafplio.auth.Role> get(String id) {
        return this.findByIdOptional(id)
                .map(RoleRepository::mapToDomain);
    }

    @Override
    public Optional<dev.nafplio.auth.Role> getByNormalizedName(String normalizedName) {
        return this.find("normalizedName", normalizedName)
                .firstResultOptional()
                .map(RoleRepository::mapToDomain);
    }

    @Override
    public dev.nafplio.auth.Role add(dev.nafplio.auth.Role role) {
        Objects.requireNonNull(role);

        var entity = mapToEntity(role);

        this.persist(entity);

        return mapToDomain(entity);
    }

    @Override
    public void update(dev.nafplio.auth.Role role) {
        Objects.requireNonNull(role);

        var entity = this.findByIdOptional(role.getId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        entity.setName(role.getName());
        entity.setNormalizedName(role.getNormalizedName());

        this.persist(entity);
    }

    @Override
    public void delete(dev.nafplio.auth.Role role) {
        Objects.requireNonNull(role);

        var entity = this.findByIdOptional(role.getId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        this.delete(entity);
    }

    private static dev.nafplio.auth.Role mapToDomain(Role entity) {
        return dev.nafplio.auth.Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .normalizedName(entity.getNormalizedName())
                .build();
    }

    private static Role mapToEntity(dev.nafplio.auth.Role domain) {
        var entity = new Role();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setNormalizedName(domain.getNormalizedName());

        return entity;
    }
}

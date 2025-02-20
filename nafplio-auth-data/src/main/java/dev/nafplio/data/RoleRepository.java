package dev.nafplio.data;

import dev.nafplio.auth.RoleStore;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
final class RoleRepository implements PanacheRepositoryBase<Role, String>, RoleStore<Role, String> {
    @Override
    public Optional<Role> get(String id) {
        return this.findByIdOptional(id)
                .map(RoleRepository::mapToDomain);
    }

    @Override
    public Optional<Role> getByNormalizedName(String normalizedName) {
        return this.find("normalizedName", normalizedName)
                .firstResultOptional()
                .map(RoleRepository::mapToDomain);
    }

    @Override
    public Role add(Role role) {
        Objects.requireNonNull(role);

        var entity = mapToEntity(role);

        this.persist(entity);

        return mapToDomain(entity);
    }

    @Override
    public void update(Role role) {
        Objects.requireNonNull(role);

        var entity = this.findByIdOptional(role.getId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        entity.setName(role.getName());
        entity.setNormalizedName(role.getNormalizedName());

        this.persist(entity);
    }

    @Override
    public void delete(Role role) {
        Objects.requireNonNull(role);

        var entity = this.findByIdOptional(role.getId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        this.delete(entity);
    }

    private static Role mapToDomain(Role entity) {
        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .normalizedName(entity.getNormalizedName())
                .build();
    }

    private static Role mapToEntity(Role domain) {
        var entity = new Role();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setNormalizedName(domain.getNormalizedName());

        return entity;
    }
}
package dev.nafplio.auth.impl;

import dev.nafplio.auth.*;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@ApplicationScoped
final class DefaultRoleService implements RoleService {
    private final RoleStore roleStore;

    @Override
    public Optional<Role> get(String id) {
        return this.roleStore.get(id);
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        return this.getByNormalizedName(Roles.normalizeName(name));
    }

    @Override
    public Optional<Role> getByNormalizedName(String normalizedName) {
        return this.roleStore.getByNormalizedName(normalizedName);
    }

    @Override
    public Role add(Role role) {
        Objects.requireNonNull(role);

        this.roleStore.get(role.getId())
                .ifPresent(r -> {
                    throw new RoleAlreadyExists();
                });

        return this.roleStore.add(role);
    }

    @Override
    public void update(Role role) {
        Objects.requireNonNull(role);

        this.roleStore.get(role.getId())
                .orElseThrow(RoleNotFoundException::new);

        this.roleStore.update(role);
    }

    @Override
    public void delete(Role role) {
        Objects.requireNonNull(role);

        this.roleStore.get(role.getId())
                .orElseThrow(RoleNotFoundException::new);

        this.roleStore.delete(role);
    }
}

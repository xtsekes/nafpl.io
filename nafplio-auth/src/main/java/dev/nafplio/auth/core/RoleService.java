package dev.nafplio.auth.core;

import dev.nafplio.auth.Role;
import dev.nafplio.auth.RoleAlreadyExists;
import dev.nafplio.auth.RoleNotFoundException;
import dev.nafplio.auth.RoleStore;
import dev.nafplio.auth.impl.Roles;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public abstract class RoleService<TRole extends Role<TKey>, TKey> {
    private final RoleStore<TRole, TKey> roleStore;

    public final Optional<TRole> get(TKey id) {
        return this.roleStore.get(id);
    }

    public final Optional<TRole> getRoleByName(String name) {
        return this.getByNormalizedName(Roles.normalizeName(name));
    }

    public final Optional<TRole> getByNormalizedName(String normalizedName) {
        return this.roleStore.getByNormalizedName(normalizedName);
    }

    @Transactional
    public final TRole add(TRole role) {
        Objects.requireNonNull(role);

        this.roleStore.get(role.getId())
                .ifPresent(r -> {
                    throw new RoleAlreadyExists();
                });

        return this.roleStore.add(role);
    }

    @Transactional
    public final void update(TRole role) {
        Objects.requireNonNull(role);

        this.roleStore.get(role.getId())
                .orElseThrow(RoleNotFoundException::new);

        this.roleStore.update(role);
    }

    @Transactional
    public final void delete(TRole role) {
        Objects.requireNonNull(role);

        this.roleStore.get(role.getId())
                .orElseThrow(RoleNotFoundException::new);

        this.roleStore.delete(role);
    }
}
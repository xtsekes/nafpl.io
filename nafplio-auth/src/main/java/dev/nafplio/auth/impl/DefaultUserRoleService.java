package dev.nafplio.auth.impl;

import dev.nafplio.auth.*;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@ApplicationScoped
final class DefaultUserRoleService implements UserRoleService {
    private final RoleStore roleStore;
    private final UserRoleStore userRoleStore;

    @Override
    public List<Role> getRolesForUser(User user) {
        Objects.requireNonNull(user);

        return this.userRoleStore.getRolesForUser(user)
                .stream()
                .map(x -> this.roleStore.get(x.getRoleId()).orElseThrow(RoleNotFoundException::new))
                .toList();
    }

    @Override
    public void addRoleToUser(User user, Role role) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);

        this.userRoleStore.addRoleToUser(new UserRole(user.getId(), role.getId()));
    }

    @Override
    public void removeRoleFromUser(User user, Role role) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);

        this.userRoleStore.removeRoleFromUser(new UserRole(user.getId(), role.getId()));
    }
}

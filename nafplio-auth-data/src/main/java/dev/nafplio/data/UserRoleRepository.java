package dev.nafplio.data;

import dev.nafplio.auth.User;
import dev.nafplio.auth.UserRoleStore;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
final class UserRoleRepository implements PanacheRepositoryBase<UserRole, UserRolePrimaryKey>, UserRoleStore {

    @Override
    public List<dev.nafplio.auth.UserRole> getRolesForUser(User user) {
        Objects.requireNonNull(user);

        return this.find("userId", user.getId())
                .stream()
                .map(UserRoleRepository::mapToDomain)
                .toList();
    }

    @Override
    public void addRoleToUser(dev.nafplio.auth.UserRole userRole) {
        Objects.requireNonNull(userRole);

        this.persist(mapToEntity(userRole));

    }

    @Override
    public void removeRoleFromUser(dev.nafplio.auth.UserRole userRole) {
        Objects.requireNonNull(userRole);

        this.delete(mapToEntity(userRole));
    }

    private static dev.nafplio.auth.UserRole mapToDomain(UserRole userRole) {
        return dev.nafplio.auth.UserRole.builder()
                .userId(userRole.getUserId())
                .roleId(userRole.getRoleId())
                .build();
    }

    private static UserRole mapToEntity(dev.nafplio.auth.UserRole userRole) {
        var entity = new UserRole();

        entity.setUserId(userRole.getUserId());
        entity.setRoleId(userRole.getRoleId());

        return entity;
    }
}

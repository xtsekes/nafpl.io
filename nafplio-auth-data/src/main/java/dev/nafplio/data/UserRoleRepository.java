package dev.nafplio.data;

import dev.nafplio.auth.UserRoleStore;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
final class UserRoleRepository implements PanacheRepositoryBase<UserRole, UserRolePrimaryKey>, UserRoleStore<UserRole, User, String, String> {
    @Override
    public List<UserRole> getRolesForUser(User user) {
        Objects.requireNonNull(user);

        return this.find("userId", user.getId())
                .stream()
                .map(UserRoleRepository::mapToDomain)
                .toList();
    }

    @Override
    public void addRoleToUser(String userId, String roleId) {
        if(StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(roleId)) {
            throw new IllegalArgumentException("User ID and Role ID must not be null or empty");
        }

        var userRole = UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        this.persist(mapToEntity(userRole));
    }

    @Override
    public void removeRoleFromUser(String userId, String roleId) {
        if(StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(roleId)) {
            throw new IllegalArgumentException("User ID and Role ID must not be null or empty");
        }

        var userRole = UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        this.delete(mapToEntity(userRole));
    }

    private static UserRole mapToDomain(UserRole userRole) {
        return UserRole.builder()
                .userId(userRole.getUserId())
                .roleId(userRole.getRoleId())
                .build();
    }

    private static UserRole mapToEntity(UserRole userRole) {
        var entity = new UserRole();

        entity.setUserId(userRole.getUserId());
        entity.setRoleId(userRole.getRoleId());

        return entity;
    }
}

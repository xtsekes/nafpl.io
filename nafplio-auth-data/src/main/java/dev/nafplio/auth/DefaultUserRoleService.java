package dev.nafplio.auth;

import dev.nafplio.auth.core.RoleService;
import dev.nafplio.auth.core.UserRoleService;
import dev.nafplio.data.Role;
import dev.nafplio.data.User;
import dev.nafplio.data.UserRole;
import jakarta.enterprise.context.Dependent;

@Dependent
final class DefaultUserRoleService extends UserRoleService<dev.nafplio.data.UserRole, dev.nafplio.data.User, dev.nafplio.data.Role, String, String> {
    public DefaultUserRoleService(
            RoleService<Role, String> roleService,
            UserRoleStore<UserRole, User, String, String> userRoleStore) {
        super(roleService, userRoleStore);
    }
}

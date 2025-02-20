package dev.nafplio.auth;

import dev.nafplio.auth.core.RoleService;
import dev.nafplio.data.Role;
import jakarta.enterprise.context.Dependent;

@Dependent
final class DefaultRoleService extends RoleService<Role, String> {
    public DefaultRoleService(RoleStore<Role, String> roleStore) {
        super(roleStore);
    }
}

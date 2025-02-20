package dev.nafplio.auth;

import java.util.List;

public interface UserRoleService {
    List<Role> getRolesForUser(User user);

    void addRoleToUser(User user, Role role);

    void removeRoleFromUser(User user, Role role);
}

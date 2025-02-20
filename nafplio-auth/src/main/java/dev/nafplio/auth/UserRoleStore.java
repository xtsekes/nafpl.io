package dev.nafplio.auth;

import java.util.List;

public interface UserRoleStore {
    List<UserRole> getRolesForUser(User user);

    void addRoleToUser(UserRole userRole);

    void removeRoleFromUser(UserRole userRole);
}

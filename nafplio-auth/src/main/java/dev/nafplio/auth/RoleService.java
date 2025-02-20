package dev.nafplio.auth;

import java.util.Optional;

public interface RoleService {
    Optional<Role> get(String id);

    Optional<Role> getRoleByName(String name);

    Optional<Role> getByNormalizedName(String normalizedName);

    Role add(Role role);

    void update(Role role);

    void delete(Role role);
}

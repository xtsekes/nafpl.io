package dev.nafplio.auth;

import java.util.Optional;

public interface RoleStore {
    Optional<Role> get(String id);

    Optional<Role> getByNormalizedName(String normalizedName);

    Role add(Role role);

    void update(Role role);

    void delete(Role role);
}

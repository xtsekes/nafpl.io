package dev.nafplio.auth;

import java.util.Optional;

public interface RoleStore<TRole extends Role<TKey>, TKey> {
    Optional<TRole> get(TKey id);

    Optional<TRole> getByNormalizedName(String normalizedName);

    TRole add(TRole role);

    void update(TRole role);

    void delete(TRole role);
}

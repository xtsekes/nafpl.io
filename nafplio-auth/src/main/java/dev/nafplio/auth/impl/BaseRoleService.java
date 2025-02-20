package dev.nafplio.auth.impl;

import dev.nafplio.auth.*;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

//@AllArgsConstructor
//public abstract class BaseRoleService<TRole extends Role<TKey>, TKey> implements RoleService<TRole, TKey> {
//    private final RoleStore<TRole, TKey> roleStore;
//
//    @Override
//    public final Optional<TRole> get(TKey id) {
//        return this.roleStore.get(id);
//    }
//
//    @Override
//    public final Optional<TRole> getRoleByName(String name) {
//        return this.getByNormalizedName(Roles.normalizeName(name));
//    }
//
//    @Override
//    public final Optional<TRole> getByNormalizedName(String normalizedName) {
//        return this.roleStore.getByNormalizedName(normalizedName);
//    }
//
//    @Override
//    public final TRole add(TRole role) {
//        Objects.requireNonNull(role);
//
//        this.roleStore.get(role.getId())
//                .ifPresent(r -> {
//                    throw new RoleAlreadyExists();
//                });
//
//        return this.roleStore.add(role);
//    }
//
//    @Override
//    public final void update(TRole role) {
//        Objects.requireNonNull(role);
//
//        this.roleStore.get(role.getId())
//                .orElseThrow(RoleNotFoundException::new);
//
//        this.roleStore.update(role);
//    }
//
//    @Override
//    public final void delete(TRole role) {
//        Objects.requireNonNull(role);
//
//        this.roleStore.get(role.getId())
//                .orElseThrow(RoleNotFoundException::new);
//
//        this.roleStore.delete(role);
//    }
//}
package dev.nafplio.auth.impl;

import dev.nafplio.auth.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

//@AllArgsConstructor
//public abstract class BaseUserRoleService<
//        TUserRole extends UserRole<TUserKey, TRoleKey>,
//        TUser extends User<TUserKey>,
//        TRole extends Role<TRoleKey>,
//        TUserKey,
//        TRoleKey>
//        implements UserRoleService<TUser, TRole, TUserKey, TRoleKey> {
//    private final RoleService<TRole, TRoleKey> roleService;
//    private final UserRoleStore<TUserRole, TUser, TUserKey, TRoleKey> userRoleStore;
//
//    @Override
//    public final List<TRole> getRolesForUser(TUser user) {
//        Objects.requireNonNull(user);
//
//        return this.userRoleStore.getRolesForUser(user)
//                .stream()
//                .map(x -> this.roleService.get(x.getRoleId()).orElseThrow(RoleNotFoundException::new))
//                .toList();
//    }
//
//    @Override
//    public final void addRoleToUser(TUser user, TRole role) {
//        Objects.requireNonNull(user);
//        Objects.requireNonNull(role);
//
//        this.userRoleStore.addRoleToUser(user.getId(), role.getId());
//    }
//
//    @Override
//    public final void removeRoleFromUser(TUser user, TRole role) {
//        Objects.requireNonNull(user);
//        Objects.requireNonNull(role);
//
//        this.userRoleStore.removeRoleFromUser(user.getId(), role.getId());
//    }
//}
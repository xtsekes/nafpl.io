package dev.nafplio.auth;

import java.util.List;

public interface UserRoleStore<TUserRole extends UserRole<TUserKey, TRoleKey>, TUser extends User<TUserKey>, TUserKey, TRoleKey> {
    List<TUserRole> getRolesForUser(TUser user);

    void addRoleToUser(TUserKey userId, TRoleKey roleId);

    void removeRoleFromUser(TUserKey userId, TRoleKey roleId);
}

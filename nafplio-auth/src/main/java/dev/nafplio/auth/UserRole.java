package dev.nafplio.auth;

public interface UserRole<TUserKey, TRoleKey> {
    TUserKey getUserId();

    void setUserId(TUserKey userId);

    TRoleKey getRoleId();

    void setRoleId(TRoleKey roleId);
}
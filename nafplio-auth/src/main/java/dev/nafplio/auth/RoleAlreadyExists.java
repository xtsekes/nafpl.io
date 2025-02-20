package dev.nafplio.auth;

public final class RoleAlreadyExists extends RuntimeException {
    public RoleAlreadyExists() {
        super("Role already exists");
    }
}

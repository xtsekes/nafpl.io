package dev.nafplio.auth;

public final class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super("Role not found");
    }
}
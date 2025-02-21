package dev.nafplio.web;

import dev.nafplio.data.User;

import java.util.Optional;

public interface UserResolver {
    Optional<User> resolve();
}

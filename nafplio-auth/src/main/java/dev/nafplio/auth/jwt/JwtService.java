package dev.nafplio.auth.jwt;

public interface JwtService {
    String generate(Claims claims, String... keys);
}

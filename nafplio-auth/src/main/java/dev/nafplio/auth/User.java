package dev.nafplio.auth;

public interface User<TKey> {
    TKey getId();

    void setId(TKey id);

    String getEmail();

    void setEmail(String email);

    String getNormalizedEmail();

    void setNormalizedEmail(String normalizedEmail);
}
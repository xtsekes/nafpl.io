package dev.nafplio.auth;

public interface Role<TKey> {
    TKey getId();

    void setId(TKey id);

    String getName();

    void setName(String name);

    String getNormalizedName();

    void setNormalizedName(String normalizedName);
}

package dev.nafplio.ai.response;

public interface AIResponseMapper {
    boolean allowed();

    String map(String response);
}
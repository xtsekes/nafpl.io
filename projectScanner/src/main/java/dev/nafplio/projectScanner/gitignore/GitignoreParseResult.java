package dev.nafplio.projectScanner.gitignore;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

record GitignoreParseResult(Path gitignorePath, List<String> patterns) {

}

class GitignoreParser {
    private GitignoreParser() {
    }

    public static GitignoreParseResult parse(Path gitignorePath) throws IOException {
        Objects.requireNonNull(gitignorePath);

        var basePath = gitignorePath.getParent();

        var patterns = Files.readAllLines(gitignorePath).stream()
                .filter(x -> !x.trim().isEmpty() && !x.startsWith("#"))
                .toList();

        return new GitignoreParseResult(basePath, patterns);
    }
}

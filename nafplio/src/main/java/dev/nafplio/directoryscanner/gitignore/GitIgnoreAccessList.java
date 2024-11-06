package dev.nafplio.directoryscanner.gitignore;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface GitIgnoreAccessList {
    boolean allowed(Path path);

    static GitIgnoreAccessList create(Path gitIgnorePath) throws IOException {
        return new DefaultGitIgnoreAccessList(gitIgnorePath.getParent(), GitignoreParser.parse(gitIgnorePath).patterns());
    }

    static GitIgnoreAccessList create(Path rootDirectory, List<String> patterns) {
        return new DefaultGitIgnoreAccessList(rootDirectory, patterns);
    }
}
package dev.nafplio.projectScanner.gitignore;


import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class DefaultGitIgnoreAccessList implements GitIgnoreAccessList {
    private final Path rootDirectory;
    private final List<PathMatcher> matchers;

    DefaultGitIgnoreAccessList(Path rootDirectory, List<String> patterns) {
        this.rootDirectory = rootDirectory;
        this.matchers = getMatchers(patterns);
    }

    private static List<PathMatcher> getMatchers(List<String> patterns) {
        var matchers = new ArrayList<PathMatcher>();

        for (String pattern : patterns) {
            if (!pattern.trim().isEmpty() && !pattern.startsWith("#")) {
                var glob = "glob:" + pattern.trim();

                if (glob.endsWith("/")) {
                    matchers.add(FileSystems.getDefault().getPathMatcher(glob.substring(0, glob.length() - 1)));
                    matchers.add(FileSystems.getDefault().getPathMatcher(glob + "**"));
                }

                matchers.add(FileSystems.getDefault().getPathMatcher(glob));
            }
        }

        return matchers;
    }

    public boolean allowed(Path path) {
        Path relativePath = this.rootDirectory.relativize(path);
        String pathToMatch = relativePath.toString().replace(File.separatorChar, '/');

        for (PathMatcher matcher : matchers) {
            if (matcher.matches(Paths.get(pathToMatch))) {
                return false;
            }
        }

        return true;
    }
}

package dev.nafplio.directoryscanner.gitignore;

import java.nio.file.Path;

public class GitIgnoreAccessListNode implements GitIgnoreAccessList {
    private final GitIgnoreAccessList parent;
    private final GitIgnoreAccessList accessList;

    public GitIgnoreAccessListNode(GitIgnoreAccessList parent, GitIgnoreAccessList accessList) {
        this.parent = parent;
        this.accessList = accessList;
    }

    @Override
    public boolean allowed(Path path) {
        if (!parent.allowed(path)) {
            return false;
        }

        return accessList.allowed(path);
    }
}
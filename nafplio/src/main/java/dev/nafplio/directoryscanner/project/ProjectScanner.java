package dev.nafplio.directoryscanner.project;


import dev.nafplio.directoryscanner.gitignore.GitIgnoreAccessList;
import dev.nafplio.directoryscanner.gitignore.GitIgnoreAccessListNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class ProjectScanner {
    private static final Logger logger = LoggerFactory.getLogger(ProjectScanner.class);

    private ProjectScanner() {
    }

    public static void scan(String rootDirectory, Writer writer, String globalExclusion) throws IOException {
        Objects.requireNonNull(rootDirectory);
        Objects.requireNonNull(writer);

        if (globalExclusion == null) {
            globalExclusion = "";
        }
        globalExclusion = globalExclusion.trim();

        var directory = new File(rootDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path");
        }

        var accessList = GitIgnoreAccessList.create(Paths.get(rootDirectory, ".gitignore"));

        if (!globalExclusion.isBlank() && !globalExclusion.isEmpty()) {
            accessList = new GitIgnoreAccessListNode(
                    GitIgnoreAccessList.create(directory.toPath(), globalExclusion.lines().toList()),
                    accessList
            );
        }

        writer.write("Directory Structure:\n");
        printDirectoryTree(directory, "", accessList, writer);

        writer.write("\nFile Contents:\n");
        printFileContents(rootDirectory, accessList, writer);
    }

    private static void printDirectoryTree(File folder, String indent, GitIgnoreAccessList accessList, Writer writer) throws IOException {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path");
        }

        if (!accessList.allowed(folder.toPath())) {
            return;
        }

        writer.write(indent + folder.getName() + "/\n");

        var files = folder.listFiles();

        if (files == null) {
            return;
        }

        for (var file : files) {
            if (file.isDirectory()) {
                printDirectoryTree(file, indent + "  ", accessList, writer);

                continue;
            }

            if (accessList.allowed(file.toPath())) {
                writer.write(indent + "  " + file.getName() + "\n");
            }
        }
    }

    private static void printFileContents(String directoryPath, GitIgnoreAccessList accessList, Writer writer) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(accessList::allowed)
                    .forEach(path -> {
                        try {
                            writer.write("\n--- " + path.toString() + " ---\n");
                            writer.write(Files.readString(path));
                        } catch (IOException e) {
                            logger.error("Error reading file: {}", path);
                        }
                    });
        }
    }
}

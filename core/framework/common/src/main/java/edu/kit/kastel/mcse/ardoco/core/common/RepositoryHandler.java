/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.AbbreviatedObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for handling repository operations such as shallow cloning and removal of repositories.
 */
public class RepositoryHandler {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryHandler.class);

    private RepositoryHandler() {
    }

    /**
     * Performs a shallow clone of a repository to the specified location and checks out the desired commit hash.
     *
     * @param repositoryLink      the URL of the repository to clone
     * @param desiredCodeLocation the local directory to clone into
     * @param desiredHash         the commit hash to check out
     * @return true if the repository was cloned and checked out successfully, false otherwise
     */
    public static boolean shallowCloneRepository(String repositoryLink, String desiredCodeLocation, String desiredHash) {
        File codeLocation;
        try {
            codeLocation = Files.createDirectories(Paths.get(desiredCodeLocation)).toFile();
        } catch (IOException e) {
            logger.warn("An exception occurred when trying to create/locate the desired code location.", e);
            return false;
        }

        try (Git git = Git.cloneRepository().setURI(repositoryLink).setDirectory(codeLocation).setDepth(1).call()) {
            List<RevCommit> commits = new ArrayList<>();
            git.log().setMaxCount(1).call().forEach(commits::add);
            assert commits.size() == 1;
            if (commits.getFirst().getId().startsWith(AbbreviatedObjectId.fromString(desiredHash))) {
                return true;
            }

            // Checkout correct code version
            git.fetch().setUnshallow(true).call();
            git.checkout().setName(desiredHash).call();
            return true;
        } catch (GitAPIException e) {
            logger.warn("An error occurred when cloning the repository.", e);
            return false;
        }
    }

    /**
     * Removes the repository at the specified location.
     *
     * @param repositoryLocation the local directory of the repository to remove
     */
    public static void removeRepository(String repositoryLocation) {
        try {
            FileUtils.deleteDirectory(new File(repositoryLocation));
        } catch (IOException e) {
            logger.warn("An exception occurred when removing a code folder.", e);
        }
    }
}

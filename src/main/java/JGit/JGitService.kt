package JGit

import org.eclipse.jgit.api.Git
import java.io.File
import java.nio.file.Files

class JGitService(remoteRepositoryUri: String) {

    private var git: Git

    init {
        val localPath = File.createTempFile("JGitRepository", null)
        Files.delete(localPath.toPath())

        git = Git.cloneRepository()
                .setURI(remoteRepositoryUri)
                .setDirectory(localPath)
                .call()
    }
}
package JGit

import org.eclipse.jgit.api.Git
import java.io.File

class JGitService(remoteRepositoryUri: String) {

    private var git: Git

    init {
        val localPath = File.createTempFile("JGitRepository", null)
        localPath.delete()

        git = Git.cloneRepository()
                .setURI(remoteRepositoryUri)
                .setDirectory(localPath)
                .call()
    }
}
package JGit

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

class JGitService(remoteRepositoryUri: String) {

    private var git: Git

    init {
        val localPath = File.createTempFile("JGitRepository", null)
        localPath.delete()

        // Need to make an Account class with a companion object with a username and password.
        git = Git.cloneRepository()
                .setURI(remoteRepositoryUri)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(Account.USERNAME, Account.PASSWORD))
                .setDirectory(localPath)
                .call()

        System.out.println("Having repository: " + git.repository.directory)
    }
}
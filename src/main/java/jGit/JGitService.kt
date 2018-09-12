package jGit

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File



class JGitService(remoteRepositoryUri: String) {

    private var git: Git

    init {
        System.out.println("launching service")

        val localPath = File.createTempFile("JGitRepository", null)
        localPath.delete()

        git = Git.cloneRepository()
                .setURI(remoteRepositoryUri)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(Account.USERNAME, Account.PASSWORD))
                .setDirectory(localPath)
                .call()

        System.out.println("Having repository: " + git.repository.directory)
    }
}
package services

import Account
import java.io.File.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand.*
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider


class JGitService(remoteRepositoryUri: String) {

    private var git: Git

    init {
        System.out.println("launching service, please stand by...")

        val localPath = createTempFile("JGitRepository", null)
        localPath.delete()

        git = Git.cloneRepository()
                .setURI(remoteRepositoryUri)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(Account.USERNAME, Account.PASSWORD))
                .setDirectory(localPath)
                .call()

        System.out.println("Having repository: " + git.repository.directory)

        getListOfRemoteBranches()
        getNumberOfRemoteBranches()

        System.out.println("Stopping service...")
    }

    private fun getListOfRemoteBranches() {
        val branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        for (branchRef in branchCall) {
            System.out.println(branchRef.name)
        }
    }

    private fun getNumberOfRemoteBranches() {
        val branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        System.out.println(branchCall.size)
    }
}
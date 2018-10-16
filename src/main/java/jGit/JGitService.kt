package jGit

import java.io.File.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand.*
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.eclipse.jgit.treewalk.TreeWalk



class JGitService(remoteRepositoryUri: String) {

    private var git: Git
    private var branchCall: List<Ref>

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
        System.out.println("Using repository: " + git.repository.directory)

        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        getNumberOfRemoteBranches()
        getListOfRemoteBranches()
        getWhenBranchesWereFirstMade()

        System.out.println("Stopping service...")
    }

    private fun getNumberOfRemoteBranches() = System.out.println("Size of remote branches: ${branchCall.size}")

    private fun getListOfRemoteBranches() {
        System.out.println("Full list of remote branches:")
        for (branchRef in branchCall) {
            System.out.println("- ${branchRef.name}")
        }
    }

    private fun getWhenBranchesWereFirstMade() {
        for (branchRef in branchCall) {

        }
    }
}
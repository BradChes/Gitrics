package services

import Account
import models.Branches
import java.io.File.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand.*
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider


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

        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        System.out.println("Stopping service...")
    }

    private fun getListOfRemoteBranches(): List<String> {
        val branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        val branchesList: ArrayList<String> = ArrayList()

        for (branchRef in branchCall) {
            branchesList.add(branchRef.name)
        }
        return branchesList
    }

    private fun getNumberOfRemoteBranches(): Int {
        val branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        return branchCall.size
    }

    fun createBranchesObject(): Branches {
        return Branches(getListOfRemoteBranches(), getNumberOfRemoteBranches())
    }
}
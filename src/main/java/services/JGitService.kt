package services

import models.Account
import models.Branches
import java.io.File.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand.*
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

interface GitService {
    fun createBranchesObject(): Branches
}

class JGitService(remoteRepositoryUri: String): GitService {

    private var git: Git
    private var branchCall: List<Ref>

    init {
        print("Starting JGit service...")
        val localPath = createTempFile("JGitRepository", null)
        localPath.delete()

        git = Git.cloneRepository()
                .setURI(remoteRepositoryUri)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(Account.USERNAME, Account.PASSWORD))
                .setDirectory(localPath)
                .call()

        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()
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

    override fun createBranchesObject(): Branches {
        return Branches(getListOfRemoteBranches(), getNumberOfRemoteBranches())
    }
}
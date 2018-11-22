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

    // Regex
    private val featRegex = "/\\bfeat\\b/".toRegex()
    private val spikeRegex = "/\\bspike\\b/".toRegex()

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

    private fun getListOfAllRemoteBranches(): List<String> {
        val branchesList: ArrayList<String> = ArrayList()

        for (branch in branchCall) {
            branchesList.add(branch.name)
        }
        return branchesList
    }

    private fun getNumberOfAllRemoteBranches(): Int {
        return branchCall.size
    }

    private fun getListOfAllFeatureBranches(): List<String> {
        val featureBranchesList: ArrayList<String> = ArrayList()

        for(branch in branchCall) {
            if (branch.name.contains(featRegex)) {
                featureBranchesList.add(branch.name)
            }
        }
        return featureBranchesList
    }

    private fun getNumberOfAllFeatureBranches(): Int {
        var featureCount = 0

        for(branch in branchCall) {
            if (branch.name.contains(featRegex)) {
                featureCount++
            }
        }
        return featureCount
    }

    private fun getListOfAllSpikeBranches(): List<String> {
        val spikeBranchesList: ArrayList<String> = ArrayList()

        for(branch in branchCall) {
            if (branch.name.contains(spikeRegex)) {
                spikeBranchesList.add(branch.name)
            }
        }
        return spikeBranchesList
    }

    private fun getNumberOfAllSpikeBranches(): Int {
        var spikeCount = 0

        for(branch in branchCall) {
            if (branch.name.contains(spikeRegex)) {
                spikeCount++
            }
        }
        return spikeCount
    }

    override fun createBranchesObject(): Branches {
        return Branches(getListOfAllRemoteBranches(),
                getNumberOfAllRemoteBranches(),
                getListOfAllFeatureBranches(),
                getNumberOfAllFeatureBranches(),
                getListOfAllSpikeBranches(),
                getNumberOfAllSpikeBranches())
    }
}
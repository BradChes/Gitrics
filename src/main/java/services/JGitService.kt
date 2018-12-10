package services

import controllers.BranchType
import models.Account
import models.Branch
import models.Branches
import java.io.File.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand.*
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.util.*


interface GitService {
    fun createBranchesObject(type: BranchType): Branches
}

class JGitService(remoteRepositoryUri: String): GitService {

    private var git: Git
    private lateinit var branchCall: List<Ref>

    // Regex
    private val featRegex = "/\\bfeat\\b/".toRegex()
    private val spikeRegex = "/\\bspike\\b/".toRegex()
    private val fixRegex = "/\\bfix\\b/".toRegex()
    private val otherRegex = "/\\b(spike|feat|fix)\\b/".toRegex()

    init {
        val localPath = createTempFile("JGitRepository", null)
        localPath.delete()

        git = Git.cloneRepository()
                .setURI(remoteRepositoryUri)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(Account.USERNAME, Account.PASSWORD))
                .setDirectory(localPath)
                .call()
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

    private fun getListOfAllFixBranches(): List<String> {
        val fixBranchesList: ArrayList<String> = ArrayList()

        for(branch in branchCall) {
            if (branch.name.contains(fixRegex)) {
                fixBranchesList.add(branch.name)
            }
        }
        return fixBranchesList
    }

    private fun getNumberOfAllFixBranches(): Int {
        var fixCount = 0

        for(branch in branchCall) {
            if (branch.name.contains(fixRegex)) {
                fixCount++
            }
        }
        return fixCount
    }

    private fun getListOfAllOtherBranches(): List<String> {
        val otherBranchesList: ArrayList<String> = ArrayList()

        for(branch in branchCall) {
            if (!branch.name.contains(otherRegex)) {
                otherBranchesList.add(branch.name)
            }
        }
        return otherBranchesList
    }

    private fun getNumberOfAllOtherBranches(): Int {
        var otherCount = 0

        for(branch in branchCall) {
            if (!branch.name.contains(otherRegex)) {
                otherCount++
            }
        }
        return otherCount
    }

    private fun getWhenBranchesWereFirstMade(): List<Branch> {
        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        val list = mutableListOf<Branch>()

        for (name in getListOfAllRemoteBranches()) {
            if(!hasBranchBeenMerged(name)) {
                val revCommit = git.log()
                        .add(git.repository.resolve(name))
                        .not(git.repository.resolve("remotes/origin/master"))
                        .call().first()

                val authorIdent = revCommit.authorIdent
                val authorDate = authorIdent.getWhen()

                println("$name was created on the $authorDate")

                val branch = Branch(name, authorDate.toString())
                list.add(branch)
            }
        }
        return list
    }

    private fun hasBranchBeenMerged(branchName: String): Boolean {
        val revWalk = RevWalk(git.repository)
        val masterHead = revWalk.parseCommit(git.repository.resolve("refs/remotes/origin/master"))
        val branchHead = revWalk.parseCommit(git.repository.resolve(branchName))
        return revWalk.isMergedInto(branchHead, masterHead)
    }

    override fun createBranchesObject(type: BranchType): Branches {
        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        when(type) {
            BranchType.ALL -> return Branches(getListOfAllRemoteBranches(), getNumberOfAllRemoteBranches())
            BranchType.FEAT -> TODO()
            BranchType.SPIKE -> TODO()
            BranchType.FIX -> TODO()
            BranchType.OTHER -> TODO()
        }

//        return Branches(getListOfAllRemoteBranches(),
//                getNumberOfAllRemoteBranches(),
//                getListOfAllFeatureBranches(),
//                getNumberOfAllFeatureBranches(),
//                getListOfAllSpikeBranches(),
//                getNumberOfAllSpikeBranches(),
//                getListOfAllFixBranches(),
//                getNumberOfAllFixBranches(),
//                getListOfAllOtherBranches(),
//                getNumberOfAllOtherBranches(),
//                getWhenBranchesWereFirstMade())
    }
}
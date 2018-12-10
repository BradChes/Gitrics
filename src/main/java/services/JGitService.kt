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

    private fun listOfRemoteBranches(): List<Branch> {
        val branches = mutableListOf<Branch>()

        for (ref in branchCall) {
            val branch = Branch(ref.name, whenBranchesWereFirstMade(ref.name))
            branches.add(branch)
        }
        return branches
    }

    private fun numberOfRemoteBranches(): Int {
        return branchCall.size
    }

    private fun listOfFeatureBranches(): List<Branch> {
        val featureBranches= mutableListOf<Branch>()

        for(ref in branchCall) {
            if (ref.name.contains(featRegex)) {
                val branch = Branch(ref.name, whenBranchesWereFirstMade(ref.name))
                featureBranches.add(branch)
            }
        }
        return featureBranches
    }

    private fun numberOfFeatureBranches(): Int {
        var featureCount = 0

        for(ref in branchCall) {
            if (ref.name.contains(featRegex)) {
                featureCount++
            }
        }
        return featureCount
    }

    private fun listOfSpikeBranches(): List<Branch> {
        val featureBranches= mutableListOf<Branch>()

        for(ref in branchCall) {
            if (ref.name.contains(spikeRegex)) {
                val branch = Branch(ref.name, whenBranchesWereFirstMade(ref.name))
                featureBranches.add(branch)
            }
        }
        return featureBranches
    }

    private fun numberOfSpikeBranches(): Int {
        var spikeCount = 0

        for(ref in branchCall) {
            if (ref.name.contains(spikeRegex)) {
                spikeCount++
            }
        }
        return spikeCount
    }

    private fun listOfFixBranches(): List<Branch> {
        val fixBranches = mutableListOf<Branch>()

        for(ref in branchCall) {
            if (ref.name.contains(fixRegex)) {
                val branch = Branch(ref.name, whenBranchesWereFirstMade(ref.name))
                fixBranches.add(branch)
            }
        }
        return fixBranches
    }

    private fun numberOfFixBranches(): Int {
        var fixCount = 0

        for(ref in branchCall) {
            if (ref.name.contains(fixRegex)) {
                fixCount++
            }
        }
        return fixCount
    }

    private fun listOfOtherBranches(): List<Branch> {
        val otherBranches = mutableListOf<Branch>()

        for(ref in branchCall) {
            if (!ref.name.contains(otherRegex)) {
                val branch = Branch(ref.name, whenBranchesWereFirstMade(ref.name))
                otherBranches.add(branch)
            }
        }
        return otherBranches
    }

    private fun numberOfOtherBranches(): Int {
        var otherCount = 0

        for(ref in branchCall) {
            if (!ref.name.contains(otherRegex)) {
                otherCount++
            }
        }
        return otherCount
    }

    private fun whenBranchesWereFirstMade(branchName: String): String? {
            if(!hasBranchBeenMerged(branchName)) {
                val revCommit = git.log()
                        .add(git.repository.resolve(branchName))
                        .not(git.repository.resolve("remotes/origin/master"))
                        .call().first()

                val authorIdent = revCommit.authorIdent
                val authorDate = authorIdent.getWhen()

                return authorDate.toString()
            }
        return null
    }

    private fun hasBranchBeenMerged(branchName: String): Boolean {
        val revWalk = RevWalk(git.repository)
        val masterHead = revWalk.parseCommit(git.repository.resolve("refs/remotes/origin/master"))
        val branchHead = revWalk.parseCommit(git.repository.resolve(branchName))
        return revWalk.isMergedInto(branchHead, masterHead)
    }

    override fun createBranchesObject(type: BranchType): Branches {
        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        return when(type) {
            BranchType.ALL -> Branches(listOfRemoteBranches(), numberOfRemoteBranches())
            BranchType.FEAT -> Branches(listOfFeatureBranches(), numberOfFeatureBranches())
            BranchType.SPIKE -> Branches(listOfSpikeBranches(), numberOfSpikeBranches())
            BranchType.FIX -> Branches(listOfFixBranches(), numberOfFixBranches())
            BranchType.OTHER -> Branches(listOfOtherBranches(), numberOfOtherBranches())
        }
    }
}
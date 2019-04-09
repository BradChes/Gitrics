package services

import controllers.BranchType
import models.Account
import models.Branch
import models.Branches
import models.BranchesLifetime
import java.io.File.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand.*
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit


interface GitService {
    fun createBranchesObject(type: BranchType): Branches
    fun createLifetimeObject(): BranchesLifetime
}

class JGitService(account: Account): GitService {

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
                .setURI(account.repoUrl)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(account.username, account.accessToken))
                .setDirectory(localPath)
                .call()
    }

    private fun listOfRemoteBranches(): List<Branch> {
        val branches = mutableListOf<Branch>()

        for (ref in branchCall) {
            val branchName = ref.name
            val branch = Branch(branchName, whenBranchesWereFirstMade(branchName),
                    lastCommitOnBranch(branchName),
                    hasBranchBeenMerged(branchName),
                    hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
            branches.add(branch)
        }
        return branches
    }

    private fun allBranchesAverageLifetime(): String {
        val listOfDays = mutableListOf<Long>()

        for(branch in listOfRemoteBranches()) {
            if (!branch.isStale) {
                if(!branch.isMerged) {
                    val firstCreationDate = branch.firstCreation
                    val nowDate = LocalDateTime.now()
                    listOfDays.add(ChronoUnit.DAYS.between(firstCreationDate, nowDate))
                }
            }
        }
        var averageLifetime = 0L

        for(day in listOfDays) {
            averageLifetime += day
        }
        averageLifetime /= listOfDays.size
        return averageLifetime.toString()
    }

    private fun listOfFeatureBranches(): List<Branch> {
        val featureBranches= mutableListOf<Branch>()

        for(ref in branchCall) {
            val branchName = ref.name
            if (branchName.contains(featRegex)) {
                val branch = Branch(branchName, whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBranchBeenMerged(branchName),
                        hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
                featureBranches.add(branch)
            }
        }
        return featureBranches
    }

    private fun listOfSpikeBranches(): List<Branch> {
        val featureBranches= mutableListOf<Branch>()

        for(ref in branchCall) {
            val branchName = ref.name
            if (branchName.contains(spikeRegex)) {
                val branch = Branch(branchName, whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBranchBeenMerged(branchName),
                        hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
                featureBranches.add(branch)
            }
        }
        return featureBranches
    }

    private fun listOfFixBranches(): List<Branch> {
        val fixBranches = mutableListOf<Branch>()
        for(ref in branchCall) {
            val branchName = ref.name
            if (branchName.contains(fixRegex)) {
                val branch = Branch(branchName, whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBranchBeenMerged(branchName),
                        hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
                fixBranches.add(branch)
            }
        }
        return fixBranches
    }

    private fun listOfOtherBranches(): List<Branch> {
        val otherBranches = mutableListOf<Branch>()

        for(ref in branchCall) {
            val branchName = ref.name
            if (!branchName.contains(otherRegex)) {
                val branch = Branch(branchName, whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBranchBeenMerged(branchName),
                        hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
                otherBranches.add(branch)
            }
        }
        return otherBranches
    }

    private fun listOfUnmergedBranches(): List<Branch> {
        val unmergedBranches = mutableListOf<Branch>()

        for(ref in branchCall) {
            val branchName = ref.name
            val hasBeenMerged = hasBranchBeenMerged(branchName)
            if(!hasBeenMerged) {
                val branch = Branch(branchName,
                        whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBeenMerged,
                        hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
                unmergedBranches.add(branch)
            }
        }
        return unmergedBranches
    }

    private fun listOfMergedBranches(): List<Branch> {
        val mergedBranches = mutableListOf<Branch>()

        for(ref in branchCall) {
            val branchName = ref.name
            val hasBeenMerged = hasBranchBeenMerged(branchName)
            if(hasBeenMerged) {
                val branch = Branch(branchName,
                        whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBeenMerged,
                        hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
                mergedBranches.add(branch)
            }
        }
        return mergedBranches
    }

    private fun listOfStaleBranches(): List<Branch> {
        val staleBranches = mutableListOf<Branch>()

        for(ref in branchCall) {
            val branchName = ref.name
            val hasBeenMerged = hasBranchBeenMerged(branchName)
            if(hasBranchGoneStale(lastCommitOnBranch(branchName))) {
                val branch = Branch(branchName,
                        whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBeenMerged,
                        hasBranchGoneStale(lastCommitOnBranch(branchName)))
                staleBranches.add(branch)
            }
        }
        return staleBranches
    }

    private fun hasBranchGoneStale(lastCommitDate: LocalDateTime?): Boolean {
        val lastCommit = lastCommitDate ?: run { return false }
        val nowDate = LocalDateTime.now()
        val differentInDays = ChronoUnit.DAYS.between(lastCommit, nowDate)
        if(differentInDays >= 30) {
            return true
        }
        return false
    }

    private fun lastCommitOnBranch(branchName: String): LocalDateTime? {
        if(!hasBranchBeenMerged(branchName)) {
            val revCommit = git.log()
                    .add(git.repository.resolve(branchName))
                    .not(git.repository.resolve("remotes/origin/master"))
                    .call().first()

            val authorIdent = revCommit.authorIdent
            return LocalDateTime.ofInstant(authorIdent.getWhen().toInstant(), ZoneId.systemDefault())
        }
        return null
    }

    private fun whenBranchesWereFirstMade(branchName: String): LocalDateTime? {
            if(!hasBranchBeenMerged(branchName)) {
                val revCommit = git.log()
                        .add(git.repository.resolve(branchName))
                        .not(git.repository.resolve("remotes/origin/master"))
                        .call().last()

                val authorIdent = revCommit.authorIdent
                return LocalDateTime.ofInstant(authorIdent.getWhen().toInstant(), ZoneId.systemDefault())
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

        val branchListType = when(type) {
            BranchType.ALL -> listOfRemoteBranches()
            BranchType.FEAT -> listOfFeatureBranches()
            BranchType.SPIKE -> listOfSpikeBranches()
            BranchType.FIX -> listOfFixBranches()
            BranchType.OTHER -> listOfOtherBranches()
            BranchType.UNMERGED -> listOfUnmergedBranches()
            BranchType.MERGED -> listOfMergedBranches()
            BranchType.STALE -> listOfStaleBranches()
        }

        return Branches(branchListType, branchListType.count())
    }

    override fun createLifetimeObject(): BranchesLifetime {
        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        return BranchesLifetime(allBranchesAverageLifetime())
    }
}
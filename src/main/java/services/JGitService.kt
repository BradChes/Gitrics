package services

import controllers.BranchType
import models.configs.Account
import models.configs.Options
import models.data.Branch
import models.data.Branches
import models.data.BranchesLifetime
import java.io.File.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand.*
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


interface GitService {
    fun createBranchesObject(id: Int, type: BranchType): Branches
    fun createLifetimeObject(id: Int): BranchesLifetime
}

class JGitService(private val options: Options, private val account: Account): GitService {

    private lateinit var git: Git
    private lateinit var branchCall: List<Ref>
    private val pathList = mutableListOf<File>()

    // Regex
    private val featRegex = "/\\bfeat\\b/".toRegex()
    private val spikeRegex = "/\\bspike\\b/".toRegex()
    private val fixRegex = "/\\bfix\\b/".toRegex()
    private val otherRegex = "/\\b(spike|feat|fix)\\b/".toRegex()

    init {
        gitRepositoryCreation()
    }

    private fun gitRepositoryCreation() {

        for(path in account.repoUrls) {
            val localPath = createTempFile("JGitRepository", null)
            localPath.delete()

            pathList.add(localPath)

            Git.cloneRepository()
                    .setURI(path)
                    .setCredentialsProvider(UsernamePasswordCredentialsProvider(account.username, account.accessToken))
                    .setDirectory(localPath)
                    .call()
        }

        git = Git.open(pathList[1])
    }

    override fun createBranchesObject(id: Int, type: BranchType): Branches {
        git = Git.open(pathList[id])
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

    override fun createLifetimeObject(id: Int): BranchesLifetime {
        git = Git.open(pathList[id])
        branchCall = git.branchList().setListMode(ListMode.REMOTE).call()

        return BranchesLifetime(averageBranchesLifetime(BranchType.ALL),
                averageBranchesLifetime(BranchType.FEAT),
                averageBranchesLifetime(BranchType.SPIKE),
                averageBranchesLifetime(BranchType.FIX))
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
        val spikeBranches= mutableListOf<Branch>()

        for(ref in branchCall) {
            val branchName = ref.name
            if (branchName.contains(spikeRegex)) {
                val branch = Branch(branchName, whenBranchesWereFirstMade(branchName),
                        lastCommitOnBranch(branchName),
                        hasBranchBeenMerged(branchName),
                        hasBranchGoneStale(whenBranchesWereFirstMade(branchName)))
                spikeBranches.add(branch)
            }
        }
        return spikeBranches
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
        if(differentInDays >= options.staleDefinition) {
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

    private fun averageBranchesLifetime(type: BranchType): Int {
        val listOfDays = mutableListOf<Int>()

        val branchList = when(type) {
            BranchType.ALL -> listOfRemoteBranches()
            BranchType.FEAT -> listOfFeatureBranches()
            BranchType.SPIKE -> listOfSpikeBranches()
            BranchType.FIX -> listOfFixBranches()
            else -> emptyList()
        }

        for(branch in branchList) {
            if (!branch.isStale) {
                if(!branch.isMerged) {
                    val firstCreationDate = branch.firstCreation
                    val nowDate = LocalDateTime.now()
                    listOfDays.add(ChronoUnit.DAYS.between(firstCreationDate, nowDate).toInt())
                }
            }
        }
        var averageLifetime = 0

        for(day in listOfDays) {
            averageLifetime += day
        }
        if (listOfDays.size != 0) {
            averageLifetime /= listOfDays.size
        }

        return averageLifetime
    }
}

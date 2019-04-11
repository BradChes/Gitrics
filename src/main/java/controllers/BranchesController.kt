package controllers

import models.data.Branches
import models.data.BranchesLifetime
import org.springframework.web.bind.annotation.*
import services.GitService
import services.JGitService
import utils.ConfigReader

@RestController
class BranchesController(configPath: String) {

    private val jGitService: GitService

    init {
        val reader = ConfigReader(configPath)
        val options = reader.parsedConfigToOptions()
        val account = reader.parsedConfigToAccount()
        jGitService = JGitService(options, account)
    }

    @RequestMapping("{repoName}/branches")
    fun getBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName, BranchType.ALL)
    }

    @RequestMapping("{repoName}/branches/feat")
    fun getFeatBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName, BranchType.FEAT)
    }

    @RequestMapping("{repoName}/branches/spike")
    fun getSpikeBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName,BranchType.SPIKE)
    }

    @RequestMapping("{repoName}/branches/fix")
    fun getFixBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName,BranchType.FIX)
    }

    @RequestMapping("{repoName}/branches/other")
    fun getOtherBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName,BranchType.OTHER)
    }

    @RequestMapping("{repoName}/branches/unmerged")
    fun getUnmergedBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName,BranchType.UNMERGED)
    }

    @RequestMapping("{repoName}/branches/merged")
    fun getMergedBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName,BranchType.MERGED)
    }

    @RequestMapping("{repoName}/branches/stale")
    fun getStaleBranches(@PathVariable repoName: String): Branches {
        return jGitService.createBranchesObject(repoName,BranchType.STALE)
    }

    @RequestMapping("{repoName}/branches/lifetime")
    fun getAverageLifetime(@PathVariable repoName: String): BranchesLifetime {
        return jGitService.createLifetimeObject(repoName)
    }
}

enum class BranchType {
    ALL,
    FEAT,
    SPIKE,
    FIX,
    OTHER,
    UNMERGED,
    MERGED,
    STALE
}
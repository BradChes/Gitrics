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

    @RequestMapping("{id}/branches")
    fun getBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id, BranchType.ALL)
    }

    @RequestMapping("{id}/branches/feat")
    fun getFeatBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id, BranchType.FEAT)
    }

    @RequestMapping("{id}/branches/spike")
    fun getSpikeBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id,BranchType.SPIKE)
    }

    @RequestMapping("{id}/branches/fix")
    fun getFixBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id,BranchType.FIX)
    }

    @RequestMapping("{id}/branches/other")
    fun getOtherBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id,BranchType.OTHER)
    }

    @RequestMapping("{id}/branches/unmerged")
    fun getUnmergedBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id,BranchType.UNMERGED)
    }

    @RequestMapping("{id}/branches/merged")
    fun getMergedBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id,BranchType.MERGED)
    }

    @RequestMapping("{id}/branches/stale")
    fun getStaleBranches(@PathVariable id: Int): Branches {
        return jGitService.createBranchesObject(id,BranchType.STALE)
    }

    @RequestMapping("{id}/branches/lifetime")
    fun getAverageLifetime(@PathVariable id: Int): BranchesLifetime {
        return jGitService.createLifetimeObject(id)
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
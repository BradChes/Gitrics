package controllers

import models.Branches
import org.springframework.web.bind.annotation.*
import services.GitService
import services.JGitService
import utils.ConfigReader

@RestController
class BranchesController {

    private val jGitService: GitService

    init {
        val account = ConfigReader("").jsonToAccount()
        jGitService = JGitService(account)
    }

    @RequestMapping("/branches")
    fun getBranches(): Branches {
        return jGitService.createBranchesObject(BranchType.ALL)
    }

    @RequestMapping("/branches/feat")
    fun getFeatBranches(): Branches {
        return jGitService.createBranchesObject(BranchType.FEAT)
    }

    @RequestMapping("/branches/spike")
    fun getSpikeBranches(): Branches {
        return jGitService.createBranchesObject(BranchType.SPIKE)
    }

    @RequestMapping("/branches/fix")
    fun getFixBranches(): Branches {
        return jGitService.createBranchesObject(BranchType.FIX)
    }

    @RequestMapping("/branches/other")
    fun getOtherBranches(): Branches {
        return jGitService.createBranchesObject(BranchType.OTHER)
    }

    @RequestMapping("/branches/unmerged")
    fun getUnmergedBranches(): Branches {
        return jGitService.createBranchesObject(BranchType.UNMERGED)
    }

    @RequestMapping("/branches/stale")
    fun getStaleBranches(): Branches {
        return jGitService.createBranchesObject(BranchType.STALE)
    }

}

enum class BranchType {
    ALL,
    FEAT,
    SPIKE,
    FIX,
    OTHER,
    UNMERGED,
    STALE
}
package controllers

import models.Account
import models.Branches
import org.springframework.web.bind.annotation.*
import services.GitService
import services.JGitService

@RestController
class BranchesController {

    private val jGitService: GitService

    init {
        val remoteRepositoryUri = Account.REMOTE_REPO_URI
        jGitService = JGitService(remoteRepositoryUri)
    }

    @RequestMapping("/branches")
    fun getBranches(): Branches {
        return jGitService.createBranchesObject()
    }

}
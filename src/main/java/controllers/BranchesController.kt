package controllers

import models.Account
import models.Branches
import org.springframework.web.bind.annotation.*
import services.JGitService

@RestController
class BranchesController {

    private val jGitService: JGitService

    init {
        val remoteRepositoryUri = Account.REMOTE_REPO_URI
        jGitService = JGitService(remoteRepositoryUri)
    }

    @RequestMapping("/branches")
    fun getBranches(): Branches {
        return jGitService.createBranchesObject()
    }

}
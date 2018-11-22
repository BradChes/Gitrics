package application

import controllers.BranchesController
import models.Account
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import services.JGitService


@EnableAutoConfiguration
@Configuration
class App {

    private val jGitService: JGitService

    init {
        val remoteRepositoryUri = Account.REMOTE_REPO_URI
        jGitService = JGitService(remoteRepositoryUri)
    }

    @Bean
    fun branchesController() = BranchesController()
}



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
    @Bean
    fun branchesController() = BranchesController()
}



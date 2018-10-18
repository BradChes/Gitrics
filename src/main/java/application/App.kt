package application

import controllers.BranchesController
import models.Account
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import services.GsonService
import services.JGitService


@EnableAutoConfiguration
@Configuration
class App {

    @Bean
    fun branchesController() = BranchesController()

    private val jGitService: JGitService
    private val gsonService: GsonService

    init {
        val remoteRepositoryUri = Account.REMOTE_REPO_URI
        jGitService = JGitService(remoteRepositoryUri)
        gsonService = GsonService()

        val json = gsonService.branchesObjectToJson(jGitService.createBranchesObject())

        System.out.println(json)
    }
}

fun main(args : Array<String>) {
    SpringApplication.run(App::class.java, *args)
}



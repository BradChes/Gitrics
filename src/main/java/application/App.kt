package application

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class App

fun main(args : Array<String>) {
    SpringApplication.run(App::class.java, *args)
}

//class App {
//
//    private val jGitService: JGitService
//    private val gsonService: GsonService
//
//    init {
//        val remoteRepositoryUri = Account.REMOTE_REPO_URI
//        jGitService = JGitService(remoteRepositoryUri)
//        gsonService = GsonService()
//
//        val json = gsonService.branchesObjectToJson(jGitService.createBranchesObject())
//
//        System.out.println(json)
//    }
//}
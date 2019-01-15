package application

import controllers.BranchesController
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@EnableAutoConfiguration
@Configuration
class App {
    @Bean
    fun branchesController() = BranchesController()
}



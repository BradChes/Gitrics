package application

import controllers.BranchesController
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableAutoConfiguration
@Configuration
class Application: ApplicationRunner {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

    private val logger = LoggerFactory.getLogger(Application::class.java)


    override fun run(args: ApplicationArguments?) {
        logger.info("OptionNames: {}", args?.optionNames)

        val containsOption = args?.containsOption("config")
        logger.info("Contains config: $containsOption")
    }

    @Bean
    fun branchesController() = BranchesController()
}
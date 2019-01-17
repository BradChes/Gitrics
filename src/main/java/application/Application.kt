package application

import controllers.BranchesController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application: ApplicationRunner {
    private val logger = LoggerFactory.getLogger(Application::class.java)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

    override fun run(args: ApplicationArguments?) {
        logger.info("OptionNames: {}", args?.optionNames)

        val containsOption = args?.containsOption("config")
        logger.info("Contains config: $containsOption")
    }

    @Bean
    fun branchesController(@Value("\${config}")configPath: String) = BranchesController(configPath)
}
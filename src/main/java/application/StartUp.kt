package application

import org.springframework.boot.SpringApplication

open class StartUp {
    companion object {
        @JvmStatic fun main(args : Array<String>) {
            SpringApplication.run(App::class.java, *args)
        }
    }
}
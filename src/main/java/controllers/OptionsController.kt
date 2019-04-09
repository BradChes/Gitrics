package controllers

import org.springframework.web.bind.annotation.*
import services.OptionsService
import services.ParsedOptionsService
import models.Options
import utils.ConfigReader

@RestController
class OptionsController(configPath: String) {

    private val optionsService: OptionsService

    init {
        val options = ConfigReader(configPath).parsedConfigToOptions()
        optionsService = ParsedOptionsService(options)
    }

    @RequestMapping("/options")
    fun getOptions(): Options {
        return optionsService.createOptionsObject()
    }
}

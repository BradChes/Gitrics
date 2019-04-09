package controllers

import org.springframework.web.bind.annotation.*
import services.OptionsService
import services.ParsedOptionsService
import models.Options

@RestController
class OptionsController {

    private val optionsService: OptionsService

    init {
        optionsService = ParsedOptionsService()
    }

    @RequestMapping("/options")
    fun getOptions(): Options {
        return optionsService.createOptionsObject()
    }
}

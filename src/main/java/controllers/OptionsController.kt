package controllers

import org.springframework.web.bind.annotation.*
import services.Options
import services.OptionsService
import services.ParsedOptionsService

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

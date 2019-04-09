package controllers

import org.springframework.web.bind.annotation.*

@RestController
class OptionsController {

    @RequestMapping("/options")
    fun getOptions(): String {
        return "Options"
    }
}

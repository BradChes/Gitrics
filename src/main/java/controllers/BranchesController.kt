package controllers

import org.springframework.web.bind.annotation.*

@RestController
class BranchesController {
    @RequestMapping("/branches")
    fun answer() = "Here be branches"
}
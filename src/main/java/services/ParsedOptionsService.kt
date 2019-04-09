package services

import models.Options

interface OptionsService {
    fun createOptionsObject(): Options
}

class ParsedOptionsService: OptionsService {
    override fun createOptionsObject(): Options {
        return Options("repoUrl", 5, 15)
    }

}
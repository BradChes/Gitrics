package services

import models.configs.Options

interface OptionsService {
    fun createOptionsObject(): Options
}

class ParsedOptionsService(private val options: Options): OptionsService {

    override fun createOptionsObject(): Options {
        return options
    }
}
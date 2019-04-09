package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import models.ParsedConfig
import java.io.File

class ConfigReader(private val configPath: String?) {

    fun jsonToAccount(): ParsedConfig {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(File(configPath), ParsedConfig::class.java)
    }
}

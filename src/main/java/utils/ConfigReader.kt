package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import models.Account
import models.Options
import models.ParsedConfig
import java.io.File

class ConfigReader(private val configPath: String?) {

    private val parsedConfig = jsonToParsedConfig()

    private fun jsonToParsedConfig(): ParsedConfig {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(File(configPath), ParsedConfig::class.java)
    }

    fun parsedConfigToAccount(): Account {
        return Account(parsedConfig.username, parsedConfig.accessToken, parsedConfig.repoUrls[0])
    }

    fun parsedConfigToOptions(): Options {
        return Options(parsedConfig.branchMinimum, parsedConfig.branchMaximum, parsedConfig.staleDefinition)
    }
}

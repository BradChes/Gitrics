package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import models.configs.Account
import models.configs.Options
import models.configs.ParsedConfig
import java.io.File

class ConfigReader(private val configPath: String?) {

    private val parsedConfig = jsonToParsedConfig()

    private fun jsonToParsedConfig(): ParsedConfig {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(File(configPath), ParsedConfig::class.java)
    }

    fun parsedConfigToAccount(): Account {
        return Account(parsedConfig.username,
                parsedConfig.accessToken,
                parsedConfig.repoUrls)
    }

    fun parsedConfigToOptions(): Options {
        return Options(parsedConfig.repoPath,
                parsedConfig.branchMinimum,
                parsedConfig.branchMaximum,
                parsedConfig.branchesAverageLifetimeMinimumThreshold,
                parsedConfig.branchesAverageLifetimeMaximumThreshold,
                parsedConfig.staleDefinition)
    }
}

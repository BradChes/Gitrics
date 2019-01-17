package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import models.Account
import java.io.File

class ConfigReader(private val configPath: String?) {

    fun jsonToAccount(): Account {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(File(configPath), Account::class.java)
    }
}

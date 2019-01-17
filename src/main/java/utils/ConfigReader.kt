package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import models.Account
import java.io.File

class ConfigReader(private val configPath: String) {

    private val testJson =
    """
        {
            "username": "",
            "accessToken": "",
            "repoUrl": ""
        }
    """

    fun jsonToAccount(fromFile: Boolean): Account {
        return when(fromFile) {
            true -> jsonToAccountFromFile()
            false -> jsonToAccountFromString()
        }
    }

    private fun jsonToAccountFromString(): Account {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(testJson, Account::class.java)
    }

    private fun jsonToAccountFromFile(): Account {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(File(configPath), Account::class.java)
    }
}

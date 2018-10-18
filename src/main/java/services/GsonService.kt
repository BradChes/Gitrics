package services

import com.google.gson.Gson
import models.Branches

class GsonService {

    private val gson: Gson = Gson()

    fun branchesObjectToJson(branches: Branches): String {
        return gson.toJson(branches)
    }
}
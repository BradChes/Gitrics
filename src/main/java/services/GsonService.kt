package services

import com.google.gson.Gson
import models.Branches

class GsonService {

    private val gson: Gson = Gson()

    init {
        // To String
        val dummyBranches = Branches(listOf("branches1", "branches2"), 2)
        val jsonString = gson.toJson(dummyBranches)
        System.out.println(jsonString)
    }
}
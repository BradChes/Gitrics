package services

import models.Branches
import org.junit.Test
import org.junit.Assert.*


class GsonServiceTests {

    private val expectedValue =  """{"listOfBranches":["branch1","branch2","branch3","branch4"],"numberOfBranches":4}"""

    private val gsonService = GsonService()

    @Test
    fun testThatABranchesObjectGetsConvertedIntoJson() {
        val dummyBranches = Branches(listOf("branch1","branch2","branch3","branch4"), 4)
        val result = gsonService.branchesObjectToJson(dummyBranches)
        assertEquals(expectedValue, result)
    }
}
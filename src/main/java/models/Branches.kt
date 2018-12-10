package models

//data class Branches(val allBranchesList: List<String>,
//                    val allBranchesSize: Int,
//                    val featureBranchesList: List<String>,
//                    val featureBranchesSize: Int,
//                    val spikeBranchesList: List<String>,
//                    val spikeBranchesSize: Int,
//                    val fixBranchesList: List<String>,
//                    val fixBranchesSize: Int,
//                    val otherBranchesList: List<String>,
//                    val otherBranchesSize: Int,
//                    val whenBranchesWereFirstMade: List<Branch>)

data class Branches(val branches: List<Branch>,
                    val size: Int)
package models

data class Branches(val allBranchesList: List<String>,
                    val allBranchesSize: Int,
                    val featureBranchesList: List<String>,
                    val featureBranchesSize: Int,
                    val spikeBranchesList: List<String>,
                    val spikeBranchesSize: Int)
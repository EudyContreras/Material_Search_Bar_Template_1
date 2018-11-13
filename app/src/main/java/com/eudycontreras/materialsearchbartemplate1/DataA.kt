package com.eudycontreras.materialsearchbartemplate1

/**
 * Created by eudycontreras.
 */
data class DataA(var id: Int = 0, var name: String = ""){

    override fun toString(): String {
        return "name: $name id: $id"
    }
}

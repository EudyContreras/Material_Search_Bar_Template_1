package com.eudycontreras.materialsearchbartemplate1

/**
 * Created by eudycontreras.
 */
data class DataB(var id: Int = 0, var title: String = ""){

    override fun toString(): String {
        return "name: $title id: $id"
    }
}

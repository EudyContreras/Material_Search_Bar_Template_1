package com.eudycontreras.materialsearchbartemplate1

import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchableData
import kotlin.reflect.KMutableProperty0

/**
 * Created by eudycontreras.
 */
data class DataB(var id: Int = 0, var title: String = ""): SearchableData {
    override fun getDataId(): Int {
        return id
    }

    override fun getDataTarget(): Array<KMutableProperty0<String>> {
        return arrayOf(::title)
    }

    override fun toString(): String {
        return "title: $title id: $id"
    }
}

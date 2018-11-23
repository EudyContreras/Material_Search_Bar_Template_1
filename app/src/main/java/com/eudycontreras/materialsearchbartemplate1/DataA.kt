package com.eudycontreras.materialsearchbartemplate1

import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchableData
import kotlin.reflect.KMutableProperty0

/**
 * Created by eudycontreras.
 */
data class DataA(var id: Int = 0, var name: String = "") : SearchableData{
    override fun getDataId(): Int {
        return id
    }

    override fun getDataTarget(): Array<KMutableProperty0<String>> {
        return arrayOf(::name)
    }

    override fun toString(): String {
        return "name: $name id: $id"
    }
}

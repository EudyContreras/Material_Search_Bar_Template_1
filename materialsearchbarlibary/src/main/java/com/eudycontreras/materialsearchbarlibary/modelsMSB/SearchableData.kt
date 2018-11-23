package com.eudycontreras.materialsearchbarlibary.modelsMSB

import kotlin.reflect.KMutableProperty0

/**
 * Created by eudycontreras.
 */
interface SearchableData{
    fun getDataId():Int
    fun getDataTarget(): Array<KMutableProperty0<String>>
}
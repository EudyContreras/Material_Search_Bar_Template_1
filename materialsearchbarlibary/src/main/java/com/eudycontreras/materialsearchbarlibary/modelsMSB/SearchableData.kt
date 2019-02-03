package com.eudycontreras.materialsearchbarlibary.modelsMSB

import com.eudycontreras.materialsearchbarlibary.SearchProperty

/**
 * Created by eudycontreras.
 */
interface SearchableData{
    fun getDataId():Int
    fun getDataTarget(): Array<SearchProperty>
}
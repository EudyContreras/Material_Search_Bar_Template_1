package com.eudycontreras.materialsearchbarlibary.listeners

import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchableData

/**
 * Created by eudycontreras.
 */
interface DataAccessInterface {

    fun createDataAccess(): HashMap<String,List<SearchableData>>
}
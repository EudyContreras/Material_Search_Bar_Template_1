package com.eudycontreras.materialsearchbarlibary.modelsMSB

/**
 * Created by eudycontreras.
 */
class SearchEntry(override var key: String, override var value: List<SearchableData>) : MutableMap.MutableEntry<String, List<SearchableData>>{

    override fun setValue(newValue: List<SearchableData>): List<SearchableData> {
        value = newValue
        return value
    }
}

package com.eudycontreras.materialsearchbarlibary.modelsMSB

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
data class SearchResult<DATA>(
    var resultPriority: Int = 0,
    var data: DATA
)
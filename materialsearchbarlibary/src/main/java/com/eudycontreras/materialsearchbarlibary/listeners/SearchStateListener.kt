package com.eudycontreras.materialsearchbarlibary.listeners

import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchResult

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
interface SearchStateListener {

    fun onSearchBarOpening()
    fun onSearchBarClosing()

    fun onSearchBarOpened()
    fun onSearchBarClosed()

    fun onPerformSearchStarted(prefix: String)
    fun onPerformSearchFinished(results: Map<String,List<SearchResult>>)

}
package com.eudycontreras.materialsearchbarlibary.controllers

import com.eudycontreras.materialsearchbarlibary.listeners.SearchResultListener
import com.eudycontreras.materialsearchbarlibary.models.SearchCriteria
import com.eudycontreras.materialsearchbarlibary.models.SearchResult
import java.util.*

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
class SearchController{
    private val recentResults = ArrayList<SearchResult>(100)

    private var lastSearchCriteria: SearchCriteria? = null


    fun performSearch(input: String, searchCriteria: SearchCriteria, searchResultListener: SearchResultListener) {
        this.lastSearchCriteria = searchCriteria
    }

    fun getRecentResults(): ArrayList<SearchResult> {
        return recentResults
    }

    fun getLastSearchCriteria(): SearchCriteria? {
        return lastSearchCriteria
    }
}
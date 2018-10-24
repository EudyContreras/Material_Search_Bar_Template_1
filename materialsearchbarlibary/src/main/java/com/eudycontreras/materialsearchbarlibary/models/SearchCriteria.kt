package com.eudycontreras.materialsearchbarlibary.models

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
class SearchCriteria {

    private var searchCreators: Boolean = false
    private var searchPosts: Boolean = false

    companion object {
        val DEFAULT = getDefault()

        private fun getDefault(): SearchCriteria {
            val searchCriteria = SearchCriteria()
            searchCriteria.setSearchCreators(true)
            searchCriteria.setSearchPosts(true)
            return searchCriteria
        }
    }

    fun isSearchCreators(): Boolean {
        return searchCreators
    }

    fun setSearchCreators(searchCreators: Boolean) {
        this.searchCreators = searchCreators
    }

    fun isSearchPosts(): Boolean {
        return searchPosts
    }

    fun setSearchPosts(searchPosts: Boolean) {
        this.searchPosts = searchPosts
    }
}
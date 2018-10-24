package com.eudycontreras.materialsearchbarlibary

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.eudycontreras.materialsearchbarlibary.controllers.SearchController
import com.eudycontreras.materialsearchbarlibary.fragments.SearchResultFragment
import com.eudycontreras.materialsearchbarlibary.listeners.BackPressListener
import com.eudycontreras.materialsearchbarlibary.listeners.SearchResultListener
import com.eudycontreras.materialsearchbarlibary.models.SearchCriteria
import com.eudycontreras.materialsearchbarlibary.models.SearchResult

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
class MaterialSearchResultWindow(private  var activity: AppCompatActivity,  private var searchBar: MaterialSearchBar, private var  resultContainerId : Int) {

    private lateinit var searchCriteria: SearchCriteria
    private lateinit var searchController: SearchController
    private lateinit var resultFragment: SearchResultFragment


    init{
        this.initComponents()
        this.registerListeners()
    }

    private fun initComponents() {
        this.searchCriteria = SearchCriteria.DEFAULT
        this.resultFragment = SearchResultFragment()
        this.searchController = SearchController()
        this.resultFragment.setOnBackPressed(object : BackPressListener{
            override fun onBackPressed() {
                searchBar.removeInputFocus()
            }

        })
    }

    fun initViews(view: View) {
        setDefaultValues()
    }

    fun showResultsWindow() {
        val transactionIds = intArrayOf(R.anim.nothing_animation, R.anim.nothing_animation, R.anim.nothing_animation, R.anim.nothing_animation)
        addFragment(activity, resultFragment, resultContainerId, transactionIds)
    }

    fun hideResultsWindow() {
        resultFragment.hideResultContainer {
            android.os.Handler().postDelayed({ removeFragment(activity, resultFragment) },200)
        }
    }

    private fun setDefaultValues() {

    }

    private fun registerListeners() {

    }

    fun showResultsForInput(input: String) {
        searchController.performSearch(input, searchCriteria, object : SearchResultListener {
            override fun onSearchResultObtained(searchResults: List<SearchResult>) {

            }
        })
    }
}
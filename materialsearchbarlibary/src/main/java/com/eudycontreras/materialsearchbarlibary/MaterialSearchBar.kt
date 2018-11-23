package com.eudycontreras.materialsearchbarlibary

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import android.transition.TransitionManager
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.EditText
import android.widget.TextView
import com.eudycontreras.materialsearchbarlibary.fragments.SearchResultFragment
import com.eudycontreras.materialsearchbarlibary.listeners.SearchStateListener
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
class MaterialSearchBar(private var activity: AppCompatActivity, private var parentView: View? = null, private var resultContainerId: Int = -1) {

    private lateinit var inputField: EditText
    private lateinit var searchHintText: TextView

    private lateinit var searchCancel: View
    private lateinit var searchHintIcon: View
    private lateinit var searchIcon: View
    private lateinit var searchClear: View

    private lateinit var searchBarLayout: ConstraintLayout
    private lateinit var searchLayoutParent: ConstraintLayout

    private var resultFragment: SearchResultFragment = SearchResultFragment.newInstance()

    private var backgroundWorker: Worker<Map<String,List<SearchResult>>> = Worker()

    private var searchHintShown = true
    private var showingResults = false

    private var searchEngine: SearchEngine? = null

    private var stateListener: SearchStateListener? = null

    init {
        this.initComponents()
        parentView?.let {
            this.initViews(it)
            this.registerListeners()
        }
    }

    fun onBackPressed(delegate:()->Unit){
        if(!resultFragment.isAnimating){
            if(resultFragment.showingResults){
                removeInputFocus()
            }else{
                delegate()
            }
        }
    }

    private fun initComponents() {

    }

    fun setParentView(parentView: View){
        this.searchLayoutParent = parentView as ConstraintLayout
        this.initViews(parentView)
        this.registerListeners()
    }

    fun setResultContainerId(resultContainerId: Int){
        this.resultContainerId = resultContainerId
    }

    private fun initViews(view: View) {
        this.searchLayoutParent = view as ConstraintLayout
        this.searchBarLayout = view.findViewById(R.id.searchBarLayout)
        this.inputField = view.findViewById(R.id.searchBarInput)
        this.searchHintText = view.findViewById(R.id.searchBarHint)
        this.searchHintIcon = view.findViewById(R.id.searchBarHintIcon)
        this.searchIcon = view.findViewById(R.id.searchBarIcon)
        this.searchCancel = view.findViewById(R.id.searchBarCancel)
        this.searchClear = view.findViewById(R.id.searchBarClear)

        setDefaultValues()
    }

    private fun setDefaultValues() {
        this.searchIcon.alpha = 1f
        this.searchIcon.scaleX = 0f
        this.searchIcon.scaleY = 0f

        this.searchClear.alpha = 1f
        this.searchClear.scaleX = 0f
        this.searchClear.scaleY = 0f
        this.searchClear.isFocusable = false
        this.searchClear.isClickable = false
    }

    private fun registerListeners() {
        this.searchClear.setOnClickListener { _ -> clearSearchBar() }

        this.searchCancel.setOnClickListener { _ ->
            hideSoftInputKeyboard(activity)
            Handler().postDelayed({ inputField.clearFocus() }, 50)
        }

        this.inputField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activateSearchBar()
            } else {
                deactivateSearchBar()
            }
        }

        this.inputField.addTextChangedListener(object : TextWatcher {
            var lastText: String = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val prefix = s.toString()
                if (inputField.text.isEmpty()) {
                    if (!searchHintShown) {
                        showHint()
                    }
                } else {
                    if (searchHintShown) {
                        hideHint()
                    }

                    if(prefix != lastText){
                        performSearch(prefix)
                    }
                }

                lastText = prefix
            }
        })
    }

    private fun removeInputFocus() {
        hideSoftInputKeyboard(activity)
        Handler().postDelayed({ inputField.clearFocus() }, 50)
    }

    private fun showHint() {
        searchHintIcon.animate()
                .alpha(1f)
                .setInterpolator(OvershootInterpolator())
                .setDuration(200)
                .start()

        searchHintText.animate()
                .alpha(1f)
                .setInterpolator(OvershootInterpolator())
                .setDuration(200)
                .start()

        searchClear.visibility = View.GONE
        searchClear.isFocusable = false
        searchClear.isClickable = false
        searchClear.animate()
                .alpha(0f)
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator())
                .start()

        searchHintShown = true
    }

    private fun hideHint() {
        searchHintIcon.animate()
                .alpha(0f)
                .setDuration(150)
                .setInterpolator(OvershootInterpolator())
                .start()

        searchHintText.animate()
                .alpha(0f)
                .setDuration(150)
                .setInterpolator(OvershootInterpolator())
                .start()

        searchClear.visibility = View.VISIBLE
        searchClear.isFocusable = true
        searchClear.isClickable = true
        searchClear.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator())
                .start()


        searchHintShown = false
    }

    private fun clearSearchBar() {
        this.inputField.setText("")
    }

    private fun activateSearchBar() {
        stateListener?.onSearchBarOpening()

        val constraintSet = ConstraintSet()
        constraintSet.clone(searchLayoutParent)
        constraintSet.connect(R.id.searchBarLayout, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, convertDpToPixel(activity, 2f).toInt())
        constraintSet.applyTo(searchLayoutParent)

        val transition = AutoTransition()
        transition.interpolator = OvershootInterpolator()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            transition.addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    super.onTransitionEnd(transition)
                    stateListener?.onSearchBarOpened()
                   //openSearchResults()
                    searchCancel.isClickable = true
                    searchCancel.isFocusable = true
                    searchCancel.visibility = View.VISIBLE
                    searchCancel.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setListener(null)
                            .setDuration(200)
                            .start()
                }
            })
        }
        transition.duration = 150
        TransitionManager.beginDelayedTransition(searchLayoutParent, transition)
        if (!showingResults) {
            openSearchResults()
            showingResults = true
        }

        searchIcon.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .start()
    }

    private fun deactivateSearchBar() {
        stateListener?.onSearchBarClosing()

        val constraintSet = ConstraintSet()
        constraintSet.clone(searchLayoutParent)
        constraintSet.connect(R.id.searchBarLayout, ConstraintSet.START, R.id.guideline_outer, ConstraintSet.END, convertDpToPixel(activity, 9f).toInt())
        constraintSet.applyTo(searchLayoutParent)

        val transition = AutoTransition()
        transition.interpolator = OvershootInterpolator()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            transition.addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    super.onTransitionEnd(transition)
                    stateListener?.onSearchBarClosed()
                    searchCancel.isClickable = false
                    searchCancel.isFocusable = false
                    searchCancel.animate()
                            .scaleX(0f)
                            .scaleY(0f)
                            .alpha(0f)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    searchCancel.visibility = View.INVISIBLE
                                }
                            })
                            .setDuration(200)
                            .start()
                }
            })
        }

        transition.duration = 150
        TransitionManager.beginDelayedTransition(searchLayoutParent, transition)
        if (showingResults) {
            closeSearchResults()
            showingResults = false
        }

        if (inputField.text.isEmpty()) {
            searchIcon.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(200)
                    .start()

        }
    }

    private fun showResultsWindow() {
        val transactionIds = intArrayOf(0,0,0,0)
        addFragment(activity, resultFragment, resultContainerId, transactionIds)
    }

    private fun hideResultsWindow() {
        resultFragment.hideResultContainer {
            android.os.Handler().postDelayed({ removeFragment(activity, resultFragment) },200)
        }
    }

    private fun performSearch(input: String) {
        searchEngine?.run {
            stateListener?.onPerformSearchStarted(input)
            doAsync(backgroundWorker) {performSearch(input)}
        }
    }

    private fun openSearchResults() {
        showResultsWindow()
    }

    private fun closeSearchResults() {
         hideResultsWindow()
    }

    fun setSearchStateListener(listener: SearchStateListener){
        this.stateListener = listener

        if(stateListener != null){
            val postWork:((Map<String,List<SearchResult>>)-> Unit)? = {it-> stateListener?.onPerformSearchFinished(it)}
            backgroundWorker.setPostWork(postWork)
        }
    }

    fun setSearchStateListener(listener: (Map<String,List<SearchResult>>) -> Unit){
        setSearchStateListener(object : SearchStateListener{
            override fun onSearchBarOpening() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSearchBarClosing() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSearchBarOpened() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSearchBarClosed() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPerformSearchStarted(prefix: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPerformSearchFinished(results: Map<String, List<SearchResult>>) {
                listener.invoke(results)
            }})
    }

    fun setSearchEngine(engine: SearchEngine?){
        this.searchEngine = engine
    }
}
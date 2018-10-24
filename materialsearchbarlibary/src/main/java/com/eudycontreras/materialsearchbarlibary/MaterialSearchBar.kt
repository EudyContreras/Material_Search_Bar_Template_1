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

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
class MaterialSearchBar(private var activity: AppCompatActivity, var parentView: View, private var resultContainerId: Int) {

    private lateinit var inputField: EditText
    private lateinit var searchHintText: TextView

    private lateinit var searchCancel: View
    private lateinit var searchHintIcon: View
    private lateinit var searchIcon: View
    private lateinit var searchClear: View

    private lateinit var searchBarLayout: ConstraintLayout
    private lateinit var searchLayoutParent: ConstraintLayout
    private lateinit var resultWindowComponent: MaterialSearchResultWindow
    
    private var onSearchActive: Action? = null
    private var onSearchInactive: Action? = null

    private var searchHintShown = true
    private var showingResults = false
    private var searchBarActive = false

    constructor(activity: AppCompatActivity, parentView: View): this(activity, parentView, -1)

    init {
        this.initComponents()
        this.initViews(parentView)
        this.registerListeners()
    }

    private fun initComponents() {
        this.resultWindowComponent = MaterialSearchResultWindow(activity, this, resultContainerId)
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
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (inputField.text.isEmpty()) {
                    if (!searchHintShown) {
                        showHint()
                    }
                } else {
                    if (searchHintShown) {
                        hideHint()
                    }

                    performSearch(inputField.text.toString())
                }
            }
        })
    }

    fun removeInputFocus() {
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
                    if (onSearchActive != null) {
                        onSearchActive?.invoke()
                    }
                    openSearchResults()
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
                    if (onSearchInactive != null) {
                        onSearchInactive?.invoke()
                    }

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

    private fun performSearch(input: String) {
        resultWindowComponent.showResultsForInput(input)
    }

    private fun openSearchResults() {
        resultWindowComponent.showResultsWindow()
    }

    private fun closeSearchResults() {
        resultWindowComponent.hideResultsWindow()
    }

    fun setOnSearchActive(action: Action?) {
        this.onSearchActive = action
    }

    fun setOnSearchInactive(action: Action?) {
        this.onSearchInactive = action
    }
}
package com.eudycontreras.materialsearchbarlibary.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.eudycontreras.materialsearchbarlibary.*
import com.eudycontreras.materialsearchbarlibary.customViews.CustomCoordinatorLayout
import com.eudycontreras.materialsearchbarlibary.listeners.BackPressListener
import com.eudycontreras.materialsearchbarlibary.listeners.TouchEventListener

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
class SearchResultFragment : Fragment() {

    private lateinit var searchResultContainer: CustomCoordinatorLayout
    private lateinit var searchResultRecycler: RecyclerView
    private lateinit var searchResultContainerBackground: View

    private lateinit var activity: AppCompatActivity
    
    private var backPressedListener: BackPressListener? = null

    private var height : Float = 0f
    private var width : Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as AppCompatActivity
    }

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)
        initViews(view)
        registerListeners()
        setDefaultValues()
        return view
    }

    override fun onResume() {
        super.onResume()
        showResultContainer(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //this.activity.removeBackPressedEvent(backPressedEvent)
    }

    private fun initViews(view: View) {
        this.searchResultContainer = view.findViewById(R.id.fragment_search_result_container)
        this.searchResultRecycler = searchResultContainer.findViewById(R.id.fragment_search_result_recycler)
        this.searchResultContainerBackground = view.findViewById(R.id.fragment_search_result_container_background)
    }

    private fun registerListeners() {
        this.searchResultContainer.setTouchEventListener(object : TouchEventListener{
            override fun onTouchEvent(event: MotionEvent) {
                hideSoftInputKeyboard(activity)
            }
        })

       // this.activity.addBackPressedEvent(backPressedEvent)
    }

    private fun setDefaultValues() {
        searchResultContainer.alpha = 0f
        searchResultRecycler.adapter = SearchResultAdapter(100)
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels.toFloat()
        height = displayMetrics.heightPixels.toFloat()
    }

    private val backPressedEvent = {
        if (backPressedListener != null) {
            backPressedListener?.onBackPressed()
        }
    }

    fun showResultContainer(onFinished: Action?) {

        val start = -(convertDpToPixel(activity, height) - convertDpToPixel(activity, 56f))
        var friction = 16f
        var tension = 110f
        val distance = Math.abs(start)
        val duration = 350

        searchResultRecycler.scrollToPosition(0)
        searchResultContainerBackground.pivotY = 0f
        searchResultContainerBackground.scaleY = 0f
        searchResultContainerBackground.visibility = View.VISIBLE

        createSpringAnimation(0f, 1f, duration, distance, friction, tension,
                { value ->
                    searchResultContainerBackground.animate()
                            .scaleY(value.toFloat())
                            .alpha(1f)
                            .setDuration(0)
                            .start()
                }, onFinished)

        searchResultContainer.alpha = 0f
        searchResultContainer.pivotY = 0f
        searchResultContainer.translationY = start
        searchResultContainer.visibility = View.VISIBLE

        friction = 16f
        tension = 50f

        createSpringAnimation(start, 0f, duration, distance, friction, tension,
                { value ->

                    val alpha = map(value, start, 0f, 0f, 1f)
                    searchResultContainer.animate()
                            .translationY(value)
                            .alpha(alpha)
                            .setDuration(0)
                            .setInterpolator(null)
                            .start()
                }, onFinished)

    }

    fun hideResultContainer(onFinished: Action?) {
        val start = -(convertDpToPixel(activity, height) - convertDpToPixel(activity, 56f))

        searchResultContainerBackground.pivotY = 0f
        searchResultContainerBackground.scaleY = 1f
        searchResultContainerBackground.visibility = View.VISIBLE

        searchResultContainerBackground.animate()
                .scaleY(0f)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        onFinished?.invoke()
                    }
                })
                .start()

        searchResultContainer.alpha = 1f
        searchResultContainer.pivotY = 0f
        searchResultContainer.translationY = 0f
        searchResultContainer.visibility = View.VISIBLE

        searchResultContainer.animate()
                .translationY(start)
                .alpha(0f)
                .setDuration(300)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
    }

    fun setOnBackPressed(listener: BackPressListener) {
        this.backPressedListener = listener
    }

    private class SearchResultAdapter(private var itemCount: Int): RecyclerView.Adapter<SearchResultFragment.SearchResultVH>() {

        override fun getItemCount(): Int {
           return itemCount
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SearchResultFragment.SearchResultVH {
            val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_search_result, viewGroup, false)
            return SearchResultFragment.SearchResultVH(itemView)
        }

        override fun onBindViewHolder(fakePageVH: SearchResultFragment.SearchResultVH, i: Int) {

        }
    }

    private class SearchResultVH internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}
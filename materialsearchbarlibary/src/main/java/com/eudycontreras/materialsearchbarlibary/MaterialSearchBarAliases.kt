package com.eudycontreras.materialsearchbarlibary

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringSystem

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
internal typealias Action = ()-> Unit

internal typealias SpringValue = (Float) -> Unit

typealias ResultPredicate<T> = (T) -> Boolean

internal fun String.containsWord(word: String): Boolean {
    val index = this.indexOf(word)

    if(index != -1){
        val lastIndex = (index + word.length)+1

        if(index != 0){
            if(this[index-1]== ' ' && this[if (lastIndex != word.length) lastIndex else word.length-1 ] == ' '){
                return true
            }
        }else{
            if(this[if (lastIndex != word.length) lastIndex else word.length-1 ] == ' '){
                return true
            }
        }
    }
    return false
}

internal fun map(
        value: Float,
        fromLow: Float,
        fromHigh: Float,
        toLow: Float,
        toHigh: Float): Float {
    val fromRangeSize = fromHigh - fromLow
    val toRangeSize = toHigh - toLow
    val valueScale = (value - fromLow) / fromRangeSize
    return toLow + valueScale * toRangeSize
}

internal fun hideSoftInputKeyboard(activity: AppCompatActivity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm != null) {
            if (imm.isActive)
                imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

internal fun convertDpToPixel(context: Context, dp: Float): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

internal fun addFragment(activity: AppCompatActivity, fragment: Fragment, containerId: Int, transactionIds: IntArray = intArrayOf(R.anim.nothing_animation,R.anim.nothing_animation)) {
    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

    fragmentTransaction.setCustomAnimations(
            transactionIds[0],
            transactionIds[1]
    )

    fragmentTransaction.add(containerId, fragment, Fragment::class.java.simpleName)
    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commitAllowingStateLoss()
}


internal fun removeFragment(activity: AppCompatActivity, fragment: Fragment, transactionIds: IntArray = intArrayOf(R.anim.nothing_animation,R.anim.nothing_animation)) {

    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

    fragmentTransaction.setCustomAnimations(
            transactionIds[0],
            transactionIds[1]
    )

    fragmentTransaction.remove(fragment)
    fragmentTransaction.commitAllowingStateLoss()
}

private val springSystem: SpringSystem by lazy {
    SpringSystem.create()
}

internal fun createSpringAnimation(start: Float, end: Float, time: Int , distance: Float, friction: Float, tension: Float, springValue: SpringValue, onEnd: Action?) {
    val spring = springSystem.createSpring()

    spring.addListener(object : SimpleSpringListener() {

        override fun onSpringUpdate(spring: Spring?) {
            val mappedValue = map(spring!!.currentValue.toFloat(), 0.0f, 1.0f, start, end)

            springValue(mappedValue)

            if (spring.isAtRest) {
                spring.destroy()

                onEnd?.invoke()
            }
        }
    })

    val velocity = 12f

    spring.apply {
        this.endValue = 1.0
        this.velocity = velocity.toDouble()
        this.springConfig = SpringConfig(tension.toDouble(), friction.toDouble())
    }
}

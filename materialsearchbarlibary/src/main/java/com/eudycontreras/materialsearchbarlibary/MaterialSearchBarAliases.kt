package com.eudycontreras.materialsearchbarlibary

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
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

internal fun addFragment(activity: AppCompatActivity, fragment: Fragment, containerId: Int, transactionIds: IntArray = intArrayOf(R.anim.nothing_animation,R.anim.nothing_animation,R.anim.nothing_animation,R.anim.nothing_animation)) {
    if (activity.supportFragmentManager.fragments.contains(fragment)) {
        return
    }

    val container : View  = activity.findViewById(containerId)
    container.visibility = View.VISIBLE

    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

    fragmentTransaction.setCustomAnimations(
            transactionIds[0],
            transactionIds[1],
            transactionIds[2],
            transactionIds[3]
    )

    fragmentTransaction.add(containerId, fragment, Fragment::class.java.simpleName)
    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
}


internal fun removeFragment(activity: AppCompatActivity, fragment: Fragment, transactionIds: IntArray = intArrayOf(R.anim.nothing_animation,R.anim.nothing_animation,R.anim.nothing_animation,R.anim.nothing_animation)) {

    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

    fragmentTransaction.setCustomAnimations(
            transactionIds[0],
            transactionIds[1],
            transactionIds[2],
            transactionIds[3]
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

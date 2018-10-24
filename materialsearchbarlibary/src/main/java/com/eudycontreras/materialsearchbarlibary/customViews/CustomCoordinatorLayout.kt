package com.eudycontreras.materialsearchbarlibary.customViews

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
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
class CustomCoordinatorLayout: CoordinatorLayout{

    private var touchEventListener: TouchEventListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context,  attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        touchEventListener?.onTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    fun setTouchEventListener(onTouchEventListener: TouchEventListener) {
        this.touchEventListener = onTouchEventListener
    }
}
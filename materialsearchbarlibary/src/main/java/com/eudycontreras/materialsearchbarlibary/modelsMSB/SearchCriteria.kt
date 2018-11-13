package com.eudycontreras.materialsearchbarlibary.modelsMSB

import com.eudycontreras.materialsearchbarlibary.ResultPredicate
import kotlin.reflect.KMutableProperty1

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
class SearchCriteria<T>(private var rules: ArrayList<ResultPredicate<T>> = ArrayList()){

    private val propertyTargets: ArrayList<KMutableProperty1<T, String>> = ArrayList()

    fun addSearchTarget(vararg property: KMutableProperty1<T, String>) {
       this.propertyTargets.addAll(property)
    }

    fun addRule(vararg predicate: ResultPredicate<T>) {
        rules.addAll(predicate)
    }

    fun removeRule(vararg predicate: ResultPredicate<T>) {
        rules.removeAll(rules)
    }

    fun getPropertyTargets(): ArrayList<KMutableProperty1<T, String>> {
        return propertyTargets
    }

    internal fun getRules(): ArrayList<ResultPredicate<T>> {
       return rules
    }

    fun setRules(rules: ArrayList<ResultPredicate<T>>) {
       this.rules = rules
    }
}
package com.eudycontreras.materialsearchbarlibary

import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchResult
import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchableData

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
abstract class SearchEngine{

        protected var data: HashMap<String,List<SearchableData>> = HashMap()

        protected var rules: HashMap<String,ArrayList<ResultPredicate>> = HashMap()

        fun addDataAccessors(vararg accessors: ()->Pair<String,List<SearchableData>>){
                for(accessor in accessors){
                        val data = accessor()
                        this.data[data.first] = data.second
                }
        }

        fun addSearchRule(targetDataKey: String, vararg predicate: ResultPredicate) {
                if(data.containsKey(targetDataKey)){
                        if(rules.containsKey(targetDataKey)){
                                rules[targetDataKey]?.addAll(predicate)
                        }else{
                                rules[targetDataKey] = ArrayList(predicate.toList())
                        }
                }else{
                        throw Exception("Rules must belong to a set of data!")
                }
        }

        fun removeRule(targetDataKey: String, vararg predicates: ResultPredicate) {
                if(data.containsKey(targetDataKey)) {
                        if (rules.containsKey(targetDataKey)) {
                                for (predicate in predicates) {
                                        rules[targetDataKey]?.remove(predicate)
                                }
                        }
                }else{
                        throw Exception("Rules must belong to a set of data!")
                }
        }

        abstract fun performSearch(input: String) : Map<String,List<SearchResult>>
}
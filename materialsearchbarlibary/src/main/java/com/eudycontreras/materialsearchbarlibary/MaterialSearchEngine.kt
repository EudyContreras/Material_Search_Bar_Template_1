package com.eudycontreras.materialsearchbarlibary

import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchResult
import kotlin.reflect.KMutableProperty0

/**
 * Unlicensed private property of the author and creator.
 * Unauthorized use of this class/file outside of the Material Search Bar Template 1 project
 * may result on legal prosecution. Do not alter or remove this text.
 * Created by Eudy Contreras
 *
 * @author  Eudy Contreras
 * @version 1.0
 */
class MaterialSearchEngine(var method: SearchMethod = SearchMethod.STARTS_WITH): SearchEngine(){

    override fun performSearch(input: String) : Map<String,List<SearchResult>>{
        val results = HashMap<String,ArrayList<SearchResult>>()

        for(data in data){
            val key = data.key
            val values = data.value

            rules[key]?.let {
                for(value in values){
                    if(it.all { rule-> rule(value) }){
                        if(value.getDataTarget().any {target-> matchType(target,input,method)}){
                            if(!results.containsKey(key)){
                                val group = ArrayList<SearchResult>()
                                group.add(SearchResult(data = value))
                                results[key] = group
                            }else{
                                results[key]?.add(SearchResult(data = value))
                            }
                        }
                    }
                }
            }
        }
        return results
    }

    private fun matchType(target: KMutableProperty0<String>, input: String, method: SearchMethod): Boolean{
        return when(method){
            SearchMethod.STARTS_WITH -> target.get().startsWith(input)
            SearchMethod.HAS_WORD -> target.get().containsWord(input)
            SearchMethod.CONTAINS -> target.get().contains(input)
        }
    }
}
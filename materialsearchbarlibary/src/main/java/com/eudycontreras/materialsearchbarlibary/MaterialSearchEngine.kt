package com.eudycontreras.materialsearchbarlibary

import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchCriteria
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
class MaterialSearchEngine<T>(
        internal var targetData: List<T> = ArrayList(),
        internal var criteria: SearchCriteria<T> = SearchCriteria(),
        var method: SearchMethod = SearchMethod.STARTS_WITH):
        SearchEngine<T>(){

    override fun performSearch(listener: (List<SearchResult<T>>) -> Unit): (String) -> Unit {
        return {
            val results = ArrayList<SearchResult<T>>()

            for(data in targetData){
                if(criteria.getRules().all { p-> p(data) }){
                    if(criteria.getPropertyTargets().any {target->
                                when(method){
                                    SearchMethod.STARTS_WITH -> target.get(data).startsWith(it)
                                    SearchMethod.HAS_WORD -> target.get(data).containsWord(it)
                                    SearchMethod.CONTAINS -> target.get(data).contains(it)
                                }
                            }){
                        results.add(SearchResult(data = data))
                    }
                }
            }
            listener.invoke(results)
        }
    }

}
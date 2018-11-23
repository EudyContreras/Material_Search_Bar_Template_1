package com.eudycontreras.materialsearchbartemplate1

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.eudycontreras.materialsearchbarlibary.MaterialSearchBar
import com.eudycontreras.materialsearchbarlibary.MaterialSearchEngine
import com.eudycontreras.materialsearchbarlibary.SearchMethod
import com.eudycontreras.materialsearchbarlibary.listeners.SearchStateListener
import com.eudycontreras.materialsearchbarlibary.modelsMSB.SearchResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.component_toolbar_home.*

class MainActivity : AppCompatActivity(){

    private lateinit var searchBar: MaterialSearchBar

    private val names = arrayOf("Noah Aguilar","Eleanor Donaldson","Shyann Holt","Josiah Payne","Douglas Gardner","Isaiah Maxwell","Liam Hahn","Greyson Kelly","Kaylen Welch","Riya Flynn","Haleigh Newton","Alisa Roman","Sophie Cisneros","Emilio Frederick","Celeste Rivas","Dylan Clark","Angela Brooks","Cooper Combs","Shania Marquez","Gillian Medina","Laci Krueger","Jean English","Cannon Foster","Bruno King","Aydan Nunez","Saniya Tucker","Alexander Rasmussen","Libby David","Sebastian Lyons","Chanel Nelson","Finley Huang","Layton Butler","Tyree Pollard","Jaylon Goodwin","Hanna Campbell","Tatum Cantu","Sydnee Faulkner","Jazmine Taylor","Kaiden Guerra","Alannah Banks","Elsie Willis","Priscilla Page","Shane Peters","Will Leon","Eugene Beck","Genesis Parsons","Sanai Ochoa","Estrella Andrade","Jakayla Hodges","Messiah Hardin","Thomas Decker","Shelby Dodson","Emiliano Chan","Violet Arellano","Brandon Morton","Skyla Hays","Jeramiah Hensley","Darien Fuentes","Monserrat Chen","Courtney Hooper","Susan Wilkinson","Mckenna Olson","Adolfo Schmitt","Haven Nielsen","Macy Clayton","Kenna Ball","Lyric Madden","Sarah Patel","Camryn Bowman","Brady Dean","Taryn Gordon","Kelton Aguirre","Maxwell Mcconnell","Gloria Vaughn","Cailyn Ramirez","Landon Schaefer","Kenley Henderson","Azul Schroeder","Annie Hansen","Saniyah Lambert","Rudy Skinner","Araceli Galvan","Abel Stafford","Zander Gay","Chad Ayala","Aryan Jacobson","Harold Liu","Ralph Randall","Weston Herman","Leland Marks","Khalil Maldonado","Malachi Durham","Hudson Perkins","Adrianna Silva","Zaniyah Osborne","Ernesto Johnson","Alfred Schmidt","Clarence Church","Briley Wong","Randy Webster")
    private val titles = arrayOf("Noah Aguilar","Eleanor Donaldson","Shyann Holt","Josiah Payne","Douglas Gardner","Isaiah Maxwell","Liam Hahn","Greyson Kelly","Kaylen Welch","Riya Flynn","Haleigh Newton","Alisa Roman","Sophie Cisneros","Emilio Frederick","Celeste Rivas","Dylan Clark","Angela Brooks","Cooper Combs","Shania Marquez","Gillian Medina","Laci Krueger","Jean English","Cannon Foster","Bruno King","Aydan Nunez","Saniya Tucker","Alexander Rasmussen","Libby David","Sebastian Lyons","Chanel Nelson","Finley Huang","Layton Butler","Tyree Pollard","Jaylon Goodwin","Hanna Campbell","Tatum Cantu","Sydnee Faulkner","Jazmine Taylor","Kaiden Guerra","Alannah Banks","Elsie Willis","Priscilla Page","Shane Peters","Will Leon","Eugene Beck","Genesis Parsons","Sanai Ochoa","Estrella Andrade","Jakayla Hodges","Messiah Hardin","Thomas Decker","Shelby Dodson","Emiliano Chan","Violet Arellano","Brandon Morton","Skyla Hays","Jeramiah Hensley","Darien Fuentes","Monserrat Chen","Courtney Hooper","Susan Wilkinson","Mckenna Olson","Adolfo Schmitt","Haven Nielsen","Macy Clayton","Kenna Ball","Lyric Madden","Sarah Patel","Camryn Bowman","Brady Dean","Taryn Gordon","Kelton Aguirre","Maxwell Mcconnell","Gloria Vaughn","Cailyn Ramirez","Landon Schaefer","Kenley Henderson","Azul Schroeder","Annie Hansen","Saniyah Lambert","Rudy Skinner","Araceli Galvan","Abel Stafford","Zander Gay","Chad Ayala","Aryan Jacobson","Harold Liu","Ralph Randall","Weston Herman","Leland Marks","Khalil Maldonado","Malachi Durham","Hudson Perkins","Adrianna Silva","Zaniyah Osborne","Ernesto Johnson","Alfred Schmidt","Clarence Church","Briley Wong","Randy Webster")

    private var dataA: ArrayList<DataA> = ArrayList()
    private var dataB: ArrayList<DataB> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataA.shuffle()

        val dataAKey = "DataA"
        val dataBKey = "DataB"

        dataA.addAll(names.mapIndexed { index, name ->  DataA(id = index, name = name)})
        dataB.addAll(titles.mapIndexed { index, title ->  DataB(id = index, title = title)})

        val accessorA = {Pair(dataAKey, dataA)}
        val accessorB = {Pair(dataBKey, dataB)}

        val engine = MaterialSearchEngine(SearchMethod.STARTS_WITH).apply {
            addDataAccessors(accessorA, accessorB)

            addSearchRule(dataAKey,
                    { p-> p.getDataId() in 0..100 },
                    { p -> p.getDataTarget().all { d-> d.get().isNotEmpty() }})

            addSearchRule(dataBKey,
                    { p-> p.getDataId() in 0..100 },
                    { p -> p.getDataTarget().all { d-> d.get().isNotEmpty() }})
        }


        searchBar = MaterialSearchBar(this)
        searchBar.setParentView(tool_bar.findViewById(R.id.toolbar_search_component))
        searchBar.setResultContainerId(R.id.fragment_outer_container)
        searchBar.setSearchEngine(engine)
        searchBar.setSearchStateListener(object : SearchStateListener{
            override fun onSearchBarOpening() {
                Log.d("Search Bar","OPENING")
            }

            override fun onSearchBarClosing() {
                Log.d("Search Bar","CLOSING")
            }

            override fun onSearchBarOpened() {
                Log.d("Search Bar","OPENED")
                toolbar_settings_icon_wrapper.animate()
                        .scaleY(0f)
                        .scaleX(0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                toolbar_settings_icon_wrapper.visibility = View.INVISIBLE
                            }
                        })
                        .setDuration(200)
                        .start()

                toolbar_profile_icon_wrapper.animate()
                        .translationX(-convertDpToPixel(this@MainActivity, 50f))
                        .alpha(0f)
                        .setInterpolator(DecelerateInterpolator())
                        .setDuration(50)
                        .start()
            }

            override fun onSearchBarClosed() {
                Log.d("Search Bar","CLOSED")
                toolbar_settings_icon_wrapper.visibility = View.VISIBLE
                toolbar_settings_icon_wrapper.animate()
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(100)
                        .setListener(null)
                        .start()

                toolbar_profile_icon_wrapper.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setDuration(100)
                        .start()
            }

            override fun onPerformSearchStarted(prefix: String) {
                Log.d("Search Bar","SEARCH STARTED")
            }

            override fun onPerformSearchFinished(results: Map<String,List<SearchResult>>) {
                Log.d("Search Bar","SEARCH ENDED")
                for(result in results){
                    val key = result.key
                    val values = result.value

                    for(value in values){
                        val data = value.data
                        Log.d("SEARCH RESULT for $key: ", data.toString())
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        searchBar.onBackPressed{super.onBackPressed()}
    }

    private fun convertDpToPixel(context: Context, dp: Float): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics

        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

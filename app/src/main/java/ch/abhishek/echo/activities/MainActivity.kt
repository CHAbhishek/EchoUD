package ch.abhishek.echo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.abhishek.echo.R
import ch.abhishek.echo.adapters.NavigationDrawerAdapter
import ch.abhishek.echo.fragments.MainScreenFragment

class MainActivity : AppCompatActivity() {

    var navigationDrawerIconsList:ArrayList<String> = arrayListOf()
    var images_for_navdrawer= intArrayOf(R.drawable.navigation_allsongs,R.drawable.navigation_favorites,
            R.drawable.navigation_settings,R.drawable.navigation_aboutus)
     object Statified{
         var drawerLayout: DrawerLayout?=null
     }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val toolbar = findViewById<Toolbar>(R.id.toolbar)
       setSupportActionBar(toolbar)

        MainActivity.Statified.drawerLayout =findViewById(R.id.drawer_layout)

        navigationDrawerIconsList.add("All Songs")
        navigationDrawerIconsList.add("Favourites")
        navigationDrawerIconsList.add("Settings")
        navigationDrawerIconsList.add("About Us")

        val toggle = ActionBarDrawerToggle(this@MainActivity,
                MainActivity.Statified.drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
         MainActivity.Statified.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        val mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment,mainScreenFragment,"MainScreenFragment")
                .commit()
        val _navigationAdpter=NavigationDrawerAdapter(navigationDrawerIconsList,images_for_navdrawer,this)
        _navigationAdpter.notifyDataSetChanged()

        val navigation_recycler_view =findViewById<RecyclerView>(R.id.navigation_recycler_view)
        navigation_recycler_view.layoutManager = LinearLayoutManager(this)
        navigation_recycler_view.itemAnimator = DefaultItemAnimator()
        navigation_recycler_view.adapter=_navigationAdpter
        navigation_recycler_view.setHasFixedSize(true)
    }

}




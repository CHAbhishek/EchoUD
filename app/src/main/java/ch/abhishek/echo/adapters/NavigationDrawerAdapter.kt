package ch.abhishek.echo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.abhishek.echo.R
import ch.abhishek.echo.activities.MainActivity
import ch.abhishek.echo.fragments.AboutUsFragment
import ch.abhishek.echo.fragments.FavouriteFragment
import ch.abhishek.echo.fragments.MainScreenFragment
import ch.abhishek.echo.fragments.SettingsFragment

class NavigationDrawerAdapter(_contentList: ArrayList<String>, _getImages: IntArray,
                              _context: Context)
    : RecyclerView.Adapter<NavigationDrawerAdapter.NavViewHolder>() {
    var contentList: ArrayList<String>? = null
    var getImages: IntArray? = null
    var mContext: Context? = null
    init {
        this.contentList = _contentList
        this.getImages = _getImages
        this.mContext = _context
    }
    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        holder.icon_GET?.setBackgroundResource(getImages?.get(position) as Int)
        holder.text_GET?.setText(contentList?.get(position))
        holder.contentHolder?.setOnClickListener {
            when (position) {
                0 -> {
                    val mainScreenFragment = MainScreenFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment, mainScreenFragment)
                            .commit()
                }
                1 -> {
                    val favouriteFragment = FavouriteFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment, favouriteFragment)
                            .commit()
                }
                2 -> {
                    val settingsFragment = SettingsFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment, settingsFragment)
                            .commit()
                }
                3 -> {
                    val aboutUsFragment = AboutUsFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment, aboutUsFragment)
                            .commit()
                }
            }
            MainActivity.Statified.drawerLayout?.closeDrawers()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_custom_navigationdrawer, parent, false)
        val returnThis = NavViewHolder(itemView)
        return returnThis
    }
    override fun getItemCount(): Int {
        return (contentList as ArrayList).size
    }
    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var icon_GET: ImageView? = null
        var text_GET: TextView? = null
        var contentHolder: RelativeLayout? = null
        init {
            icon_GET = itemView?.findViewById(R.id.icon_navdrawer)
            text_GET = itemView?.findViewById(R.id.text_navdrawer)
            contentHolder = itemView?.findViewById(R.id.navdrawer_item_content_holder)
        }
    }
}

package com.example.boris_perdija_stress_meter

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

// Navigation view logic was adopted from "Navigation Drawer Menu in Android Studio using Kotlin"
// https://www.youtube.com/watch?v=sSL6a_ivRVk

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null)
        {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StressMeterFragment()).commit()
            navigationView.setCheckedItem(R.id.stressMeter)
        }
    }

    // Navigate to the appropriate fragments based on the item chosen in the navigation pane
    override fun onNavigationItemSelected(item:MenuItem):Boolean{
        when(item.itemId){
            R.id.stressMeter -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StressMeterFragment()).commit()
            R.id.results -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ResultsFragment()).commit()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
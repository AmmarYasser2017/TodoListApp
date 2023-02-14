package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigation: BottomNavigationView
    lateinit var addButton: FloatingActionButton
    val todoListFragment = TodoListFragment()
    val settingsFragment = HistoryFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.navigation_view)
        addButton = findViewById(R.id.add)
        addButton.setOnClickListener {
            showAddBottomSheet()
        }
        bottomNavigation.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener {
                if (it.itemId == R.id.navigation_list) {
                    pushFragment(todoListFragment)
                } else if (it.itemId == R.id.navigation_settings) {
                    pushFragment(settingsFragment)
                }
                return@OnItemSelectedListener true
            }
        )
        bottomNavigation.selectedItemId = R.id.navigation_list

    }

    private fun showAddBottomSheet() {
        val addBottomSheet = AddTodoBottomSheet()
        addBottomSheet.show(supportFragmentManager, "")
        addBottomSheet.onTodoAddedListener = object : AddTodoBottomSheet.OnTodoAddedListener {
            override fun onTodoAdded() {
                todoListFragment.getTodosListFromDB()
            }
        }
    }

    fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }
}
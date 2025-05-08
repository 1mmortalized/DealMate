package com.bizsolutions.dealmate

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bizsolutions.dealmate.databinding.ActivityMainBinding
import com.bizsolutions.dealmate.ui.ToolbarMenuHandler
import com.bizsolutions.dealmate.ui.home.HomeViewModel
import com.chaquo.python.Python
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.utilities.Score.score
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.ArrayList
import kotlin.getValue


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.toolbar.updateLayoutParams<MarginLayoutParams> {
                topMargin = insets.top
            }
            binding.activityMainBottomNavBar.updatePadding(bottom = insets.bottom)

            view.updateLayoutParams<MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
            }

            WindowInsetsCompat.CONSUMED
        }

        val navView: BottomNavigationView = binding.activityMainBottomNavBar

        val navController = findNavController(R.id.activity_main_nav_host_fragment).apply {
            val navGraph = navInflater.inflate(R.navigation.mobile_navigation)
            graph = navGraph

            addOnDestinationChangedListener { _, destination, _ ->
                binding.homeAppTitleLayout.isVisible = destination.id == R.id.navigation_home

                if (destination.id !in listOf(
                    R.id.navigation_task
                )) {
                    binding.toolbar.menu.clear()
                } else {
                    binding.toolbar.menu.clear()
                    binding.toolbar.inflateMenu(R.menu.item_menu)
                }
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_contacts, R.id.navigation_dashboard, R.id.navigation_deals
            )
        )

        binding.toolbar.setOnMenuItemClickListener { item ->
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment)
            val currentFragment = navHostFragment
                ?.childFragmentManager
                ?.fragments
                ?.firstOrNull()

            (currentFragment as? ToolbarMenuHandler)?.onToolbarMenuItemClicked(item)
            return@setOnMenuItemClickListener true
        }

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val py = Python.getInstance()
        val keywordsModule = py.getModule("keywords")
        val extractKeywordsFunction = keywordsModule["extract_keywords"]

        lifecycleScope.launch {
            val today = LocalDate.now()
            val uncompletedTasks = viewModel.getOverdueUncompletedTasks(today)

            uncompletedTasks.forEach { task ->
//                val updatedTask = task.copy(postponed = true)
//                taskDao.update(updatedTask)
                val keywordsRaw = extractKeywordsFunction?.call(task.title)
                val keywords = keywordsRaw?.asList()?.map { it.toJava(String::class.java) }
            }
        }
    }
}
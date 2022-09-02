package com.codingwithjks.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.codingwithjks.weatherapp.Repository.WeatherRepository
import com.codingwithjks.weatherapp.ViewModel.WeatherViewModel
import com.codingwithjks.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initListener()
        lifecycleScope.launchWhenStarted {
            weatherViewModel.weatherResponse.collect { response ->

                when (response.weather[0].description) {
                    "clear sky", "mist" -> {
                        Glide.with(this@MainActivity)
                            .load(R.drawable.clouds)
                            .into(binding.image)
                    }
                    "haze", "overcast clouds" -> {
                        Glide.with(this@MainActivity)
                            .load(R.drawable.haze)
                            .into(binding.image)
                    }
                    else -> {
                        Glide.with(this@MainActivity)
                            .load(R.drawable.rain)
                            .into(binding.image)
                    }
                }

                binding.description.text = response.weather[0].description
                binding.name.text = response.name
                binding.degree.text = response.wind.deg.toString()
                binding.speed.text = response.wind.speed.toString()
                binding.temp.text =
                    (((response.main.temp - 273) * 100.0).roundToInt() / 100.0).toString()
                binding.humidity.text = response.main.humidity.toString()

            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun initListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { weatherViewModel.setSearchQuery(it) }
                Log.d("main", "onQueryTextChange: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

        })
    }
}

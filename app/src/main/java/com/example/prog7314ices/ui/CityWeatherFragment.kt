package com.example.prog7314ices.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.prog7314ices.R
import com.example.prog7314ices.network.CitySearchResultDto
import com.example.prog7314ices.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityWeatherFragment : Fragment() {

    private val sharedLocationVM: SharedLocationViewModel by activityViewModels()

    private val apiKey = "zpka_c6f2c81a5688443aad64355246cf2029_a1526508"

    private lateinit var etCity: EditText
    private lateinit var btnSearch: Button
    private lateinit var progress: ProgressBar
    private lateinit var tvStatus: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_city_weather, container, false)
        etCity = v.findViewById(R.id.etCity)
        btnSearch = v.findViewById(R.id.btnSearch)
        progress = v.findViewById(R.id.progress)
        tvStatus = v.findViewById(R.id.tvStatus)

        btnSearch.setOnClickListener {
            val query = etCity.text.toString().trim()
            if (query.isEmpty()) {
                tvStatus.text = "Please enter a city name."
                return@setOnClickListener
            }
            searchCity(query)
        }

        return v
    }

    private fun searchCity(query: String) {
        showLoading(true, "Searching for \"$query\"...")

        RetrofitClient.api.searchCity(apiKey, query)
            .enqueue(object : Callback<List<CitySearchResultDto>> {
                override fun onResponse(
                    call: Call<List<CitySearchResultDto>>,
                    response: Response<List<CitySearchResultDto>>
                ) {
                    val results = response.body()
                    if (!response.isSuccessful || results.isNullOrEmpty()) {
                        showLoading(false, "No results found for \"$query\".")
                        return
                    }

                    // Take the first match (simplest flow for now)
                    val first = results.first()
                    val key = first.Key
                    if (key.isNullOrBlank()) {
                        showLoading(false, "No valid location key returned.")
                        return
                    }

                    // Publish to shared VM so TODAY and 5-DAY reload automatically
                    sharedLocationVM.updateLocationKey(key)

                    val labelCity = first.LocalizedName ?: "Unknown"
                    val labelCountry = first.Country?.LocalizedName ?: ""
                    val label = if (labelCountry.isBlank()) labelCity else "$labelCity, $labelCountry"

                    showLoading(false, "Showing weather for: $label")
                }

                override fun onFailure(call: Call<List<CitySearchResultDto>>, t: Throwable) {
                    showLoading(false, "Search failed: ${t.message}")
                }
            })
    }

    private fun showLoading(loading: Boolean, msg: String) {
        progress.visibility = if (loading) View.VISIBLE else View.GONE
        tvStatus.text = msg
    }
}

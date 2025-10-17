package com.example.prog7314ices.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.prog7314ices.R
import com.example.prog7314ices.network.CurrentConditionsDto
import com.example.prog7314ices.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentWeatherFragment : Fragment() {

    private val sharedLocationVM: SharedLocationViewModel by activityViewModels()

    private val apiKey = "zpka_c6f2c81a5688443aad64355246cf2029_a1526508"

    private lateinit var progress: ProgressBar
    private lateinit var tvStatus: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvDesc: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_current_weather, container, false)
        progress = v.findViewById(R.id.progress)
        tvStatus = v.findViewById(R.id.tvStatus)
        tvTemp = v.findViewById(R.id.tvTemp)
        tvDesc = v.findViewById(R.id.tvDesc)

        // React to the shared locationKey (defaults to Durban until Activity resolves device location)
        sharedLocationVM.locationKey.observe(viewLifecycleOwner) { key ->
            loadCurrentConditions(key)
        }

        return v
    }

    private fun loadCurrentConditions(locationKey: String) {
        showLoading("Loading current conditions...")

        RetrofitClient.api.getCurrentConditions(locationKey, apiKey, details = true)
            .enqueue(object : Callback<List<CurrentConditionsDto>> {
                override fun onResponse(
                    call: Call<List<CurrentConditionsDto>>,
                    response: Response<List<CurrentConditionsDto>>
                ) {
                    val body = response.body()
                    if (!response.isSuccessful || body.isNullOrEmpty()) {
                        showError("No current conditions found.")
                        return
                    }
                    val item = body.first()
                    val temp = item.Temperature?.Metric?.Value?.let { "$it°C" } ?: "—"
                    val desc = item.WeatherText ?: "—"
                    showData(temp, desc)
                }

                override fun onFailure(call: Call<List<CurrentConditionsDto>>, t: Throwable) {
                    showError(t.message ?: "Network error")
                }
            })
    }

    private fun showLoading(msg: String) {
        progress.visibility = View.VISIBLE
        tvStatus.visibility = View.VISIBLE
        tvStatus.text = msg
        tvTemp.visibility = View.GONE
        tvDesc.visibility = View.GONE
    }

    private fun showData(temp: String, desc: String) {
        progress.visibility = View.GONE
        tvStatus.visibility = View.GONE
        tvTemp.visibility = View.VISIBLE
        tvDesc.visibility = View.VISIBLE
        tvTemp.text = temp
        tvDesc.text = desc
    }

    private fun showError(msg: String) {
        progress.visibility = View.GONE
        tvStatus.visibility = View.VISIBLE
        tvStatus.text = msg
        tvTemp.visibility = View.GONE
        tvDesc.visibility = View.GONE
    }
}

package com.example.prog7314ices.ui
//Imports
//------------------------------------------------------------------------------------------------
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prog7314ices.R
import kotlin.concurrent.thread
import java.net.URL
import com.google.gson.Gson
//------------------------------------------------------------------------------------------------
//Fragment Class
//------------------------------------------------------------------------------------------------
class DailyForecastsFragment : Fragment() {
    //API Config
    //---------------------------------------------------------------
    private val apiKey = "zpka_c6f2c81a5688443aad64355246cf2029_a1526508" // AccuWeather API key
    //---------------------------------------------------------------

    //View References
    //---------------------------------------------------------------
    private lateinit var recycler: RecyclerView // The list on screen where each day will show
    //---------------------------------------------------------------

    // Shared key from Activity (defaults to Durban)
    private val sharedLocationVM: SharedLocationViewModel by activityViewModels()
//------------------------------------------------------------------------------------------------
    //Lifecycle - Create View
    //--------------------------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_daily_forecasts, container, false) // Draw the screen from XML
        recycler = v.findViewById(R.id.rvForecast) // Find the list in the layout
        recycler.layoutManager = LinearLayoutManager(requireContext()) // Show items in a simple vertical list

        // Observe shared locationKey; reload when it changes
        sharedLocationVM.locationKey.observe(viewLifecycleOwner) { key ->
            loadForecast(key)
        }
        return v
    }
    //--------------------------------------------------------------------------------------------

    //Networking
    //--------------------------------------------------------------------------------------------
    private fun loadForecast(locationKey: String) {
        thread { // Do internet work off the main thread so the app doesn’t freeze
            try {
                val apiUrl =
                    "https://dataservice.accuweather.com/forecasts/v1/daily/5day/$locationKey?apikey=$apiKey&metric=true"
                val response = URL(apiUrl).readText() // Download the raw text from the web address
                val items = parseFiveDay(response) // Turn the raw text into a list we can display

                activity?.runOnUiThread { // Switch back to the main thread to update the UI
                    recycler.adapter = ForecastAdapter(items) // Attach the list so it appears on screen
                }
            } catch (e: Exception) {
                e.printStackTrace()
                activity?.runOnUiThread {
                    recycler.adapter = ForecastAdapter(
                        listOf(Forecast("Error", "-", e.message ?: "Unknown error")) // Show a simple row if something went wrong
                    )
                }
            }
        }
    }
    //--------------------------------------------------------------------------------------------
    //Rows
    //--------------------------------------------------------------------------------------------
    private fun parseFiveDay(json: String): List<Forecast> {
        val data = Gson().fromJson(json, FiveDayForecast::class.java) // Let Gson turn the JSON into Kotlin objects
        val out = mutableListOf<Forecast>() // Collect rows here

        data.DailyForecasts?.forEach { day ->
            val date = day.Date?.substring(0, 10) ?: "—" // Keep only yyyy-mm-dd so it’s easy to read
            val min = day.Temperature?.Minimum?.Value?.toString() ?: "—" // Use dashes when data is missing
            val max = day.Temperature?.Maximum?.Value?.toString() ?: "—"
            out.add(Forecast(date, min, max)) // Add one row for the list
        }
        return out // Send rows back for display
    }
    //--------------------------------------------------------------------------------------------
}

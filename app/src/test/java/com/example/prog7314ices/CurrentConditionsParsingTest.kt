package com.example.prog7314ices

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CurrentConditionsParsingTest {
    @Test
    fun parse_sample_current_conditions() {
        // Real endpoint returns a LIST with one object
        val sampleJson = """
            [
              {
                "WeatherText": "Partly sunny",
                "Temperature": { "Metric": { "Value": 23.5, "Unit": "C" } }
              }
            ]
        """.trimIndent()

        val list = Gson().fromJson(sampleJson, Array<CurrentConditionsDto>::class.java).toList()
        assertEquals(1, list.size)
        val item = list.first()
        assertEquals("Partly sunny", item.WeatherText)
        assertEquals(23.5, item.Temperature!!.Metric!!.Value!!, 0.0)
        assertEquals("C", item.Temperature!!.Metric!!.Unit)
    }
}

package com.example.prog7314ices

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class FiveDayParsingTest {
    @Test
    fun parse_sample_five_day_json() {
        val sampleJson = """
            {
              "DailyForecasts": [
                {"Date":"2025-10-17T07:00:00+02:00","Temperature":{"Minimum":{"Value":14.0},"Maximum":{"Value":24.0}}},
                {"Date":"2025-10-18T07:00:00+02:00","Temperature":{"Minimum":{"Value":15.0},"Maximum":{"Value":25.0}}},
                {"Date":"2025-10-19T07:00:00+02:00","Temperature":{"Minimum":{"Value":16.0},"Maximum":{"Value":26.0}}},
                {"Date":"2025-10-20T07:00:00+02:00","Temperature":{"Minimum":{"Value":17.0},"Maximum":{"Value":27.0}}},
                {"Date":"2025-10-21T07:00:00+02:00","Temperature":{"Minimum":{"Value":18.0},"Maximum":{"Value":28.0}}}
              ]
            }
        """.trimIndent()

        val data = Gson().fromJson(sampleJson, FiveDayForecastDto::class.java)
        assertNotNull(data.DailyForecasts)
        assertEquals(5, data.DailyForecasts!!.size)
        val first = data.DailyForecasts!![0]
        assertEquals("2025-10-17T07:00:00+02:00", first.Date)
        assertEquals(14.0, first.Temperature!!.Minimum!!.Value!!, 0.0)
        assertEquals(24.0, first.Temperature!!.Maximum!!.Value!!, 0.0)
    }
}

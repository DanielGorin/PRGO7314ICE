package com.example.prog7314ices.ui

data class FiveDayForecast(
    val DailyForecasts: List<DailyForecast>?
)

data class DailyForecast(
    val Date: String?,
    val Temperature: Temperature?
)

data class Temperature(
    val Minimum: TempValue?,
    val Maximum: TempValue?
)

data class TempValue(
    val Value: Double?
)

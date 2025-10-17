package com.example.prog7314ices.network
data class CurrentConditionsDto(
    val WeatherText: String?,
    val Temperature: TemperatureBlock?
)
data class TemperatureBlock(
    val Metric: MetricValue?
)
data class MetricValue(
    val Value: Double?,
    val Unit: String?
)
data class FiveDayForecastDto(
    val DailyForecasts: List<DailyForecastDto>?
)
data class DailyForecastDto(
    val Date: String?,
    val Temperature: DailyTemperatureDto?
)
data class DailyTemperatureDto(
    val Minimum: MinMaxDto?,
    val Maximum: MinMaxDto?
)
data class MinMaxDto(
    val Value: Double?
)
data class GeoPositionDto(
    val Key: String?
)
data class CitySearchResultDto(
    val Key: String?,
    val LocalizedName: String?,
    val Country: CountryDto?
)
data class CountryDto(
    val LocalizedName: String?
)

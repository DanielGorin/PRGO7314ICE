package com.example.prog7314ices.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface AccuWeatherApi {
    // Current conditions for a locationKey
    @GET("currentconditions/v1/{locationKey}")
    fun getCurrentConditions(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("details") details: Boolean = true
    ): Call<List<CurrentConditionsDto>>

    // 5-day daily forecast
    @GET("forecasts/v1/daily/5day/{locationKey}")
    fun getFiveDayForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("metric") metric: Boolean = true
    ): Call<FiveDayForecastDto>

    @GET("locations/v1/cities/geoposition/search")
    fun getLocationKeyByGeo(
        @Query("apikey") apiKey: String,
        @Query("q") latCommaLon: String   // "lat,lon"
    ): Call<GeoPositionDto>

    @GET("locations/v1/cities/search")
    fun searchCity(
        @Query("apikey") apiKey: String,
        @Query("q") cityQuery: String
    ): Call<List<CitySearchResultDto>>

}

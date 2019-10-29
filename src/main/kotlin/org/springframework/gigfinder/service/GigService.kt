package org.springframework.gigfinder.service

import Json4Kotlin_Base
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import org.springframework.gigfinder.model.location.Json4Kotlin_LocationBase
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class GigService {

    @Autowired
    lateinit var appProperties: AppProperties

    // Create this as a singleton
    private val okHttpClient = OkHttpClient()


    fun createGigDetails(): GigDetailsForm {

        var gigDetailsForm = GigDetailsForm()
        gigDetailsForm.gigList = ArrayList<GigDetails>()

        return gigDetailsForm
    }

    fun getGigs(gigDetailsForm: GigDetailsForm): GigDetailsForm {

        var metroAreaId = getMetroAreaIdFromCurrentLocation(gigDetailsForm.gigLocation)

        gigDetailsForm.gigList = getGigsFromMetroAreaId(metroAreaId)

        return gigDetailsForm

    }

    fun getMetroAreaIdFromCurrentLocation(gigLocation: String): Int {
        var locationUrl = appProperties.songkickLocationUrl
        locationUrl = locationUrl?.replace("param1", gigLocation)
        locationUrl = locationUrl?.replace("param2", appProperties.songkickApiKey)

        val request = Request.Builder()
                .get()
                .url(locationUrl!!)
                .build()

        var jsonAsString = ""

        val response = okHttpClient.newCall(request).execute()
        response.use {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            jsonAsString = response.body!!.string()
        }

        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_LocationBase::class.java)

        var metroAreaId = 0
        if (jsonObj.resultsPage.totalEntries > 0) {
            for (location in jsonObj.resultsPage.results.location) {
                if ("UK".equals(location.city.country.displayName)) {
                    metroAreaId = location.metroArea.id
                    break
                }
            }
        }

        return metroAreaId
    }


    fun getGigsFromMetroAreaId(metroAreaId: Int): ArrayList<GigDetails> {
        var metroAreaUrl = appProperties.songkickMetroAreaUrl
        metroAreaUrl = metroAreaUrl?.replace("param1", metroAreaId.toString())
        metroAreaUrl = metroAreaUrl?.replace("param2", appProperties.songkickApiKey)

        val request = Request.Builder()
                .get()
                .url(metroAreaUrl!!)
                .build()

        var jsonAsString = ""

        val response = okHttpClient.newCall(request).execute()
        response.use {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            jsonAsString = response.body!!.string()
        }

        println(jsonAsString)
        var gigList: ArrayList<GigDetails> = ArrayList<GigDetails>()


        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)
        val df = DateTimeFormatter.ofPattern("dd-MMM-yyyy")

        if (jsonObj.resultsPage.totalEntries > 0) {
            for (event in jsonObj.resultsPage.results.event) {
                var gigDetails: GigDetails = GigDetails()

                var startTime = "No time specified"
                if (!event.start.time.isNullOrBlank()) {
                    startTime = event.start.time?.substring(0, 5)
                }
                val artistName = StringBuilder()
                val artistNameArray = event.displayName.split(" ")
                var wordIndex = artistNameArray.indexOf("at") - 1
                for (i in 0..wordIndex) {
                    artistName.append(artistNameArray[i]).append(" ")
                }

                gigDetails.artist = artistName.toString().trim()
                gigDetails.venue = event.venue.displayName
                gigDetails.location = event.location.city
                gigDetails.startDate = LocalDate.parse(event.start.date).format(df).toString()
                gigDetails.startTime = startTime

                gigList.add(gigDetails)
            }
        }

        return gigList

    }

}

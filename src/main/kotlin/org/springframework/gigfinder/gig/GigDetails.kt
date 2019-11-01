package org.springframework.gigfinder.gig

data class GigDetails (

    var artist: String    = "",

    var venue: String     = "",

    var location: String  = "",

    var startDate: String = "",

    var songkickUrl : String= "",

    // Start time could be null so define as String?
    var startTime: String? = ""

)

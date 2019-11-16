package org.springframework.gigfinder.gig

data class GigDetails (

    var artist: String       = "",

    var venue: String        = "",

    var location: String     = "",

    var startDate: String    = "",

    var songkickUrl : String = "",

    var gigToday : Boolean   = false,

    var startTime: String    = "No time specified "

)

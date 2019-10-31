package org.springframework.gigfinder.gig

import java.time.LocalDate

class GigDetails () {

    var artist    = ""

    var venue     = ""

    var location  = ""

    var startDate = ""

    var songkickUrl = ""

    // Start time could be null so define as String?
    var startTime: String? = ""

}

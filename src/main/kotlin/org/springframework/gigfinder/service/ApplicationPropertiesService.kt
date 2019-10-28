package org.springframework.gigfinder.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {

    @Value("\${songkick.api.key}")
    lateinit var songkickApiKey: String

    @Value("\${songkick.location.url}")
    var songkickLocationUrl: String? = null

    @Value("\${songkick.metro.area.url}")
    var songkickMetroAreaUrl: String? = null

}

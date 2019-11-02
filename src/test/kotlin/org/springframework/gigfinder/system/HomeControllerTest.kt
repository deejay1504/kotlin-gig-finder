package org.springframework.gigfinder.system


import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import org.springframework.gigfinder.service.GigService
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(HomeController::class)
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var gigService: GigService

    private lateinit var gigDetailsForm: GigDetailsForm

    val VIEWS_GIG_DETAILS_FORM = "gig/gigDetailsForm"

    @BeforeEach
    fun setup() {
        gigDetailsForm = GigDetailsForm()
        gigDetailsForm.totalEntries = 1
        gigDetailsForm.resultsPerPage= 20
        gigDetailsForm.metroAreaId = 24555
        gigDetailsForm.gigList = ArrayList<GigDetails>()
        given(gigService.createGigDetails()).willReturn(gigDetailsForm)
    }

    @Test
    fun testInitCreationForm() {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("gigDetailsForm"))
                .andExpect(view().name(VIEWS_GIG_DETAILS_FORM))
    }

}

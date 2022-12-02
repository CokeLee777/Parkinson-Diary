package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.MedicineHistoryRequest
import healthcare.severance.parkinson.util.WebProperties.MEDICINE_HISTORY_PATH
import retrofit2.Call
import retrofit2.http.*

interface MedicineHistoryApi {

    /**
     * Request URL: http://[host]:[port]/api/medicine-history
     */

    @POST(MEDICINE_HISTORY_PATH)
    fun createMedicineNotificationHistory(@Header("ACCESS_TOKEN") accessToken: String,
                                          @Body medicineHistoryRequest: MedicineHistoryRequest) : Call<Void>

    @PUT(MEDICINE_HISTORY_PATH)
    fun updateMedicineNotificationHistory(@Header("ACCESS_TOKEN") accessToken: String,
                                          @Body medicineHistoryRequest: MedicineHistoryRequest) : Call<Void>
}
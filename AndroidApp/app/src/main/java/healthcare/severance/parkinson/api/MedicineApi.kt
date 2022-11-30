package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.util.WebProperties.MEDICINE_PATH
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MedicineApi {

    /**
     * Request URL: http://[host]:[port]/api/medicine
     */

    @POST("$MEDICINE_PATH/notification")
    fun registerMedicineNotification(@Header("ACCESS_TOKEN") accessToken: String) : Call<Void>

    @DELETE("$MEDICINE_PATH/notification")
    fun cancelMedicineNotification(@Header("ACCESS_TOKEN") accessToken: String) : Call<Void>
}
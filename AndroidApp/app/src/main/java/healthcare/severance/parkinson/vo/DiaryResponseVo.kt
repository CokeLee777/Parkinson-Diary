package healthcare.severance.parkinson.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DiaryResponseVo (
    val data: DiaryResponse
)

data class DiaryResponse (
    //JSON 형식으로 요청, 응답시 Snake 명명 표기법
    @SerializedName(value = "sleep_start_time")
    val sleepStartTime: String,

    @SerializedName(value = "sleep_end_time")
    val sleepEndTime: String,

    @SerializedName(value = "take_times")
    val takeTimes: ArrayList<TakeTime>
)

data class TakeTime(

    @SerializedName(value = "take_time")
    var takeTime: String
): Serializable
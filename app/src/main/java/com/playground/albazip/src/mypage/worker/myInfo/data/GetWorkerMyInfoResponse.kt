
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.mypage.worker.init.data.UserInfo
import com.playground.albazip.src.mypage.worker.init.data.WorkInfo
import com.google.gson.annotations.SerializedName

data class GetWorkerMyInfoResponse(
    @SerializedName("data") val data: WorkerInfoData
) : BaseResponse()

data class WorkerInfoData(
    @SerializedName("userInfo") val userInfo: UserInfo,
    @SerializedName("workInfo") val workInfo: WorkInfo,
    @SerializedName("joinDate") val joinDate: String
)
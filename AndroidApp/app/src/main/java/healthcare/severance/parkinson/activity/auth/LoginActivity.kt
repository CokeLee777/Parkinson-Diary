package healthcare.severance.parkinson.activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.dto.LoginRequest
import healthcare.severance.parkinson.dto.LoginResponse
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    //토큰 정보를 불러오기 위한 세션 매니저 선언
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // 세션 매니저 초기화
        sessionManager = SessionManager(applicationContext)
    }

    fun login(view: View){
        //입력한 환자번호 가져오기
        val inputPatientNum: String = findViewById<TextView>(R.id.lLoginForm).text
            .toString()
        if(inputPatientNum.isBlank() || inputPatientNum.isEmpty()){
            Toast.makeText(this@LoginActivity, "존재하지 않는 환자번호 입니다",
                Toast.LENGTH_SHORT).show()
            return
        }
        //서버로 로그인 요청
        RetrofitClient.loginService.login(
            LoginRequest(
                patientNum = inputPatientNum.toLong()
            )
        ).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                //요청 성공시
                if(response.isSuccessful) {
                    val loginResponse: LoginResponse = response.body()!!
                    //메모리에 JWT 토큰 저장
                    sessionManager.saveAccessToken(loginResponse.accessToken)
                    //알림 정보도 저장
                    sessionManager.saveAlarmIsActive()

                    // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    // 다이어리 세팅 시작
                    startActivity(intent)
                } else if(response.code() == 400) {
                    Toast.makeText(this@LoginActivity, "올바르지 않은 입력입니다",
                        Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    Toast.makeText(this@LoginActivity, "존재하지 않는 환자번호 입니다",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "알수없는 이유로 로그인이 불가합니다",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@LoginActivity, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}
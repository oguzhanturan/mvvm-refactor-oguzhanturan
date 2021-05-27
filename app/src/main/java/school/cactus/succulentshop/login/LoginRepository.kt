package school.cactus.succulentshop.login

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.api.login.LoginResponse
import school.cactus.succulentshop.errorMessage
import school.cactus.succulentshop.infra.util.RequestCallBack

class LoginRepository {
    fun sendLoginRequest(
        identifier: String,
        password: String,
        callback: LoginRequestCallback
    ) {
        val request = LoginRequest(identifier, password)

        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                when (response.code()) {
                    200 -> callback.onSuccess(response.body()!!.jwt)
                    in 400..499 -> callback.onClientError(response.errorBody()!!.errorMessage())
                    else -> callback.onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

    interface LoginRequestCallback : RequestCallBack {
        fun onSuccess(jwt: String)
        fun onClientError(errorMessage: String)
    }
}
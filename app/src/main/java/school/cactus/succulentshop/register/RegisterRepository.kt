package school.cactus.succulentshop.register

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.register.RegisterRequest
import school.cactus.succulentshop.api.register.RegisterResponse
import school.cactus.succulentshop.errorMessage
import school.cactus.succulentshop.infra.util.RequestCallBack


class RegisterRepository {
    fun sendRegisterRequest(
        email: String,
        username: String,
        password: String,
        callback: RegisterRequestCallback
    ) {

        val request = RegisterRequest(email, password, username)

        api.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                when (response.code()) {
                    in 200..299 -> callback.onRegisterSuccess(response.body()!!.jwt)
                    in 400..499 -> callback.onClientError(response.errorBody()!!.errorMessage())
                    else -> callback.onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

    interface RegisterRequestCallback : RequestCallBack {
        fun onRegisterSuccess(jwt: String)
        fun onClientError(errorMessage: String)
    }
}
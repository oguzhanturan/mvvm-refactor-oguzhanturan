package school.cactus.succulentshop

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import school.cactus.succulentshop.api.GenericErrorResponse

fun Int.resolveAsString(context: Context) = context.getString(this)

fun ResponseBody.errorMessage(): String {
    val errorBody = string()
    val gson: Gson = GsonBuilder().create()
    val errorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
    return errorResponse.message[0].messages[0].message
}

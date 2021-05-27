package school.cactus.succulentshop.infra.util

interface RequestCallBack {
    fun onTokenExpired()
    fun onUnexpectedError()
    fun onFailure()
}
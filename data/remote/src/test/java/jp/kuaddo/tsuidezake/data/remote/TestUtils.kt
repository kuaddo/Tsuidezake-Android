package jp.kuaddo.tsuidezake.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class TsuidezakeDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val rootHandler = RankingHandler()
        rootHandler.setNext(SakeHandler())
        return rootHandler.dispatch(request.getOperationName())
    }

    companion object {
        private val gson = Gson()

        private fun RecordedRequest.getOperationName() =
            gson.fromJson(body.readUtf8(), JsonObject::class.java)
                .get("operationName")
                .asString
    }
}

private class RankingHandler : Handler() {
    override fun handle(operationName: String): MockResponse? {
        return null
    }
}

private class SakeHandler : Handler() {
    override fun handle(operationName: String): MockResponse? {
        return null
    }
}

private abstract class Handler {
    private var next: Handler? = null

    protected abstract fun handle(operationName: String): MockResponse?

    fun setNext(next: Handler): Handler {
        this.next = next
        return next
    }

    fun dispatch(operationName: String): MockResponse =
        handle(operationName)
            ?: next?.dispatch(operationName)
            ?: error("$operationName can't handle. Please check chain of responsibility.")
}

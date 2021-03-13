package jp.kuaddo.tsuidezake.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okio.BufferedSource
import okio.buffer
import okio.source

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
    override val jsonDirectoryPath: String = "mock_response/ranking"
    override val operationNameToJsonFileNameMap = mapOf(
        "RankingsQuery" to "RankingsQuery.json"
    )
}

private class SakeHandler : Handler() {
    override val jsonDirectoryPath: String = "mock_response/sake"
    override val operationNameToJsonFileNameMap = emptyMap<String, String>()
}

private abstract class Handler {
    protected abstract val jsonDirectoryPath: String
    protected abstract val operationNameToJsonFileNameMap: Map<String, String>
    private var next: Handler? = null

    fun setNext(next: Handler): Handler {
        this.next = next
        return next
    }

    fun dispatch(operationName: String): MockResponse =
        handle(operationName)
            ?: next?.dispatch(operationName)
            ?: error("$operationName can't handle. Please check chain of responsibility.")

    private fun handle(operationName: String): MockResponse? {
        val fileName = operationNameToJsonFileNameMap[operationName] ?: return null
        val json = javaClass.classLoader!!
            .getResourceAsStream("$jsonDirectoryPath/$fileName")
            .source()
            .buffer()
            .use(BufferedSource::readUtf8)
        return MockResponse().apply { setBody(json) }
    }
}

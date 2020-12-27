package jp.kuaddo.tsuidezake.data.repository.internal

import androidx.annotation.WorkerThread
import jp.kuaddo.tsuidezake.data.repository.ApiResponse
import jp.kuaddo.tsuidezake.data.repository.ErrorResponse
import jp.kuaddo.tsuidezake.data.repository.SuccessResponse
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.LoadingResource
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SuccessResource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * https://github.com/android/architecture-components-samples/blob/main/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
 */
internal abstract class NetworkBoundResource<LocalSourceResult : Any, RemoteSourceResult> {
    private val result = MutableSharedFlow<Resource<LocalSourceResult>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun getResultFlow(): Flow<Resource<LocalSourceResult>> {
        result.tryEmit(LoadingResource(null))
        GlobalScope.launch {
            val dbFlow = loadFromDb()
            if (shouldFetch(dbFlow.firstOrNull())) fetchFromNetwork(dbFlow)
            else result.emitAll(dbFlow.filterNotNull().map(::SuccessResource))
        }
        return result.distinctUntilChanged()
    }

    private suspend fun fetchFromNetwork(dbFlow: Flow<LocalSourceResult?>) {
        val loadingJob = GlobalScope.launch {
            result.emitAll(dbFlow.map(::LoadingResource))
        }
        val apiResponse = callApi()
        loadingJob.cancel()

        when (apiResponse) {
            is SuccessResponse -> {
                saveApiResult(apiResponse.data)
                result.emitAll(dbFlow.filterNotNull().map(::SuccessResource))
            }
            is ErrorResponse -> {
                onFetchFailed()
                result.emitAll(dbFlow.map { ErrorResource(apiResponse.message, it) })
            }
        }
    }

    protected abstract fun loadFromDb(): Flow<LocalSourceResult?>

    protected abstract fun shouldFetch(data: LocalSourceResult?): Boolean

    @WorkerThread
    protected abstract suspend fun callApi(): ApiResponse<RemoteSourceResult>

    @WorkerThread
    protected abstract suspend fun saveApiResult(item: RemoteSourceResult)

    protected open fun onFetchFailed() {
    }
}

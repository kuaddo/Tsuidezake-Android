package jp.kuaddo.tsuidezake.data.remote.internal

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

internal class FirebaseImagePathConverter @Inject constructor() {
    private val firebaseStorage = FirebaseStorage.getInstance()

    suspend fun getImageUriString(firebaseImagePath: String): String? =
        runCatching { getImageUri(firebaseImagePath) }
            .onFailure { if (it is CancellationException) throw it }
            .getOrNull()
            ?.toString()

    private suspend fun getImageUri(firebaseImagePath: String): Uri? = firebaseStorage
        .getReferenceFromUrl(firebaseImagePath)
        .downloadUrl
        .await()
}

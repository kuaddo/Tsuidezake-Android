package jp.kuaddo.tsuidezake.data.remote.internal

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import jp.kuaddo.tsuidezake.core.runCatchingS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseImagePathConverter @Inject constructor() {
    private val firebaseStorage = FirebaseStorage.getInstance()

    suspend fun getImageUriString(firebaseImagePath: String): String? =
        runCatchingS { getImageUri(firebaseImagePath) }
            .getOrNull()
            ?.toString()

    private suspend fun getImageUri(firebaseImagePath: String): Uri? = firebaseStorage
        .getReferenceFromUrl(firebaseImagePath)
        .downloadUrl
        .await()
}

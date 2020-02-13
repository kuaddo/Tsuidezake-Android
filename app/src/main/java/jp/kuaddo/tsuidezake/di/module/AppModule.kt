package jp.kuaddo.tsuidezake.di.module

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    @JvmStatic
    fun provideNotificationManager(context: Context): NotificationManager? =
        context.getSystemService()
}
package gq.kirmanak.mealient.extensions

import android.app.Activity
import android.app.Application
import android.app.job.JobInfo
import android.content.Context
import android.content.ContextWrapper
import gq.kirmanak.mealient.BuildConfig
import org.acra.config.httpSender
import org.acra.config.scheduler
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.acra.sender.HttpSender

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

internal fun Application.setupCrashReporting() {
    val acraHost = BuildConfig.ACRA_HOST.takeUnless { it.isBlank() } ?: return
    val acraLogin = BuildConfig.ACRA_LOGIN.takeUnless { it.isBlank() } ?: return
    val acraPassword = BuildConfig.ACRA_PASSWORD.takeUnless { it.isBlank() } ?: return
    initAcra {
        reportFormat = StringFormat.JSON
        alsoReportToAndroidFramework = true

        httpSender {
            uri = "$acraHost/report"
            basicAuthLogin = acraLogin
            basicAuthPassword = acraPassword
            httpMethod = HttpSender.Method.POST
            // TODO compressed reports are failing due to https://github.com/F43nd1r/Acrarium/issues/458
            compress = false
        }

        scheduler {
            requiresNetworkType = JobInfo.NETWORK_TYPE_UNMETERED
            requiresBatteryNotLow = true
        }
    }
}
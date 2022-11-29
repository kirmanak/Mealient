package gq.kirmanak.mealient.ui

import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible

sealed class OperationUiState<T> {

    val exceptionOrNull: Throwable?
        get() = (this as? Failure)?.exception

    val isSuccess: Boolean
        get() = this is Success

    val isProgress: Boolean
        get() = this is Progress

    val isFailure: Boolean
        get() = this is Failure

    fun updateButtonState(button: Button) {
        button.isEnabled = !isProgress
        button.isClickable = !isProgress
    }

    fun updateProgressState(progressBar: ProgressBar) {
        progressBar.isVisible = isProgress
    }

    class Initial<T> : OperationUiState<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Progress<T> : OperationUiState<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    data class Failure<T>(val exception: Throwable) : OperationUiState<T>()

    data class Success<T>(val value: T) : OperationUiState<T>()

    companion object {
        fun <T> fromResult(result: Result<T>) = result.fold({ Success(it) }, { Failure(it) })
    }
}

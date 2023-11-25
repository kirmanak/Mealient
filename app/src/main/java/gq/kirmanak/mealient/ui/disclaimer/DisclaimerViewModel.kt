package gq.kirmanak.mealient.ui.disclaimer

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
internal class DisclaimerViewModel @Inject constructor(
    private val application: Application,
    private val disclaimerStorage: DisclaimerStorage,
    private val logger: Logger,
) : ViewModel() {

    private var isCountDownStarted = false

    private val okayCountDown = MutableStateFlow(FULL_COUNT_DOWN_SEC)

    val screenState: StateFlow<DisclaimerScreenState> = okayCountDown
        .map(::countDownToScreenState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = countDownToScreenState(okayCountDown.value)
        )

    val isAcceptedState: StateFlow<Boolean>
        get() = disclaimerStorage
            .isDisclaimerAcceptedFlow
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private fun countDownToScreenState(countDown: Int): DisclaimerScreenState {
        val isCountDownOver = countDown == 0
        return DisclaimerScreenState(
            buttonText = if (isCountDownOver) {
                application.getString(R.string.fragment_disclaimer_button_okay)
            } else {
                application.resources.getQuantityString(
                    R.plurals.fragment_disclaimer_button_okay_timer,
                    countDown,
                    countDown,
                )
            },
            buttonEnabled = isCountDownOver,
        )
    }

    fun acceptDisclaimer() {
        logger.v { "acceptDisclaimer() called" }
        viewModelScope.launch { disclaimerStorage.acceptDisclaimer() }
    }

    fun startCountDown() {
        logger.v { "startCountDown() called" }
        if (isCountDownStarted) return
        isCountDownStarted = true
        tickerFlow(COUNT_DOWN_TICK_PERIOD_SEC.toLong(), TimeUnit.SECONDS)
            .take(FULL_COUNT_DOWN_SEC - COUNT_DOWN_TICK_PERIOD_SEC + 1)
            .onEach { okayCountDown.value = FULL_COUNT_DOWN_SEC - it }
            .launchIn(viewModelScope)
    }

    /**
     * Sends an event every [period] of [timeUnit]. For example, if period = 3 and timeUnit = SECOND
     * then this will send an event every 3 seconds. Additionally to the event, it sends counter
     * of how many ticks there were. It doesn't depend on period or timeUnit parameters, it just
     * counts how many events it sent starting from 1.
     */
    @VisibleForTesting
    fun tickerFlow(period: Long, timeUnit: TimeUnit) = flow {
        logger.v { "tickerFlow() called with: period = $period, timeUnit = $timeUnit" }
        val periodMillis = timeUnit.toMillis(period)
        var counter = 0
        while (true) {
            delay(periodMillis)
            counter++
            emit(counter)
        }
    }

    companion object {
        private const val FULL_COUNT_DOWN_SEC = 5
        private const val COUNT_DOWN_TICK_PERIOD_SEC = 1
    }
}
package gq.kirmanak.mealient.ui.disclaimer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.BaseScreen
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Destination
@Composable
internal fun DisclaimerScreen(
    navController: NavController,
    viewModel: DisclaimerViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()
    val isAccepted by viewModel.isAcceptedState.collectAsState()

    LaunchedEffect(isAccepted) {
        if (isAccepted) {
            navController.navigateUp()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startCountDown()
    }

    BaseScreen { modifier ->
        DisclaimerScreen(
            modifier = modifier,
            state = screenState,
            onButtonClick = viewModel::acceptDisclaimer,
        )
    }
}

@Composable
internal fun DisclaimerScreen(
    state: DisclaimerScreenState,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(Dimens.Large)
            .semantics { testTag = "disclaimer-screen" },
        verticalArrangement = Arrangement.spacedBy(Dimens.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedCard {
            Text(
                modifier = Modifier
                    .padding(Dimens.Medium)
                    .semantics { testTag = "disclaimer-text" },
                text = stringResource(id = R.string.fragment_disclaimer_main_text),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Button(
            modifier = Modifier
                .semantics { testTag = "okay-button" },
            onClick = onButtonClick,
            enabled = state.isCountDownOver,
        ) {
            val text = if (state.isCountDownOver) {
                stringResource(R.string.fragment_disclaimer_button_okay)
            } else {
                pluralStringResource(
                    R.plurals.fragment_disclaimer_button_okay_timer,
                    state.countDown,
                    state.countDown,
                )
            }
            Text(
                modifier = Modifier
                    .semantics { testTag = "okay-button-text" },
                text = text,
            )
        }
    }
}

@ColorSchemePreview
@Composable
private fun DisclaimerScreenPreview() {
    AppTheme {
        DisclaimerScreen(
            state = DisclaimerScreenState(
                isCountDownOver = false,
                countDown = 5,
            ),
            onButtonClick = {},
        )
    }
}
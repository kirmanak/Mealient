package gq.kirmanak.mealient.ui.disclaimer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun DisclaimerScreen(
    state: DisclaimerScreenState,
    onButtonClick: () -> Unit,
) {
    Scaffold { containerPadding ->
        Column(
            modifier = Modifier
                .padding(containerPadding)
                .consumeWindowInsets(containerPadding)
                .padding(Dimens.Large),
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
                enabled = state.buttonEnabled,
            ) {
                Text(
                    modifier = Modifier
                        .semantics { testTag = "okay-button-text" },
                    text = state.buttonText,
                )
            }
        }
    }
}

@ColorSchemePreview
@Composable
private fun DisclaimerScreenPreview() {
    AppTheme {
        DisclaimerScreen(
            state = DisclaimerScreenState(
                buttonText = "Okay (5 seconds)",
                buttonEnabled = false,
            ),
            onButtonClick = {},
        )
    }
}
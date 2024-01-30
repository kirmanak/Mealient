package gq.kirmanak.mealient.ui.recipes.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.Dimens
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.TextFieldValue

@Composable
internal fun HeaderSection(
    imageUrl: String?,
    title: String,
    description: String,
    isEditMode: Boolean,
) {
    val imageFallback = painterResource(id = R.drawable.placeholder_recipe)

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f) // 2:1
                .clip(
                    RoundedCornerShape(
                        topEnd = 0.dp,
                        topStart = 0.dp,
                        bottomEnd = Dimens.Intermediate,
                        bottomStart = Dimens.Intermediate,
                    )
                ),
            model = imageUrl,
            contentDescription = stringResource(id = R.string.content_description_fragment_recipe_info_image),
            placeholder = imageFallback,
            error = imageFallback,
            fallback = imageFallback,
            contentScale = ContentScale.Crop,
        )


        if (isEditMode) {
            // ToDo: Only for Testing
            var titleValue by remember {
                mutableStateOf(TextFieldValue(title))
            }

            OutlinedTextField(
                value = titleValue,
                onValueChange = { titleValue = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "title")}
            )
            OutlinedTextField(
                value = description!!,
                onValueChange = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "description")}
            )
        } else {
            if (!title.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = Dimens.Small),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            if (!description.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = Dimens.Small),
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
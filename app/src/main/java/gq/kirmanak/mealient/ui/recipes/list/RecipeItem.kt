package gq.kirmanak.mealient.ui.recipes.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview
import gq.kirmanak.mealient.ui.recipes.info.SUMMARY_ENTITY
import kotlin.random.Random

@Composable
internal fun RecipeItem(
    recipe: RecipeListItemState,
    onDeleteClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onItemClick)
                .padding(
                    horizontal = Dimens.Medium,
                    vertical = Dimens.Small,
                ),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small),
        ) {
            RecipeHeader(
                onDeleteClick = onDeleteClick,
                recipe = recipe,
                onFavoriteClick = onFavoriteClick
            )

            RecipeImage(
                recipe = recipe,
            )

            Text(
                text = recipe.entity.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Composable
private fun RecipeImage(
    recipe: RecipeListItemState,
) {
    val imageFallback = painterResource(id = R.drawable.placeholder_recipe)

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f) // 2:1
            .clip(RoundedCornerShape(Dimens.Intermediate)),
        model = recipe.imageUrl,
        contentDescription = stringResource(id = R.string.content_description_fragment_recipe_info_image),
        placeholder = imageFallback,
        error = imageFallback,
        fallback = imageFallback,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun RecipeHeader(
    onDeleteClick: () -> Unit,
    recipe: RecipeListItemState,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        IconButton(
            onClick = onDeleteClick,
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.view_holder_recipe_delete_content_description),
            )
        }

        if (recipe.showFavoriteIcon) {
            IconButton(
                onClick = onFavoriteClick,
            ) {
                Icon(
                    imageVector = if (recipe.entity.isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = if (recipe.entity.isFavorite) {
                        stringResource(id = R.string.view_holder_recipe_favorite_content_description)
                    } else {
                        stringResource(id = R.string.view_holder_recipe_non_favorite_content_description)
                    },
                )
            }
        }
    }
}

@ColorSchemePreview
@Composable
private fun RecipeItemPreview() {
    val isFavorite = Random.nextBoolean()
    AppTheme {
        RecipeItem(
            recipe = RecipeListItemState(null, isFavorite, SUMMARY_ENTITY),
            onDeleteClick = {},
            onFavoriteClick = {},
            onItemClick = {},
        )
    }
}
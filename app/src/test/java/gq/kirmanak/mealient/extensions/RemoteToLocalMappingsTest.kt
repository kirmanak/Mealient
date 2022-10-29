package gq.kirmanak.mealient.extensions

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeIngredientV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeInstructionV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeSettingsV0
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft
import org.junit.Test

class RemoteToLocalMappingsTest {

    @Test
    fun `when toAddRecipeRequest then fills fields correctly`() {
        val input = AddRecipeDraft(
            recipeName = "Recipe name",
            recipeDescription = "Recipe description",
            recipeYield = "Recipe yield",
            recipeInstructions = listOf("Recipe instruction 1", "Recipe instruction 2"),
            recipeIngredients = listOf("Recipe ingredient 1", "Recipe ingredient 2"),
            isRecipePublic = false,
            areCommentsDisabled = true,
        )

        val expected = AddRecipeRequestV0(
            name = "Recipe name",
            description = "Recipe description",
            recipeYield = "Recipe yield",
            recipeIngredient = listOf(
                AddRecipeIngredientV0(note = "Recipe ingredient 1"),
                AddRecipeIngredientV0(note = "Recipe ingredient 2")
            ),
            recipeInstructions = listOf(
                AddRecipeInstructionV0(text = "Recipe instruction 1"),
                AddRecipeInstructionV0(text = "Recipe instruction 2")
            ),
            settings = AddRecipeSettingsV0(
                public = false,
                disableComments = true,
            )
        )

        assertThat(input.toAddRecipeRequest()).isEqualTo(expected)
    }

    @Test
    fun `when toDraft then fills fields correctly`() {
        val request = AddRecipeRequestV0(
            name = "Recipe name",
            description = "Recipe description",
            recipeYield = "Recipe yield",
            recipeIngredient = listOf(
                AddRecipeIngredientV0(note = "Recipe ingredient 1"),
                AddRecipeIngredientV0(note = "Recipe ingredient 2")
            ),
            recipeInstructions = listOf(
                AddRecipeInstructionV0(text = "Recipe instruction 1"),
                AddRecipeInstructionV0(text = "Recipe instruction 2")
            ),
            settings = AddRecipeSettingsV0(
                public = false,
                disableComments = true,
            )
        )

        val expected = AddRecipeDraft(
            recipeName = "Recipe name",
            recipeDescription = "Recipe description",
            recipeYield = "Recipe yield",
            recipeInstructions = listOf("Recipe instruction 1", "Recipe instruction 2"),
            recipeIngredients = listOf("Recipe ingredient 1", "Recipe ingredient 2"),
            isRecipePublic = false,
            areCommentsDisabled = true,
        )

        assertThat(request.toDraft()).isEqualTo(expected)
    }
}
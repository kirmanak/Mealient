package gq.kirmanak.mealient.extensions

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredient
import gq.kirmanak.mealient.datasource.models.AddRecipeInstruction
import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.datasource.models.AddRecipeSettings
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

        val expected = AddRecipeRequest(
            name = "Recipe name",
            description = "Recipe description",
            recipeYield = "Recipe yield",
            recipeIngredient = listOf(
                AddRecipeIngredient(note = "Recipe ingredient 1"),
                AddRecipeIngredient(note = "Recipe ingredient 2")
            ),
            recipeInstructions = listOf(
                AddRecipeInstruction(text = "Recipe instruction 1"),
                AddRecipeInstruction(text = "Recipe instruction 2")
            ),
            settings = AddRecipeSettings(
                public = false,
                disableComments = true,
            )
        )

        assertThat(input.toAddRecipeRequest()).isEqualTo(expected)
    }

    @Test
    fun `when toDraft then fills fields correctly`() {
        val request = AddRecipeRequest(
            name = "Recipe name",
            description = "Recipe description",
            recipeYield = "Recipe yield",
            recipeIngredient = listOf(
                AddRecipeIngredient(note = "Recipe ingredient 1"),
                AddRecipeIngredient(note = "Recipe ingredient 2")
            ),
            recipeInstructions = listOf(
                AddRecipeInstruction(text = "Recipe instruction 1"),
                AddRecipeInstruction(text = "Recipe instruction 2")
            ),
            settings = AddRecipeSettings(
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
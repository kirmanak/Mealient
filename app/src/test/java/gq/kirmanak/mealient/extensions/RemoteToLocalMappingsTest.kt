package gq.kirmanak.mealient.extensions

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.add.AddRecipeInfo
import gq.kirmanak.mealient.data.add.AddRecipeIngredientInfo
import gq.kirmanak.mealient.data.add.AddRecipeInstructionInfo
import gq.kirmanak.mealient.data.add.AddRecipeSettingsInfo
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

        val expected = AddRecipeInfo(
            name = "Recipe name",
            description = "Recipe description",
            recipeYield = "Recipe yield",
            recipeIngredient = listOf(
                AddRecipeIngredientInfo(note = "Recipe ingredient 1"),
                AddRecipeIngredientInfo(note = "Recipe ingredient 2")
            ),
            recipeInstructions = listOf(
                AddRecipeInstructionInfo(text = "Recipe instruction 1"),
                AddRecipeInstructionInfo(text = "Recipe instruction 2")
            ),
            settings = AddRecipeSettingsInfo(
                public = false,
                disableComments = true,
            )
        )

        assertThat(input.toAddRecipeInfo()).isEqualTo(expected)
    }

    @Test
    fun `when toDraft then fills fields correctly`() {
        val request = AddRecipeInfo(
            name = "Recipe name",
            description = "Recipe description",
            recipeYield = "Recipe yield",
            recipeIngredient = listOf(
                AddRecipeIngredientInfo(note = "Recipe ingredient 1"),
                AddRecipeIngredientInfo(note = "Recipe ingredient 2")
            ),
            recipeInstructions = listOf(
                AddRecipeInstructionInfo(text = "Recipe instruction 1"),
                AddRecipeInstructionInfo(text = "Recipe instruction 2")
            ),
            settings = AddRecipeSettingsInfo(
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
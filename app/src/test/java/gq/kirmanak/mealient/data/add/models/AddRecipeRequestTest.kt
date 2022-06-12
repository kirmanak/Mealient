package gq.kirmanak.mealient.data.add.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AddRecipeRequestTest {

    @Test
    fun `when construct from input then fills fields correctly`() {
        val input = AddRecipeInput.newBuilder()
            .setRecipeName("Recipe name")
            .setRecipeDescription("Recipe description")
            .setRecipeYield("Recipe yield")
            .addAllRecipeIngredients(listOf("Recipe ingredient 1", "Recipe ingredient 2"))
            .addAllRecipeInstructions(listOf("Recipe instruction 1", "Recipe instruction 2"))
            .setIsRecipePublic(false)
            .setAreCommentsDisabled(true)
            .build()

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

        assertThat(AddRecipeRequest(input)).isEqualTo(expected)
    }

    @Test
    fun `when toInput then fills fields correctly`() {
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

        val expected = AddRecipeInput.newBuilder()
            .setRecipeName("Recipe name")
            .setRecipeDescription("Recipe description")
            .setRecipeYield("Recipe yield")
            .addAllRecipeIngredients(listOf("Recipe ingredient 1", "Recipe ingredient 2"))
            .addAllRecipeInstructions(listOf("Recipe instruction 1", "Recipe instruction 2"))
            .setIsRecipePublic(false)
            .setAreCommentsDisabled(true)
            .build()

        assertThat(request.toInput()).isEqualTo(expected)
    }
}
package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithSummaryAndIngredientsAndInstructions(
    @Embedded val recipeEntity: RecipeEntity,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id"
    )
    val recipeSummaryEntity: RecipeSummaryEntity,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id"
    )
    val recipeIngredients: List<RecipeIngredientEntity>,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id"
    )
    val recipeInstructions: List<RecipeInstructionEntity>,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id"
    )
    val recipeIngredientToInstructionEntity: List<RecipeIngredientToInstructionEntity>,
)

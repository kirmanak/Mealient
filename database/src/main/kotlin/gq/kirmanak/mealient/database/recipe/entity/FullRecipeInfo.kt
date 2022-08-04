package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FullRecipeInfo(
    @Embedded val recipeEntity: RecipeEntity,
    @Relation(
        parentColumn = "remote_id",
        entityColumn = "remote_id"
    )
    val recipeSummaryEntity: RecipeSummaryEntity,
    @Relation(
        parentColumn = "remote_id",
        entityColumn = "recipe_id"
    )
    val recipeIngredients: List<RecipeIngredientEntity>,
    @Relation(
        parentColumn = "remote_id",
        entityColumn = "recipe_id"
    )
    val recipeInstructions: List<RecipeInstructionEntity>,
)

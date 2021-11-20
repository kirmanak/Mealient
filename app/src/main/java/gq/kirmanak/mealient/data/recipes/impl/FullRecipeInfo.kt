package gq.kirmanak.mealient.data.recipes.impl

import androidx.room.Embedded
import androidx.room.Relation
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity

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

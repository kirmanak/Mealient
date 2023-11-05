package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "recipe_ingredient_to_instruction",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["recipe_id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecipeIngredientEntity::class,
            parentColumns = ["recipe_ingredient_id"],
            childColumns = ["ingredient_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecipeInstructionEntity::class,
            parentColumns = ["recipe_instruction_id"],
            childColumns = ["instruction_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    primaryKeys = ["recipe_id", "ingredient_id", "instruction_id"]
)
data class RecipeIngredientToInstructionEntity(
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: String,
    @ColumnInfo(name = "ingredient_id", index = true) val ingredientId: String,
    @ColumnInfo(name = "instruction_id", index = true) val instructionId: String,
)

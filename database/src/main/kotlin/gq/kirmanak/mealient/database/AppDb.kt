package gq.kirmanak.mealient.database

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.*
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsDao
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity

@Database(
    version = 9,
    entities = [
        RecipeSummaryEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        RecipeInstructionEntity::class,
        ShoppingListEntity::class,
        ShoppingListItemEntity::class,
        ShoppingListItemRecipeReferenceEntity::class,
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = AppDb.From4To5Migration::class),
        AutoMigration(from = 5, to = 6, spec = AppDb.From5To6Migration::class),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9, spec = AppDb.From8To9Migration::class),
    ]
)
@TypeConverters(RoomTypeConverters::class)
internal abstract class AppDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    abstract fun shoppingListsDao(): ShoppingListsDao

    @DeleteColumn(tableName = "recipe_instruction", columnName = "title")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "title")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "unit")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "food")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "disable_amount")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "quantity")
    class From4To5Migration : AutoMigrationSpec

    @DeleteColumn(tableName = "recipe_summaries", columnName = "image")
    @DeleteColumn(tableName = "recipe_summaries", columnName = "rating")
    @DeleteTable(tableName = "tag_recipe")
    @DeleteTable(tableName = "tags")
    @DeleteTable(tableName = "categories")
    @DeleteTable(tableName = "category_recipe")
    class From5To6Migration : AutoMigrationSpec

    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "remote_id",
        toColumnName = "recipe_id"
    )
    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "date_added",
        toColumnName = "recipe_summaries_date_added"
    )
    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "date_updated",
        toColumnName = "recipe_summaries_date_updated"
    )
    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "is_favorite",
        toColumnName = "recipe_summaries_is_favorite"
    )
    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "name",
        toColumnName = "recipe_summaries_name"
    )
    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "description",
        toColumnName = "recipe_summaries_description"
    )
    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "image_id",
        toColumnName = "recipe_summaries_image_id"
    )
    @RenameColumn(
        tableName = "recipe_summaries",
        fromColumnName = "slug",
        toColumnName = "recipe_summaries_slug"
    )
    @RenameColumn(
        tableName = "recipe",
        fromColumnName = "disable_amounts",
        toColumnName = "recipe_disable_amounts"
    )
    @RenameColumn(
        tableName = "recipe_instruction",
        fromColumnName = "local_id",
        toColumnName = "recipe_instruction_local_id"
    )
    @RenameColumn(
        tableName = "recipe_instruction",
        fromColumnName = "text",
        toColumnName = "recipe_instruction_text"
    )
    @RenameColumn(
        tableName = "recipe_ingredient",
        fromColumnName = "note",
        toColumnName = "recipe_ingredient_note"
    )
    @RenameColumn(
        tableName = "recipe_ingredient",
        fromColumnName = "unit",
        toColumnName = "recipe_ingredient_unit"
    )
    @RenameColumn(
        tableName = "recipe_ingredient",
        fromColumnName = "quantity",
        toColumnName = "recipe_ingredient_quantity"
    )
    @RenameColumn(
        tableName = "recipe_ingredient",
        fromColumnName = "local_id",
        toColumnName = "recipe_ingredient_local_id"
    )
    @RenameColumn(
        tableName = "recipe_ingredient",
        fromColumnName = "title",
        toColumnName = "recipe_ingredient_title"
    )
    @RenameColumn(
        tableName = "recipe_ingredient",
        fromColumnName = "food",
        toColumnName = "recipe_ingredient_food"
    )
    @RenameColumn(
        tableName = "recipe",
        fromColumnName = "remote_id",
        toColumnName = "recipe_id"
    )
    class From8To9Migration : AutoMigrationSpec // TODO does not rename remote_id to recipe_id
}
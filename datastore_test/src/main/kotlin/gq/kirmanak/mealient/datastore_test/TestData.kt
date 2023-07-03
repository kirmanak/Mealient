package gq.kirmanak.mealient.datastore_test

import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft

val PORRIDGE_RECIPE_DRAFT = AddRecipeDraft(
    recipeName = "Porridge",
    recipeDescription = "A tasty porridge",
    recipeYield = "3 servings",
    recipeInstructions = listOf("Mix the ingredients", "Boil the ingredients"),
    recipeIngredients = listOf("2 oz of white milk", "2 oz of white sugar"),
    isRecipePublic = true,
    areCommentsDisabled = false,
)


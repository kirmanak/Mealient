package gq.kirmanak.mealient.extensions

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.test.RecipeImplTestData.CAKE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.test.RecipeImplTestData.CAKE_RECIPE_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.MILK_RECIPE_INGREDIENT_INFO
import gq.kirmanak.mealient.test.RecipeImplTestData.MILK_RECIPE_INGREDIENT_RESPONSE_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.MILK_RECIPE_INGREDIENT_RESPONSE_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.MIX_CAKE_RECIPE_INSTRUCTION_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.MIX_INSTRUCTION
import gq.kirmanak.mealient.test.RecipeImplTestData.MIX_RECIPE_INSTRUCTION_INFO
import gq.kirmanak.mealient.test.RecipeImplTestData.MIX_RECIPE_INSTRUCTION_RESPONSE_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.MIX_RECIPE_INSTRUCTION_RESPONSE_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_ADD_RECIPE_REQUEST_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_CREATE_RECIPE_REQUEST_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_RECIPE_DRAFT
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_RECIPE_RESPONSE_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_RECIPE_RESPONSE_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_RECIPE_SUMMARY_RESPONSE_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_RECIPE_SUMMARY_RESPONSE_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_UPDATE_RECIPE_REQUEST_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.RECIPE_SUMMARY_PORRIDGE_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.RECIPE_SUMMARY_PORRIDGE_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.SUGAR_INGREDIENT
import gq.kirmanak.mealient.test.RecipeImplTestData.VERSION_INFO_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.VERSION_INFO_V1
import gq.kirmanak.mealient.test.RecipeImplTestData.VERSION_RESPONSE_V0
import gq.kirmanak.mealient.test.RecipeImplTestData.VERSION_RESPONSE_V1
import org.junit.Test

class ModelMappingsTest : BaseUnitTest() {

    @Test
    fun `when toAddRecipeRequest then fills fields correctly`() {
        assertThat(PORRIDGE_RECIPE_DRAFT.toAddRecipeInfo()).isEqualTo(PORRIDGE_ADD_RECIPE_INFO)
    }

    @Test
    fun `when toDraft then fills fields correctly`() {
        assertThat(PORRIDGE_ADD_RECIPE_INFO.toDraft()).isEqualTo(PORRIDGE_RECIPE_DRAFT)
    }

    @Test
    fun `when full recipe info to entity expect correct entity`() {
        assertThat(CAKE_FULL_RECIPE_INFO.toRecipeEntity()).isEqualTo(CAKE_RECIPE_ENTITY)
    }

    @Test
    fun `when ingredient info to entity expect correct entity`() {
        val actual = SUGAR_INGREDIENT.toRecipeIngredientEntity("1")
        assertThat(actual).isEqualTo(CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY)
    }

    @Test
    fun `when instruction info to entity expect correct entity`() {
        val actual = MIX_INSTRUCTION.toRecipeInstructionEntity("1")
        assertThat(actual).isEqualTo(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY)
    }

    @Test
    fun `when summary v0 to info expect correct info`() {
        val actual = PORRIDGE_RECIPE_SUMMARY_RESPONSE_V0.toRecipeSummaryInfo()
        assertThat(actual).isEqualTo(RECIPE_SUMMARY_PORRIDGE_V0)
    }

    @Test
    fun `when summary v1 to info expect correct info`() {
        val actual = PORRIDGE_RECIPE_SUMMARY_RESPONSE_V1.toRecipeSummaryInfo()
        assertThat(actual).isEqualTo(RECIPE_SUMMARY_PORRIDGE_V1)
    }

    @Test
    fun `when summary info to entity expect correct entity`() {
        val actual = RECIPE_SUMMARY_PORRIDGE_V0.toRecipeSummaryEntity(isFavorite = false)
        assertThat(actual).isEqualTo(PORRIDGE_RECIPE_SUMMARY_ENTITY)
    }

    @Test
    fun `when version response v0 to info expect correct info`() {
        assertThat(VERSION_RESPONSE_V0.toVersionInfo()).isEqualTo(VERSION_INFO_V0)
    }

    @Test
    fun `when version response v1 to info expect correct info`() {
        assertThat(VERSION_RESPONSE_V1.toVersionInfo()).isEqualTo(VERSION_INFO_V1)
    }

    @Test
    fun `when recipe ingredient response v0 to info expect correct info`() {
        val actual = MILK_RECIPE_INGREDIENT_RESPONSE_V0.toRecipeIngredientInfo()
        assertThat(actual).isEqualTo(MILK_RECIPE_INGREDIENT_INFO)
    }

    @Test
    fun `when recipe ingredient response v1 to info expect correct info`() {
        val actual = MILK_RECIPE_INGREDIENT_RESPONSE_V1.toRecipeIngredientInfo()
        assertThat(actual).isEqualTo(MILK_RECIPE_INGREDIENT_INFO)
    }

    @Test
    fun `when recipe instruction response v0 to info expect correct info`() {
        val actual = MIX_RECIPE_INSTRUCTION_RESPONSE_V0.toRecipeInstructionInfo()
        assertThat(actual).isEqualTo(MIX_RECIPE_INSTRUCTION_INFO)
    }

    @Test
    fun `when recipe instruction response v1 to info expect correct info`() {
        val actual = MIX_RECIPE_INSTRUCTION_RESPONSE_V1.toRecipeInstructionInfo()
        assertThat(actual).isEqualTo(MIX_RECIPE_INSTRUCTION_INFO)
    }

    @Test
    fun `when recipe response v0 to info expect correct info`() {
        val actual = PORRIDGE_RECIPE_RESPONSE_V0.toFullRecipeInfo()
        assertThat(actual).isEqualTo(PORRIDGE_FULL_RECIPE_INFO)
    }

    @Test
    fun `when recipe response v1 to info expect correct info`() {
        val actual = PORRIDGE_RECIPE_RESPONSE_V1.toFullRecipeInfo()
        assertThat(actual).isEqualTo(PORRIDGE_FULL_RECIPE_INFO)
    }

    @Test
    fun `when add recipe info to request v0 expect correct request`() {
        val actual = PORRIDGE_ADD_RECIPE_INFO.toV0Request()
        assertThat(actual).isEqualTo(PORRIDGE_ADD_RECIPE_REQUEST_V0)
    }

    @Test
    fun `when add recipe info to create request v1 expect correct request`() {
        val actual = PORRIDGE_ADD_RECIPE_INFO.toV1CreateRequest()
        assertThat(actual).isEqualTo(PORRIDGE_CREATE_RECIPE_REQUEST_V1)
    }

    @Test
    fun `when add recipe info to update request v1 expect correct request`() {
        val actual = PORRIDGE_ADD_RECIPE_INFO.toV1UpdateRequest()
        assertThat(actual).isEqualTo(PORRIDGE_UPDATE_RECIPE_REQUEST_V1)
    }
}
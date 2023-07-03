package gq.kirmanak.mealient.model_mapper

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.database.CAKE_RECIPE_ENTITY
import gq.kirmanak.mealient.database.CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY
import gq.kirmanak.mealient.database.MIX_CAKE_RECIPE_INSTRUCTION_ENTITY
import gq.kirmanak.mealient.database.PORRIDGE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.datasource_test.CAKE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.MILK_RECIPE_INGREDIENT_INFO
import gq.kirmanak.mealient.datasource_test.MILK_RECIPE_INGREDIENT_RESPONSE_V0
import gq.kirmanak.mealient.datasource_test.MILK_RECIPE_INGREDIENT_RESPONSE_V1
import gq.kirmanak.mealient.datasource_test.MIX_INSTRUCTION
import gq.kirmanak.mealient.datasource_test.MIX_RECIPE_INSTRUCTION_INFO
import gq.kirmanak.mealient.datasource_test.MIX_RECIPE_INSTRUCTION_RESPONSE_V0
import gq.kirmanak.mealient.datasource_test.MIX_RECIPE_INSTRUCTION_RESPONSE_V1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_REQUEST_V0
import gq.kirmanak.mealient.datasource_test.PORRIDGE_CREATE_RECIPE_REQUEST_V1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_RESPONSE_V0
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_RESPONSE_V1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_SUMMARY_RESPONSE_V0
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_SUMMARY_RESPONSE_V1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_UPDATE_RECIPE_REQUEST_V1
import gq.kirmanak.mealient.datasource_test.RECIPE_SUMMARY_PORRIDGE_V0
import gq.kirmanak.mealient.datasource_test.RECIPE_SUMMARY_PORRIDGE_V1
import gq.kirmanak.mealient.datasource_test.SUGAR_INGREDIENT
import gq.kirmanak.mealient.datasource_test.VERSION_INFO_V0
import gq.kirmanak.mealient.datasource_test.VERSION_INFO_V1
import gq.kirmanak.mealient.datasource_test.VERSION_RESPONSE_V0
import gq.kirmanak.mealient.datasource_test.VERSION_RESPONSE_V1
import gq.kirmanak.mealient.datastore_test.PORRIDGE_RECIPE_DRAFT
import gq.kirmanak.mealient.test.BaseUnitTest
import org.junit.Before
import org.junit.Test

class ModelMappingsTest : BaseUnitTest() {

    private lateinit var subject: ModelMapper

    @Before
    override fun setUp() {
        super.setUp()
        subject = ModelMapperImpl()
    }

    @Test
    fun `when toAddRecipeRequest then fills fields correctly`() {
        assertThat(subject.toAddRecipeInfo(PORRIDGE_RECIPE_DRAFT)).isEqualTo(
            PORRIDGE_ADD_RECIPE_INFO
        )
    }

    @Test
    fun `when toDraft then fills fields correctly`() {
        assertThat(subject.toDraft(PORRIDGE_ADD_RECIPE_INFO)).isEqualTo(PORRIDGE_RECIPE_DRAFT)
    }

    @Test
    fun `when full recipe info to entity expect correct entity`() {
        assertThat(subject.toRecipeEntity(CAKE_FULL_RECIPE_INFO)).isEqualTo(CAKE_RECIPE_ENTITY)
    }

    @Test
    fun `when ingredient info to entity expect correct entity`() {
        val actual = subject.toRecipeIngredientEntity(SUGAR_INGREDIENT, "1")
        assertThat(actual).isEqualTo(CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY)
    }

    @Test
    fun `when instruction info to entity expect correct entity`() {
        val actual = subject.toRecipeInstructionEntity(MIX_INSTRUCTION, "1")
        assertThat(actual).isEqualTo(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY)
    }

    @Test
    fun `when summary v0 to info expect correct info`() {
        val actual = subject.toRecipeSummaryInfo(PORRIDGE_RECIPE_SUMMARY_RESPONSE_V0)
        assertThat(actual).isEqualTo(RECIPE_SUMMARY_PORRIDGE_V0)
    }

    @Test
    fun `when summary v1 to info expect correct info`() {
        val actual = subject.toRecipeSummaryInfo(PORRIDGE_RECIPE_SUMMARY_RESPONSE_V1)
        assertThat(actual).isEqualTo(RECIPE_SUMMARY_PORRIDGE_V1)
    }

    @Test
    fun `when summary info to entity expect correct entity`() {
        val actual = subject.toRecipeSummaryEntity(RECIPE_SUMMARY_PORRIDGE_V0, isFavorite = false)
        assertThat(actual).isEqualTo(PORRIDGE_RECIPE_SUMMARY_ENTITY)
    }

    @Test
    fun `when version response v0 to info expect correct info`() {
        assertThat(subject.toVersionInfo(VERSION_RESPONSE_V0)).isEqualTo(VERSION_INFO_V0)
    }

    @Test
    fun `when version response v1 to info expect correct info`() {
        assertThat(subject.toVersionInfo(VERSION_RESPONSE_V1)).isEqualTo(VERSION_INFO_V1)
    }

    @Test
    fun `when recipe ingredient response v0 to info expect correct info`() {
        val actual = subject.toRecipeIngredientInfo(MILK_RECIPE_INGREDIENT_RESPONSE_V0)
        assertThat(actual).isEqualTo(MILK_RECIPE_INGREDIENT_INFO)
    }

    @Test
    fun `when recipe ingredient response v1 to info expect correct info`() {
        val actual = subject.toRecipeIngredientInfo(MILK_RECIPE_INGREDIENT_RESPONSE_V1)
        assertThat(actual).isEqualTo(MILK_RECIPE_INGREDIENT_INFO)
    }

    @Test
    fun `when recipe instruction response v0 to info expect correct info`() {
        val actual = subject.toRecipeInstructionInfo(MIX_RECIPE_INSTRUCTION_RESPONSE_V0)
        assertThat(actual).isEqualTo(MIX_RECIPE_INSTRUCTION_INFO)
    }

    @Test
    fun `when recipe instruction response v1 to info expect correct info`() {
        val actual = subject.toRecipeInstructionInfo(MIX_RECIPE_INSTRUCTION_RESPONSE_V1)
        assertThat(actual).isEqualTo(MIX_RECIPE_INSTRUCTION_INFO)
    }

    @Test
    fun `when recipe response v0 to info expect correct info`() {
        val actual = subject.toFullRecipeInfo(PORRIDGE_RECIPE_RESPONSE_V0)
        assertThat(actual).isEqualTo(PORRIDGE_FULL_RECIPE_INFO)
    }

    @Test
    fun `when recipe response v1 to info expect correct info`() {
        val actual = subject.toFullRecipeInfo(PORRIDGE_RECIPE_RESPONSE_V1)
        assertThat(actual).isEqualTo(PORRIDGE_FULL_RECIPE_INFO)
    }

    @Test
    fun `when add recipe info to request v0 expect correct request`() {
        val actual = subject.toV0Request(PORRIDGE_ADD_RECIPE_INFO)
        assertThat(actual).isEqualTo(PORRIDGE_ADD_RECIPE_REQUEST_V0)
    }

    @Test
    fun `when add recipe info to create request v1 expect correct request`() {
        val actual = subject.toV1CreateRequest(PORRIDGE_ADD_RECIPE_INFO)
        assertThat(actual).isEqualTo(PORRIDGE_CREATE_RECIPE_REQUEST_V1)
    }

    @Test
    fun `when add recipe info to update request v1 expect correct request`() {
        val actual = subject.toV1UpdateRequest(PORRIDGE_ADD_RECIPE_INFO)
        assertThat(actual).isEqualTo(PORRIDGE_UPDATE_RECIPE_REQUEST_V1)
    }
}
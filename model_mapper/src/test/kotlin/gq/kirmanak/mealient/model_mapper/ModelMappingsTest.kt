package gq.kirmanak.mealient.model_mapper

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.database.CAKE_RECIPE_ENTITY
import gq.kirmanak.mealient.database.CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY
import gq.kirmanak.mealient.database.MIX_CAKE_RECIPE_INSTRUCTION_ENTITY
import gq.kirmanak.mealient.datasource_test.CAKE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.MILK_RECIPE_INGREDIENT_INFO
import gq.kirmanak.mealient.datasource_test.MILK_RECIPE_INGREDIENT_RESPONSE
import gq.kirmanak.mealient.datasource_test.MIX_INSTRUCTION
import gq.kirmanak.mealient.datasource_test.MIX_RECIPE_INSTRUCTION_INFO
import gq.kirmanak.mealient.datasource_test.MIX_RECIPE_INSTRUCTION_RESPONSE
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_CREATE_RECIPE_REQUEST
import gq.kirmanak.mealient.datasource_test.PORRIDGE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_RESPONSE
import gq.kirmanak.mealient.datasource_test.PORRIDGE_UPDATE_RECIPE_REQUEST
import gq.kirmanak.mealient.datasource_test.SUGAR_INGREDIENT
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
    fun `when recipe ingredient response to info expect correct info`() {
        val actual = subject.toRecipeIngredientInfo(MILK_RECIPE_INGREDIENT_RESPONSE)
        assertThat(actual).isEqualTo(MILK_RECIPE_INGREDIENT_INFO)
    }

    @Test
    fun `when recipe instruction response to info expect correct info`() {
        val actual = subject.toRecipeInstructionInfo(MIX_RECIPE_INSTRUCTION_RESPONSE)
        assertThat(actual).isEqualTo(MIX_RECIPE_INSTRUCTION_INFO)
    }

    @Test
    fun `when recipe response to info expect correct info`() {
        val actual = subject.toFullRecipeInfo(PORRIDGE_RECIPE_RESPONSE)
        assertThat(actual).isEqualTo(PORRIDGE_FULL_RECIPE_INFO)
    }

    @Test
    fun `when add recipe info to create request expect correct request`() {
        val actual = subject.toCreateRequest(PORRIDGE_ADD_RECIPE_INFO)
        assertThat(actual).isEqualTo(PORRIDGE_CREATE_RECIPE_REQUEST)
    }

    @Test
    fun `when add recipe info to update request expect correct request`() {
        val actual = subject.toUpdateRequest(PORRIDGE_ADD_RECIPE_INFO)
        assertThat(actual).isEqualTo(PORRIDGE_UPDATE_RECIPE_REQUEST)
    }
}
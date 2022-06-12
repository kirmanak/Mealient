package gq.kirmanak.mealient.data.add.impl

import androidx.datastore.core.DataStore
import gq.kirmanak.mealient.data.add.AddRecipeStorage
import gq.kirmanak.mealient.data.add.models.AddRecipeInput
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeStorageImpl @Inject constructor(
    private val dataStore: DataStore<AddRecipeInput>,
) : AddRecipeStorage {

    override val updates: Flow<AddRecipeRequest>
        get() = dataStore.data.map { AddRecipeRequest(it) }

    override suspend fun save(addRecipeRequest: AddRecipeRequest) {
        Timber.v("saveRecipeInput() called with: addRecipeRequest = $addRecipeRequest")
        dataStore.updateData { addRecipeRequest.toInput() }
    }

    override suspend fun clear() {
        Timber.v("clearRecipeInput() called")
        dataStore.updateData { AddRecipeInput.getDefaultInstance() }
    }
}
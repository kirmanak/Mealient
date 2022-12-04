package gq.kirmanak.mealient.ui.recipes.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.databinding.ViewHolderIngredientBinding
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.recipes.info.RecipeIngredientsAdapter.RecipeIngredientViewHolder
import javax.inject.Inject
import javax.inject.Singleton

class RecipeIngredientsAdapter private constructor(
    private val recipeIngredientViewHolderFactory: RecipeIngredientViewHolder.Factory,
    private val logger: Logger,
    private val disableAmounts: Boolean,
) : ListAdapter<RecipeIngredientEntity, RecipeIngredientViewHolder>(RecipeIngredientDiffCallback) {

    @Singleton
    class Factory @Inject constructor(
        private val recipeIngredientViewHolderFactory: RecipeIngredientViewHolder.Factory,
        private val logger: Logger,
    ) {
        fun build(disableAmounts: Boolean) = RecipeIngredientsAdapter(
            recipeIngredientViewHolderFactory = recipeIngredientViewHolderFactory,
            logger = logger,
            disableAmounts = disableAmounts,
        )
    }

    class RecipeIngredientViewHolder private constructor(
        private val binding: ViewHolderIngredientBinding,
        private val logger: Logger,
        private val disableAmounts: Boolean,
    ) : RecyclerView.ViewHolder(binding.root) {

        @Singleton
        class Factory @Inject constructor(
            private val logger: Logger,
        ) {

            fun build(
                binding: ViewHolderIngredientBinding,
                disableAmounts: Boolean,
            ) = RecipeIngredientViewHolder(
                binding = binding,
                logger = logger,
                disableAmounts = disableAmounts,
            )
        }

        fun bind(item: RecipeIngredientEntity) {
            logger.v { "bind() called with: item = $item" }
            binding.checkBox.text = if (disableAmounts) {
                item.note
            } else {
                val builder = StringBuilder()
                item.quantity?.let { builder.append("${it.formatUsingMediantMethod()} ") }
                item.unit?.let { builder.append("$it ") }
                item.food?.let { builder.append("$it ") }
                builder.append(item.note)
                builder.toString().trim()
            }
        }
    }

    private object RecipeIngredientDiffCallback : DiffUtil.ItemCallback<RecipeIngredientEntity>() {
        override fun areItemsTheSame(
            oldItem: RecipeIngredientEntity, newItem: RecipeIngredientEntity
        ): Boolean = oldItem.localId == newItem.localId

        override fun areContentsTheSame(
            oldItem: RecipeIngredientEntity, newItem: RecipeIngredientEntity
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientViewHolder {
        logger.v { "onCreateViewHolder() called with: parent = $parent, viewType = $viewType" }
        val inflater = LayoutInflater.from(parent.context)
        return recipeIngredientViewHolderFactory.build(
            ViewHolderIngredientBinding.inflate(inflater, parent, false),
            disableAmounts,
        )
    }

    override fun onBindViewHolder(holder: RecipeIngredientViewHolder, position: Int) {
        logger.v { "onBindViewHolder() called with: holder = $holder, position = $position" }
        val item = getItem(position)
        logger.d { "onBindViewHolder: item is $item" }
        holder.bind(item)
    }
}

private fun Double.formatUsingMediantMethod(d: Int = 10, mixed: Boolean = true): String {
    val triple = mediantMethod(d, mixed)
    return when {
        triple.second == 0 -> "${triple.first}"
        triple.first == 0 -> "${triple.second}/${triple.third}"
        else -> "${triple.first} ${triple.second}/${triple.third}"
    }
}

/**
 * Rational approximation to a floating point number with bounded denominator using Mediant Method.
 * For example, 333/1000 will become [0, 1, 3] (1/3), 1414/1000 will be [1, 2, 5] (1 2/5).
 * Uses algorithm from this npm package - https://www.npmjs.com/package/frac
 * Can be seen here https://github.com/SheetJS/frac/blob/d07f3c99c7dc059fb47d391bcb3da80f4956608e/frac.js
 * @receiver - number that needs to be approximated
 * @param d - maximum denominator (i.e. if 10 then 17/20 will be 4/5, if 20 then 17/20).
 * @param mixed - if true returns a mixed fraction otherwise improper (i.e. "7/5" or "1 2/5")
 */
@VisibleForTesting
fun Double.mediantMethod(d: Int = 10, mixed: Boolean = true): Triple<Int, Int, Int> {
    val x = this
    var n1 = x.toInt()
    var d1 = 1
    var n2 = n1 + 1
    var d2 = 1
    if (x != n1.toDouble()) {
        while (d1 <= d && d2 <= d) {
            val m = (n1 + n2).toDouble() / (d1 + d2)
            when {
                x == m -> {
                    when {
                        d1 + d2 <= d -> {
                            d1 += d2
                            n1 += n2
                            d2 = d + 1
                        }
                        d1 > d2 -> d2 = d + 1
                        else -> d1 = d + 1
                    }
                    break
                }
                x < m -> {
                    n2 += n1
                    d2 += d1
                }
                else -> {
                    n1 += n2
                    d1 += d2
                }
            }
        }
    }
    if (d1 > d) {
        d1 = d2
        n1 = n2
    }
    if (!mixed) return Triple(0, n1, d1)
    val q = (n1.toDouble() / d1).toInt()
    return Triple(q, n1 - q * d1, d1)
}
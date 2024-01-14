import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gq.kirmanak.mealient.Versions
import gq.kirmanak.mealient.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlinx.kover")
            }

            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = Versions.TARGET_SDK_VERSION
            }
        }
    }
}
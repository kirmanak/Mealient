import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gq.kirmanak.mealient.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<BaseAppModuleExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}
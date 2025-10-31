plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // ✅ keep the alias (don’t specify version manually)
    alias(libs.plugins.google.gms.google.services) apply false
}

package app.medium.patches.medium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch
import app.morphe.patcher.patch.stringOption
import app.medium.patches.shared.Constants.COMPATIBILITY_MEDIUM

private const val EXTENSION_CLASS = "Lapp/medium/extension/MediumPatchExtension;"

@Suppress("unused")
val translatePatch = patch(
    name = "Translate articles",
    description = "Adds dynamic translation capability to Medium articles using Google Translate API.",
    default = true
) {
    compatibleWith(COMPATIBILITY_MEDIUM)

    extendWith("extensions/extension.mpe")

    val targetLanguage by stringOption(
        key = "targetLanguage",
        title = "Target Language",
        description = "Language code to translate articles to (e.g., ru, es, de, fr, zh).",
        default = "ru"
    )

    execute {
        // Safely try to match Activity onResume fingerprint
        ActivityOnResumeFingerprint.matchOrNull()?.method?.addInstructions(
            0,
            """
                invoke-static {p0}, $EXTENSION_CLASS->onActivityResumed(Landroid/app/Activity;)V
            """
        )
    }
}
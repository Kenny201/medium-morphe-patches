package app.medium.patches.medium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch
import app.medium.patches.shared.Constants.COMPATIBILITY_MEDIUM

private const val EXTENSION_CLASS = "Lapp/medium/extension/PaywallBypassHelper;"

@Suppress("unused")
val paywallBypassPatch = patch(
    name = "Bypass paywall",
    description = "Bypasses Medium paywall for member-only articles via Freedium proxy.",
    default = true
) {
    compatibleWith(COMPATIBILITY_MEDIUM)

    extendWith("extensions/extension.mpe")

    execute {
        // If article model has lock check, bypass it
        ArticleModelFingerprint.matchOrNull()?.method?.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """
        )
    }
}
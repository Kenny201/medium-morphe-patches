package app.medium.patches.medium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string

/**
 * Robust fingerprint to match onCreate in any Activity class within the Medium app.
 */
object ActivityOnCreateFingerprint : Fingerprint(
    name = "onCreate",
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
    filters = listOf(
        methodCall(
            definingClass = "Landroid/app/Activity;",
            name = "onCreate"
        )
    )
)

/**
 * Fingerprint to match article loading or model parsing containing slug/title.
 */
object ArticleModelFingerprint : Fingerprint(
    definingClass = "L",
    name = "parse",
    filters = listOf(
        string("slug"),
        string("title")
    )
)
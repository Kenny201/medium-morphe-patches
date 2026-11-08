package app.medium.patches.medium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

/**
 * Fingerprint to hook Activity.onResume in Medium application.
 */
object ActivityOnResumeFingerprint : Fingerprint(
    name = "onResume",
    accessFlags = listOf(AccessFlags.PROTECTED),
    returnType = "V",
    parameters = emptyList(),
    filters = listOf(
        methodCall(
            definingClass = "Landroid/app/Activity;",
            name = "onResume",
            returnType = "V"
        )
    )
)

/**
 * Fingerprint to match Medium article paywall / locked status check.
 */
object PaywallCheckFingerprint : Fingerprint(
    name = "isLocked",
    returnType = "Z",
    parameters = emptyList(),
    filters = listOf(
        string("isLocked"),
        methodCall(
            name = "equals",
            returnType = "Z"
        ),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately())
    )
)

/**
 * Fingerprint matching Article viewer Activity or View in Medium.
 */
object ArticleViewFingerprint : Fingerprint(
    definingClass = "Lcom/medium/reader/",
    name = "onArticleLoaded",
    returnType = "V",
    filters = listOf(
        string("article_id")
    )
)
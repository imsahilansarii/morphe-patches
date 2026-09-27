package com.dmoniak.patches.shazam

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.ExternalLabel
import com.dmoniak.patches.shared.Constants.COMPATIBILITY_SHAZAM

private val shazamPackageCheckFingerprint = Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf("Ljava/lang/String;"),
    filters = listOf(
        methodCall(
            "Landroid/content/pm/PackageManager;",
            "getApplicationInfo",
            "(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;"
        )
    )
)

@Suppress("unused")
val shazamMorpheYouTubeIntegrationPatch = bytecodePatch(
    name = "Morphe YouTube Integration - Shazam",
    description = "Allows Shazam to recognize Morphe-patched YouTube and YouTube Music.",
) {
    compatibleWith(COMPATIBILITY_SHAZAM)

    execute {
        val method = shazamPackageCheckFingerprint.method
        val match = shazamPackageCheckFingerprint.instructionMatches.first()

        val index = match.index

        method.addInstructionsWithLabels(
            index,
            """
            const-string v2, "com.google.android.youtube"
            invoke-virtual {p1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
            move-result v2
            if-eqz v2, :check_youtube_music

            const-string p1, "app.morphe.android.youtube"
            goto :continue_package_check

            :check_youtube_music
            const-string v2, "com.google.android.apps.youtube.music"
            invoke-virtual {p1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
            move-result v2
            if-eqz v2, :continue_package_check

            const-string p1, "app.morphe.android.apps.youtube.music"

            :continue_package_check
            """.trimIndent(),
            ExternalLabel(
                "continue_package_check",
                method.getInstruction(index)
            )
        )
    }
}

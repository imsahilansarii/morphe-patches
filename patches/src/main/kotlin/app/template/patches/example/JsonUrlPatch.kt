package app.template.patches.example

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption
import app.template.patches.shared.Constants.COMPATIBILITY_EXAMPLE

@Suppress("unused")
val jsonUrlPatch = bytecodePatch(
    name = "Universal OTA",
    description = "Configures the OTA JSON URL used for update checks.",
    default = true
) {

    val otaJsonUrlOption = stringOption(
        key = "otaJsonUrl",
        default = "",
        title = "OTA JSON URL",
        description = "Enter the URL of the JSON file used for OTA update checks.",
        required = true
    )

    execute {
        val otaJsonUrl = otaJsonUrlOption.value!!

        println("Universal OTA JSON URL: $otaJsonUrl")
    }
}
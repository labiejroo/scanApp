/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandit.datacapture.inventoryauditsample.models

import com.scandit.datacapture.barcode.data.Symbology
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTracking
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTracking.Companion.forDataCaptureContext
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingScenario.A
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingSettings
import com.scandit.datacapture.core.capture.DataCaptureContext
import com.scandit.datacapture.core.capture.DataCaptureContext.Companion.forLicenseKey
import com.scandit.datacapture.core.source.Camera
import com.scandit.datacapture.core.source.Camera.Companion.getDefaultCamera
import com.scandit.datacapture.core.source.VideoResolution
import com.scandit.datacapture.core.ui.style.Brush

object DataCaptureManager {
    private const val SCANDIT_LICENSE_KEY = "AYUfuji0Jw+6JurcaQWiRq08XVOgArB9hlGpLPIXdlPfaV428kWHIQdqFttIUQ5X82XeCuI8rlj3a1iKRi8a7IgWxrSQRue9wh9t7ydXwl8IenKwKHOR0B9Feg0rbZH9G3B8kJNfwg1NHrDfqXFQLvp1/v1Zfrzo1k7+tFZHxbLweqPJS1yI4A9ETfzRVjP+a09cXpNJzDO3bCYApno66/xSYcPFZkEV5UDiTAB/UtQJbiVuRV4xWhVf1fNjZzan1UTrClxSeXYGchpxl33FjkBrV5WYU1dS4AoPekxqmxvqZJDrfXTwgFFLD8/xWkGZ/1nNdJ9fvdSeYAImWVdEnPRAYgJHKhwIeHZgEMZxgfjMRex91UcLZmVptFHJUsKDj2z1vZMvv7G7dVaNGX9NCQlRTqGTRJqPklRUNfoT6UexdCTv4GqKvAdNkxq0WXl2Z3GBrZlq8qUZFhCZU0eFwoFQrcHeYxOJLX0JluFWayNMX3E1qXMd8BBzkNilT0DM+DGI6+8+bwmxQGCNlwLnOT2MNtKtKack4dtqW4fcuuMx7QBu1oQtj+ZChplOft9pTlcLScCLJsaqF4RJ99BDE/a6e/u3AfP6K0QtUkxSRIi8CKjGOfXMKy7shjdlkcEnoyhcFKmtdNq6J2rdoW5sR2qhSQEQ4E9/4i4Z949SsgE/TzL3nkiPrO39wkwjI+UTuRkdu9dbym+KehSBYcGapNzzODBl5U7/bic95cyakMYL52Y4cXB3S+wBgX9GzoK8qajXMSxiEruYDimUIl5BjbQ7zKcbcK48IewM+COUnrlApO19kVTui+mza//Wrv05L8pRV5pZuP8pVflZT8reCZswfI2IF/P4RThJCRQ6i566v6xHfHBFPnV/ifexVBUOSUDVdR4WHuGLXWYG3MIJl1VS2B2gx6a4ZZrZPwmVodju0EToeGidh4O1nr8ShGi27jV7pwrrb45YXpRLmp9Hi3ESrq/Mu8BYLnNQx/5G8g4Y9gXHfkiQNni3JBL1eOLLxcj+SQCZTvhpj4PkUitiS/CoRIETdnhiE+bwdRQqgPkRLbPtV+5Cm8mXUQxoF+bKKT9bNAvojScMKhnbC0xEMjKkeV3PwVlLO5zUe1IEZ1M4Wxtlcss+yKbIh25VBjFgrR6jUYtk0ok+7PUJk3rR6W/Lr34X0cfa2sRWFoJ1Ct2ZdRqZv7arBh7QwIVU+NQ="

    val barcodeTracking: BarcodeTracking
    val dataCaptureContext: DataCaptureContext
    val camera: Camera
    val defaultBrush = Brush.transparent()

    init {
        // The barcode tracking process is configured through barcode tracking settings
        // which are then applied to the barcode tracking instance that manages barcode recognition and tracking.
        val barcodeTrackingSettings = BarcodeTrackingSettings.forScenario(A).apply {
            // The settings instance initially has all types of barcodes (symbologies) disabled.
            // For the purpose of this sample we enable a generous set of symbologies.
            // In your own app ensure that you only enable the symbologies that your app requires
            // as every additional enabled symbology has an impact on processing times.
            enableSymbology(Symbology.EAN13_UPCA, true)
            enableSymbology(Symbology.EAN8, true)
            enableSymbology(Symbology.UPCE, true)
        }

        val cameraSettings = BarcodeTracking.createRecommendedCameraSettings().apply {
            preferredResolution = VideoResolution.UHD4K
        }

        camera = getDefaultCamera(cameraSettings) ?: throw IllegalStateException(
                "Sample depends on a camera, which failed to initialize.")

        // Create data capture context using your license key and set the camera as the frame source.
        dataCaptureContext = forLicenseKey(SCANDIT_LICENSE_KEY).apply {
            setFrameSource(camera)
        }

        // Create new barcode tracking mode with the settings from above.
        barcodeTracking = forDataCaptureContext(dataCaptureContext, barcodeTrackingSettings)
    }
}
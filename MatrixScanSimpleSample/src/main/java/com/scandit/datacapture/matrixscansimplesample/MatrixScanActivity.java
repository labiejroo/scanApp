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

package com.scandit.datacapture.matrixscansimplesample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.scandit.datacapture.barcode.data.Symbology;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTracking;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingListener;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingSession;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingSettings;
import com.scandit.datacapture.barcode.tracking.data.TrackedBarcode;
import com.scandit.datacapture.barcode.tracking.ui.overlay.BarcodeTrackingBasicOverlay;
import com.scandit.datacapture.core.capture.DataCaptureContext;
import com.scandit.datacapture.core.data.FrameData;
import com.scandit.datacapture.core.source.Camera;
import com.scandit.datacapture.core.source.CameraSettings;
import com.scandit.datacapture.core.source.FrameSourceState;
import com.scandit.datacapture.core.source.VideoResolution;
import com.scandit.datacapture.core.ui.DataCaptureView;
import com.scandit.datacapture.matrixscansimplesample.data.ScanResult;

import java.util.HashSet;

public class MatrixScanActivity extends CameraPermissionActivity
        implements BarcodeTrackingListener {

    // Enter your Scandit License key here.
    // Your Scandit License key is available via your Scandit SDK web account.
    public static final String SCANDIT_LICENSE_KEY = "AYUfuji0Jw+6JurcaQWiRq08XVOgArB9hlGpLPIXdlPfaV428kWHIQdqFttIUQ5X82XeCuI8rlj3a1iKRi8a7IgWxrSQRue9wh9t7ydXwl8IenKwKHOR0B9Feg0rbZH9G3B8kJNfwg1NHrDfqXFQLvp1/v1Zfrzo1k7+tFZHxbLweqPJS1yI4A9ETfzRVjP+a09cXpNJzDO3bCYApno66/xSYcPFZkEV5UDiTAB/UtQJbiVuRV4xWhVf1fNjZzan1UTrClxSeXYGchpxl33FjkBrV5WYU1dS4AoPekxqmxvqZJDrfXTwgFFLD8/xWkGZ/1nNdJ9fvdSeYAImWVdEnPRAYgJHKhwIeHZgEMZxgfjMRex91UcLZmVptFHJUsKDj2z1vZMvv7G7dVaNGX9NCQlRTqGTRJqPklRUNfoT6UexdCTv4GqKvAdNkxq0WXl2Z3GBrZlq8qUZFhCZU0eFwoFQrcHeYxOJLX0JluFWayNMX3E1qXMd8BBzkNilT0DM+DGI6+8+bwmxQGCNlwLnOT2MNtKtKack4dtqW4fcuuMx7QBu1oQtj+ZChplOft9pTlcLScCLJsaqF4RJ99BDE/a6e/u3AfP6K0QtUkxSRIi8CKjGOfXMKy7shjdlkcEnoyhcFKmtdNq6J2rdoW5sR2qhSQEQ4E9/4i4Z949SsgE/TzL3nkiPrO39wkwjI+UTuRkdu9dbym+KehSBYcGapNzzODBl5U7/bic95cyakMYL52Y4cXB3S+wBgX9GzoK8qajXMSxiEruYDimUIl5BjbQ7zKcbcK48IewM+COUnrlApO19kVTui+mza//Wrv05L8pRV5pZuP8pVflZT8reCZswfI2IF/P4RThJCRQ6i566v6xHfHBFPnV/ifexVBUOSUDVdR4WHuGLXWYG3MIJl1VS2B2gx6a4ZZrZPwmVodju0EToeGidh4O1nr8ShGi27jV7pwrrb45YXpRLmp9Hi3ESrq/Mu8BYLnNQx/5G8g4Y9gXHfkiQNni3JBL1eOLLxcj+SQCZTvhpj4PkUitiS/CoRIETdnhiE+bwdRQqgPkRLbPtV+5Cm8mXUQxoF+bKKT9bNAvojScMKhnbC0xEMjKkeV3PwVlLO5zUe1IEZ1M4Wxtlcss+yKbIh25VBjFgrR6jUYtk0ok+7PUJk3rR6W/Lr34X0cfa2sRWFoJ1Ct2ZdRqZv7arBh7QwIVU+NQ=";

    public static final int REQUEST_CODE_SCAN_RESULTS = 1;

    private Camera camera;
    private BarcodeTracking barcodeTracking;
    private DataCaptureContext dataCaptureContext;

    private final HashSet<ScanResult> scanResults = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_scan);

        // Initialize and start the barcode recognition.
        initialize();

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (scanResults) {
                    // Show new screen displaying a list of all barcodes that have been scanned.
                    Intent intent = ResultsActivity.getIntent(MatrixScanActivity.this, scanResults);
                    startActivityForResult(intent, REQUEST_CODE_SCAN_RESULTS);
                }
            }
        });
    }

    private void initialize() {
        // Create data capture context using your license key.
        dataCaptureContext = DataCaptureContext.forLicenseKey(SCANDIT_LICENSE_KEY);

        // Use the recommended camera settings for the BarcodeTracking mode.
        CameraSettings cameraSettings = BarcodeTracking.createRecommendedCameraSettings();
        // Adjust camera settings - set Full HD resolution.
        cameraSettings.setPreferredResolution(VideoResolution.FULL_HD);
        // Use the default camera and set it as the frame source of the context.
        // The camera is off by default and must be turned on to start streaming frames to the data
        // capture context for recognition.
        // See resumeFrameSource and pauseFrameSource below.
        camera = Camera.getDefaultCamera(cameraSettings);
        if (camera != null) {
            dataCaptureContext.setFrameSource(camera);
        } else {
            throw new IllegalStateException("Sample depends on a camera, which failed to initialize.");
        }

        // The barcode tracking process is configured through barcode tracking settings
        // which are then applied to the barcode tracking instance that manages barcode tracking.
        BarcodeTrackingSettings barcodeTrackingSettings = new BarcodeTrackingSettings();

        // The settings instance initially has all types of barcodes (symbologies) disabled.
        // For the purpose of this sample we enable a very generous set of symbologies.
        // In your own app ensure that you only enable the symbologies that your app requires
        // as every additional enabled symbology has an impact on processing times.
        HashSet<Symbology> symbologies = new HashSet<>();
        symbologies.add(Symbology.EAN13_UPCA);
        symbologies.add(Symbology.EAN8);
        symbologies.add(Symbology.UPCE);
        symbologies.add(Symbology.CODE39);
        symbologies.add(Symbology.CODE128);

        barcodeTrackingSettings.enableSymbologies(symbologies);

        // Create barcode tracking and attach to context.
        barcodeTracking =
                BarcodeTracking.forDataCaptureContext(dataCaptureContext, barcodeTrackingSettings);

        // Register self as a listener to get informed of tracked barcodes.
        barcodeTracking.addListener(this);

        // To visualize the on-going barcode tracking process on screen, setup a data capture view
        // that renders the camera preview. The view must be connected to the data capture context.
        DataCaptureView dataCaptureView = DataCaptureView.newInstance(this, dataCaptureContext);

        // Add a barcode tracking overlay to the data capture view to render the tracked barcodes on
        // top of the video preview. This is optional, but recommended for better visual feedback.
        BarcodeTrackingBasicOverlay.newInstance(barcodeTracking, dataCaptureView);

        // Add the DataCaptureView to the container.
        FrameLayout container = findViewById(R.id.data_capture_view_container);
        container.addView(dataCaptureView);
    }

    @Override
    protected void onPause() {
        pauseFrameSource();
        super.onPause();
    }

    private void pauseFrameSource() {
        // Switch camera off to stop streaming frames.
        // The camera is stopped asynchronously and will take some time to completely turn off.
        // Until it is completely stopped, it is still possible to receive further results, hence
        // it's a good idea to first disable barcode tracking as well.
        barcodeTracking.setEnabled(false);
        camera.switchToDesiredState(FrameSourceState.OFF, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission();
    }

    @Override
    public void onCameraPermissionGranted() {
        resumeFrameSource();
    }

    private void resumeFrameSource() {
        // Switch camera on to start streaming frames.
        // The camera is started asynchronously and will take some time to completely turn on.
        barcodeTracking.setEnabled(true);
        camera.switchToDesiredState(FrameSourceState.ON, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN_RESULTS
                && resultCode == ResultsActivity.RESULT_CODE_CLEAN) {
            synchronized (scanResults) {
                scanResults.clear();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onObservationStarted(@NonNull BarcodeTracking barcodeTracking) {
        // Nothing to do.
    }

    @Override
    public void onObservationStopped(@NonNull BarcodeTracking barcodeTracking) {
        // Nothing to do.
    }

    // This function is called whenever objects are updated and it's the right place to react to
    // the tracking results.
    @Override
    public void onSessionUpdated(
            @NonNull BarcodeTracking mode,
            @NonNull BarcodeTrackingSession session,
            @NonNull FrameData data
    ) {
        synchronized (scanResults) {
            for (TrackedBarcode trackedBarcode : session.getAddedTrackedBarcodes()) {
                scanResults.add(new ScanResult(trackedBarcode.getBarcode()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        dataCaptureContext.removeMode(barcodeTracking);
        super.onDestroy();
    }
}

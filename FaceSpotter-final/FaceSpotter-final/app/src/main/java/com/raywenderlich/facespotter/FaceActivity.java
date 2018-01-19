/*
 * Copyright (c) 2017 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish, 
 * distribute, sublicense, create a derivative work, and/or sell copies of the 
 * Software in any work that is designed, intended, or marketed for pedagogical or 
 * instructional purposes related to programming, coding, application development, 
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works, 
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.raywenderlich.facespotter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.raywenderlich.facespotter.ui.camera.CameraSourcePreview;
import com.raywenderlich.facespotter.ui.camera.GraphicOverlay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class FaceActivity extends AppCompatActivity {

  private static final String TAG = "FaceActivity";

  private static final int RC_HANDLE_GMS = 9001;
  // permission request codes need to be < 256
  private static final int RC_HANDLE_CAMERA_PERM = 255;

  private CameraSource mCameraSource = null;
  private CameraSourcePreview mPreview;
  private GraphicOverlay mGraphicOverlay;
  private boolean mIsFrontFacing = true;
 private  ImageButton takepicher;
  private FrameLayout capcher,capcher1;

  // Activity event handlers
  // =======================

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate called.");

    setContentView(R.layout.activity_face);

    mPreview = (CameraSourcePreview) findViewById(R.id.preview);
    takepicher = (ImageButton) findViewById(R.id.flipButton1);
    mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
    capcher =(FrameLayout) findViewById(R.id.capcher);
    capcher1 =(FrameLayout) findViewById(R.id.capcher1);
    final ImageButton button = (ImageButton) findViewById(R.id.flipButton);
    button.setOnClickListener(mSwitchCameraButtonListener);

    if (savedInstanceState != null) {
      mIsFrontFacing = savedInstanceState.getBoolean("IsFrontFacing");
    }

    // Start using the camera if permission has been granted to this app,
    // otherwise ask for permission to use it.
    int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
    if (rc == PackageManager.PERMISSION_GRANTED) {
      createCameraSource();
    } else {
      requestCameraPermission();
    }

    capcher1.setBackground(getResources().getDrawable(R.drawable.side_effect));
      takepicher.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
          private File imageFile;

          @Override
          public void onPictureTaken(byte[] bytes) {
            try {
              // convert byte array into bitmap
              Bitmap loadedImage = null;
              Bitmap rotatedBitmap = null;
              Bitmap rotatedBitmap1 = null;
              loadedImage = BitmapFactory.decodeByteArray(bytes, 0,
                      bytes.length);

//              Bitmap cameraScaledBitmap = Bitmap.createScaledBitmap(loadedImage, loadedImage.getWidth(), loadedImage.getHeight(), true);
//              Bitmap resizedBitmap = Bitmap.createBitmap(mGraphicOverlay.getWidth(),mGraphicOverlay.getHeight(),Bitmap.Config.ARGB_8888);
//              mGraphicOverlay.draw(new Canvas(resizedBitmap));
              Bitmap cameraScaledBitmap = Bitmap.createScaledBitmap(loadedImage, mGraphicOverlay.getWidth(), mGraphicOverlay.getHeight(), true);
              Matrix rotateMatrix = new Matrix();
              rotateMatrix.preScale(-1, 1);
              rotatedBitmap = Bitmap.createBitmap(cameraScaledBitmap, 0, 0,
                      cameraScaledBitmap.getWidth(), cameraScaledBitmap.getHeight(),
                      rotateMatrix, false);
              Bitmap createBitmap = Bitmap.createBitmap(mGraphicOverlay.getWidth(), mGraphicOverlay.getHeight(), Bitmap.Config.ARGB_8888);
              capcher.draw(new Canvas(createBitmap));
              Bitmap overlayScaledBitmap = Bitmap.createScaledBitmap(createBitmap, cameraScaledBitmap.getWidth(), cameraScaledBitmap.getHeight(), true);
              Bitmap newImage = Bitmap.createBitmap(cameraScaledBitmap.getWidth(), cameraScaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//              Canvas canvas = new Canvas(resizedBitmap);
//              Matrix matrix = new Matrix();
//
//              matrix.setScale((float)resizedBitmap.getWidth()/(float)loadedImage.getWidth(),(float)resizedBitmap.getHeight()/(float)loadedImage.getHeight());
//
//// mirror by inverting scale and translating
//              matrix.preScale(-1, 1);
//              matrix.postTranslate(canvas.getWidth(), 0);
//
//              Paint paint = new Paint();
//              canvas.drawBitmap(loadedImage,matrix,paint);
//              Matrix rotateMatrix1 = new Matrix();
//              rotateMatrix1.postRotate(0);
//              rotateMatrix1.preScale(0, 0,0,0);
//              rotatedBitmap1 = Bitmap.createBitmap(overlayScaledBitmap, 0, 0,
//                      overlayScaledBitmap.getWidth(), overlayScaledBitmap.getHeight(),
//                      rotateMatrix1, false);
              Canvas canvas = new Canvas(newImage);
//              canvas.d
              canvas.drawBitmap(rotatedBitmap , 0, 0, null);
              canvas.drawBitmap(overlayScaledBitmap , (canvas.getHeight() - rotatedBitmap.getHeight())/2f, (canvas.getWidth()-rotatedBitmap.getWidth())/2f, null);
              File dir = new File(
                      Environment.getExternalStoragePublicDirectory(
                              Environment.DIRECTORY_PICTURES), "MyPhotos");
//              onPictureTaken1(bytes,mPreview);
              boolean success = true;
              if (!dir.exists())
              {
                success = dir.mkdirs();
              }
              if (success) {
                java.util.Date date = new java.util.Date();
                imageFile = new File(dir.getAbsolutePath()
                        + File.separator
                        + "Image.jpg");

                imageFile.createNewFile();
              } else {
                Toast.makeText(getBaseContext(), "Image Not saved",
                        Toast.LENGTH_SHORT).show();
                return;
              }
              ByteArrayOutputStream ostream = new ByteArrayOutputStream();

              // save image into gallery
              newImage.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

              FileOutputStream fout = new FileOutputStream(imageFile);
              fout.write(ostream.toByteArray());
              fout.close();
              ContentValues values = new ContentValues();

              values.put(MediaStore.Images.Media.DATE_TAKEN,
                      System.currentTimeMillis());
              values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
              values.put(MediaStore.MediaColumns.DATA,
                      imageFile.getAbsolutePath());

              FaceActivity.this.getContentResolver().insert(
                      MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

              //saveToInternalStorage(loadedImage);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
      }
    });
  }

  private View.OnClickListener mSwitchCameraButtonListener = new View.OnClickListener() {
    public void onClick(View v) {
      mIsFrontFacing = !mIsFrontFacing;

      if (mCameraSource != null) {
        mCameraSource.release();
        mCameraSource = null;
      }

      createCameraSource();
      startCameraSource();

    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume called.");

    startCameraSource();
  }

  @Override
  protected void onPause() {
    super.onPause();

    mPreview.stop();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putBoolean("IsFrontFacing", mIsFrontFacing);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (mCameraSource != null) {
      mCameraSource.release();
    }
  }

  // Handle camera permission requests
  // =================================

  private void requestCameraPermission() {
    Log.w(TAG, "Camera permission not acquired. Requesting permission.");

    final String[] permissions = new String[]{Manifest.permission.CAMERA};
    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
      Manifest.permission.CAMERA)) {
      ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
      return;
    }

    final Activity thisActivity = this;
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ActivityCompat.requestPermissions(thisActivity, permissions, RC_HANDLE_CAMERA_PERM);
      }
    };
    Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
      Snackbar.LENGTH_INDEFINITE)
      .setAction(R.string.ok, listener)
      .show();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode != RC_HANDLE_CAMERA_PERM) {
      Log.d(TAG, "Got unexpected permission result: " + requestCode);
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      return;
    }

    if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      // We have permission to access the camera, so create the camera source.
      Log.d(TAG, "Camera permission granted - initializing camera source.");
      createCameraSource();
      return;
    }

    // If we've reached this part of the method, it means that the user hasn't granted the app
    // access to the camera. Notify the user and exit.
    Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
      " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        finish();
      }
    };
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.app_name)
      .setMessage(R.string.no_camera_permission)
      .setPositiveButton(R.string.disappointed_ok, listener)
      .show();
  }

  // Camera source
  // =============

  private void createCameraSource() {
    Log.d(TAG, "createCameraSource called.");

    Context context = getApplicationContext();
    FaceDetector detector = createFaceDetector(context);

    int facing = CameraSource.CAMERA_FACING_FRONT;
    if (!mIsFrontFacing) {
      facing = CameraSource.CAMERA_FACING_BACK;
    }

    // The camera source is initialized to use either the front or rear facing camera.  We use a
    // relatively low resolution for the camera preview, since this is sufficient for this app
    // and the face detector will run faster at lower camera resolutions.
    //
    // However, note that there is a speed/accuracy trade-off with respect to choosing the
    // camera resolution.  The face detector will run faster with lower camera resolutions,
    // but may miss smaller faces, landmarks, or may not correctly detect eyes open/closed in
    // comparison to using higher camera resolutions.  If you have any of these issues, you may
    // want to increase the resolution.

    mCameraSource = new CameraSource.Builder(context, detector)
      .setFacing(facing).setRequestedPreviewSize(1024,786)
      .setRequestedFps(60.0f)
      .setAutoFocusEnabled(true)
      .build();

  }

  private void startCameraSource() {
    Log.d(TAG, "startCameraSource called.");

    // Make sure that the device has Google Play services available.
    int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
            getApplicationContext());
    if (code != ConnectionResult.SUCCESS) {
      Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
      dlg.show();
    }

    if (mCameraSource != null) {
      try {
        mPreview.start(mCameraSource, mGraphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        mCameraSource.release();
        mCameraSource = null;
      }
    }
  }

  // Face detector
  // =============

  /**
   *  Create the face detector, and check if it's ready for use.
   */
  @NonNull
  private FaceDetector createFaceDetector(final Context context) {
    Log.d(TAG, "createFaceDetector called.");

    FaceDetector detector = new FaceDetector.Builder(context)
      .setLandmarkType(FaceDetector.ALL_LANDMARKS)
      .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
      .setTrackingEnabled(true)
      .setMode(FaceDetector.FAST_MODE)
      .setProminentFaceOnly(mIsFrontFacing)
      .setMinFaceSize(mIsFrontFacing ? 0.35f : 0.15f)
      .build();

    MultiProcessor.Factory<Face> factory = new MultiProcessor.Factory<Face>() {
      @Override
      public Tracker<Face> create(Face face) {
        return new FaceTracker(mGraphicOverlay, context, mIsFrontFacing);
      }
    };

    Detector.Processor<Face> processor = new MultiProcessor.Builder<>(factory).build();
    detector.setProcessor(processor);

    if (!detector.isOperational()) {
      Log.w(TAG, "Face detector dependencies are not yet available.");

      // Check the device's storage.  If there's little available storage, the native
      // face detection library will not be downloaded, and the app won't work,
      // so notify the user.
      IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
      boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

      if (hasLowStorage) {
        Log.w(TAG, getString(R.string.low_storage_error));
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            finish();
          }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
          .setMessage(R.string.low_storage_error)
          .setPositiveButton(R.string.disappointed_ok, listener)
          .show();
      }
    }
    return detector;
  }

}
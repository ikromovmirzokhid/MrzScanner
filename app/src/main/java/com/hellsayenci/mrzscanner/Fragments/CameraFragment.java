package com.hellsayenci.mrzscanner.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.FrameOverlay;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.TextRecognitionHelper;
import com.hellsayenci.mrzscanner.mrz.MrzParser;
import com.hellsayenci.mrzscanner.mrz.MrzRecord;
import com.hellsayenci.mrzscanner.pojos.MrzResult;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;


public class CameraFragment extends Fragment {

    private NavController navController;
    private Toolbar toolbar;

    private static String TAG = "CameraActivity";

    private CameraView camera;

    private FrameOverlay viewFinder;

    private TextRecognitionHelper textRecognitionHelper;

    private AtomicBoolean processing = new AtomicBoolean(false);

    private ProcessOCR processOCR;

    private Bitmap originalBitmap = null;
    private Bitmap scannable = null;

    private ConstraintLayout root;

    private Bitmap faceRecognisedBitmap;

    private byte[] jpegByteArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.camera_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        toolbar = view.findViewById(R.id.toolbar);

        ((MainActivity) getActivity()).setToolbar(toolbar);

        camera = view.findViewById(R.id.camera);
        camera.setLifecycleOwner(this);

        root = view.findViewById(R.id.root);
        toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setToolbar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        camera.addCameraListener(new CameraListener() {
            @Override
            public void onCameraOpened(@NonNull CameraOptions options) {
                viewFinder = new FrameOverlay(getActivity());
                camera.addView(viewFinder);
                camera.addFrameProcessor(frameProcessor);
            }
        });

        textRecognitionHelper = new TextRecognitionHelper(getActivity(), new TextRecognitionHelper.OnMRZScanned() {
            @Override
            public void onScanned(String mrzText) {

                try {
                    FileOutputStream fos = new FileOutputStream(getActivity().getFilesDir().getAbsolutePath() + "/" + "mrzimage.png");
                    originalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    FileOutputStream fos = new FileOutputStream(getActivity().getFilesDir().getAbsolutePath() + "/" + "scannable.png");
                    scannable.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("Found MRZ", mrzText);
                camera.removeFrameProcessor(frameProcessor);

                Snackbar.make(view, "Your Passport is Scanned Successfully", Snackbar.LENGTH_SHORT).show();


//                faceRecognisedBitmap = faceRecogniseFromBitmap(originalBitmap);
//
//                if (faceRecognisedBitmap != null) {
//                    originalBitmap = faceRecognisedBitmap;
//                }

                //originalBitmap = getResizedBitmap(originalBitmap, 500);

                MrzRecord record = MrzParser.parse(mrzText);
                MrzResult result = new MrzResult(record.dateOfBirth.day + "/" + record.dateOfBirth.month + "/" + record.dateOfBirth.year,
                        record.documentNumber, record.expirationDate.day + "/" + record.expirationDate.month + "/" + record.expirationDate.year,
                        record.nationality, record.sex.mrz, record.givenNames, record.issuingCountry, record.surname, null, "", "");

                String mrzJson = new Gson().toJson(result);
                Bundle bundle = new Bundle();
                bundle.putString("mrzResult", mrzJson);
                navController.navigate(R.id.action_cameraFragment_to_selectBlankTypeFragment, bundle);

            }
        });


    }

    private FrameProcessor frameProcessor = new FrameProcessor() {
        @Override
        public void process(@NonNull Frame frame) {
            if (frame.getData() != null && !processing.get()) {
                processing.set(true);

                YuvImage yuvImage = new YuvImage(frame.getData(), ImageFormat.NV21, frame.getSize().getWidth(), frame.getSize().getHeight(), null);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(new Rect(0, 0, frame.getSize().getWidth(), frame.getSize().getHeight()), 100, os);
                jpegByteArray = os.toByteArray();

                Bitmap bitmap = BitmapFactory.decodeByteArray(jpegByteArray, 0, jpegByteArray.length);

                if (bitmap != null) {
                    bitmap = rotateImage(bitmap, frame.getRotation());

                    bitmap = getViewFinderArea(bitmap);

                    originalBitmap = bitmap;

                    scannable = getScannableArea(bitmap);

                    processOCR = new ProcessOCR();
                    processOCR.setBitmap(scannable);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processOCR.execute();
                        }
                    });
                }
            }
        }
    };

    private Bitmap getViewFinderArea(Bitmap bitmap) {
        int sizeInPixel = getResources().getDimensionPixelSize(R.dimen.frame_margin);
        int center = bitmap.getHeight() / 2;

        int left = sizeInPixel;
        int right = bitmap.getWidth() - sizeInPixel;
        int width = right - left;
        int frameHeight = (int) (width / 1.42f); // Passport's size (ISO/IEC 7810 ID-3) is 125mm × 88mm

        int top = center - (frameHeight / 2);

        bitmap = Bitmap.createBitmap(bitmap, left, top,
                width, frameHeight);

        return bitmap;
    }

    private Bitmap getScannableArea(Bitmap bitmap) {
        int top = bitmap.getHeight() * 4 / 10;

        bitmap = Bitmap.createBitmap(bitmap, 0, top,
                bitmap.getWidth(), bitmap.getHeight() - top);

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap bitmap, int rotate) {
        Log.v(TAG, "Rotation: " + rotate);

        if (rotate != 0) {

            // Getting width & height of the given image.
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            // Rotating Bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
        }

        // Convert to ARGB_8888, required by tess
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return bitmap;
    }

    /**
     * reduces the size of the image
     *
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private class ProcessOCR extends AsyncTask {

        Bitmap bitmap = null;

        @Override
        protected Object doInBackground(Object[] objects) {
            if (bitmap != null) {

                textRecognitionHelper.setBitmap(bitmap);

                textRecognitionHelper.doOCR();

                textRecognitionHelper.stop();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            processing.set(false);
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

    private Bitmap faceRecogniseFromBitmap(Bitmap myBitmap) {
        Paint recPaint = new Paint();
        recPaint.setStrokeWidth(5);
        recPaint.setColor(Color.RED);
        recPaint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);

        FaceDetector faceDetector = new FaceDetector.Builder(getActivity().getApplication())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        if (faceDetector.isOperational()) {
            Snackbar.make(getView(), "Face Recognition failed", Snackbar.LENGTH_SHORT).show();
            return null;
        }
        com.google.android.gms.vision.Frame frame = new com.google.android.gms.vision.Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);

        for (int i = 0; i < sparseArray.size(); i++) {
            Face face = sparseArray.valueAt(i);
            float x1 = face.getPosition().x;
            float y1 = face.getPosition().y;
            float x2 = x1 + face.getWidth();
            float y2 = y1 + face.getHeight();
            RectF rectF = new RectF(x1, y1, x2, y2);

            canvas.drawRoundRect(rectF, 2, 2, recPaint);

        }

        return tempBitmap;

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flash_menu:
                if (camera.getFlash() == Flash.OFF) {
                    camera.setFlash(Flash.TORCH);
                    item.setIcon(R.drawable.ic_flash_on_black_24dp);
                } else {
                    camera.setFlash(Flash.OFF);
                    item.setIcon(R.drawable.ic_flash_off_black_24dp);
                }
                return true;
        }
        return false;
    }


}

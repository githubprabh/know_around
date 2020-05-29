package com.example.know360_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.know360_1.Helper.InternetCheck;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.speech.tts.TextToSpeech;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public TextToSpeech mTTS;
    public EditText mEditText;
    public Button mButtonSpeak;
    CameraView cameraView;
    Button btn_detect;
    AlertDialog waitingDialog;

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (CameraView)findViewById(R.id.camera_view);
        btn_detect = (Button)findViewById(R.id.btn_detect);
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage("Please wait...").setCancelable(false).build();
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();
//                runDetector(bitmap);
            }
            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.start();
                cameraView.captureImage();
            }
        });

        mButtonSpeak = findViewById(R.id.button_speak);
        mEditText = findViewById(R.id.edit_text);
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.GERMAN);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        mButtonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }});

                mButtonSpeak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        speak();
                    }
                });
            }

        public void speak() {
            String text = mEditText.getText().toString();
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
        @Override
        public void onDestroy() {
            if (mTTS != null) {
                mTTS.stop();
                mTTS.shutdown();
            }
            super.onDestroy();
        }
}







//
//    private void runDetector(Bitmap bitmap) {
//
//        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//
//
//        new InternetCheck(new InternetCheck.Consumer() {
//            @Override
//            public void accept(boolean internet) {
//                if(internet){
//                    FirebaseVisionCloudDetectorOptions options = new FirebaseVisionCloudDetectorOptions.Builder().setMaxResults(5).build();
//                    FirebaseVisionCloudLabelDetector detector = FirebaseVision.getInstance().getVisionCloudLabelDetector();
//
//                    detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
//                        @Override
//                        public void onSuccess(List<FirebaseVisionCloudLabel> firebaseVisionCloudLabels) {
//                            processDataResultCloud(firebaseVisionCloudLabels);
//
//
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d("EDMERROR", e.getMessage());
//                                }
//                            });
//                    }
//                else
//                {
//                    FirebaseVisionLabelDetectorOptions options = new FirebaseVisionLabelDetectorOptions.Builder().setConfidenceThreshold(0.8f).build();
//                    FirebaseVisionLabelDetector detector = FirebaseVision.getInstance().getVisionLabelDetector(options);
//
//                    detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionLabel>>() {
//                        @Override
//                        public void onSuccess(List<FirebaseVisionLabel> firebaseVisionLabels) {
//                            processDataResult(firebaseVisionLabels);
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d("EDMERROR", e.getMessage());
//                                }
//                            });
//
//                    }
//
//                }
//        });
//    }
//
//    private void processDataResultCloud(List<FirebaseVisionCloudLabel> firebaseVisionCloudLabels) {
//        for(FirebaseVisionCloudLabel label : firebaseVisionCloudLabels){
//            Toast.makeText(this, "Cloud label " + label.getLabel(), Toast.LENGTH_SHORT).show();
//        }
//        if(waitingDialog.isShowing())
//        waitingDialog.dismiss();
//    }
//
//    private void processDataResult(List<FirebaseVisionLabel> firebaseVisionLabels) {
//        for(FirebaseVisionLabel label : firebaseVisionLabels){
//            Toast.makeText(this, "Device label :" + label.getLabel(), Toast.LENGTH_SHORT).show();
//        }
//        if(waitingDialog.isShowing())
//        waitingDialog.dismiss();
//    }


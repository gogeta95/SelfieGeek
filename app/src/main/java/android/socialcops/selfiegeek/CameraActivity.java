package android.socialcops.selfiegeek;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class CameraActivity extends AppCompatActivity {

    private ImageSurfaceView mImageSurfaceView;
    private Camera camera;
    private String DEBUG_TAG = MainActivity.class.getSimpleName();
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    private FrameLayout cameraPreviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraPreviewLayout = (FrameLayout)findViewById(R.id.camera_preview);

        camera = checkDeviceCamera();
        mediaRecorder = prepareVideoRecorder(camera);

        mImageSurfaceView = new ImageSurfaceView(CameraActivity.this, camera, mediaRecorder);
        cameraPreviewLayout.addView(mImageSurfaceView);

        ImageButton captureButton = (ImageButton) findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.startPreview();
                camera.takePicture(null, null, pictureCallback);
            }
        });

        final ImageButton videoButton = (ImageButton) findViewById(R.id.video_button);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording){
                    isRecording=false;
                    mediaRecorder.stop();
                    releaseMediaRecorder();
                    camera.lock();
                    videoButton.setImageResource(R.mipmap.ic_start);

                }else {
                    isRecording=true;
                    mediaRecorder.start();
                    videoButton.setImageResource(R.mipmap.ic_stop);

                }
            }
        });
    }
    private Camera checkDeviceCamera(){
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            Log.e("Camera",e.getMessage());
        }
        return mCamera;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFileDir = getDir();

            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

                Log.d(DEBUG_TAG, "Can't create directory to save image.");
                Toast.makeText(CameraActivity.this, "Can't create directory to save image.",
                        Toast.LENGTH_LONG).show();
                return;

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile = "Picture_" + date + ".jpg";

            String filename = pictureFileDir.getPath() + File.separator + photoFile;

            File pictureFile = new File(filename);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(CameraActivity.this, "New Image saved:" + photoFile,
                        Toast.LENGTH_LONG).show();
            } catch (Exception error) {
                Log.d(DEBUG_TAG, "File" + filename + "not saved: "
                        + error.getMessage());
                Toast.makeText(CameraActivity.this, "Image could not be saved.",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    public static File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");
    }

    private MediaRecorder prepareVideoRecorder(Camera mCamera){
        MediaRecorder mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(getOutputMediaFile());
        return mMediaRecorder;
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            camera.lock();           // lock camera for later use
        }
    }

    private String getOutputMediaFile(){
        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Log.d(DEBUG_TAG, "Can't create directory to save image.");
            Toast.makeText(CameraActivity.this, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String videoFile = "Video_" + date + ".mp4";
        String filename = pictureFileDir.getPath() + File.separator + videoFile;

        return filename;
    }
}
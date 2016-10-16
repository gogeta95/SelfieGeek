package android.socialcops.selfiegeek;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

/**
 * Created by Ayush on 15-10-2016.
 */

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private SurfaceHolder surfaceHolder;

    public ImageSurfaceView(Context context, Camera camera, MediaRecorder mediaRecorder) {
        super(context);
        this.camera = camera;
        this.mediaRecorder = mediaRecorder;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            mediaRecorder.setPreviewDisplay(holder.getSurface());
            try{
                this.mediaRecorder.prepare();
            } catch (Exception e) {
                String message = e.getMessage();
                Log.e("MediaRecorder",message);
            }

            try {
                this.camera.setPreviewDisplay(holder);
                this.camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            this.mediaRecorder.release();
            this.camera.stopPreview();
            this.camera.release();
    }
}

package com.carrillo.jesus.socialmediaweeduutablet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private ImageButton takePictureButton;
    private ImageButton atrasButton;
    private TextureView textureView;
    private Context mContext;
    private String rutaImagen;
    private boolean iscreatedThread=false ,isopenCamera=false;
    private int width;
    private int height;
    private TextView countDownTextView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    private Activity mActivity=this;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;

    private int inicioContador=5000;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    //  private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pantalla Completa y sin ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ///////
        countDownTextView =(TextView) findViewById(R.id.textViewCamera);
        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = (ImageButton) findViewById(R.id.btn_takepicture);
        assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer((inicioContador), 1000) {
                    public void onTick(long millisUntilFinished) {
                        countDownTextView.setText(getString(R.string.count_down) + ": " + (millisUntilFinished) / 1000);
                    }

                    public void onFinish() {
                        countDownTextView.setText("sonrie");
                        takePicture();
                    }
                }.start();

            }
        });
        mContext=getApplicationContext();
        atrasButton = (ImageButton) findViewById(R.id.imageButtonAtrasCamera);
        assert atrasButton != null;
        atrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeCamera();
                onBackPressed();

            }
        });
    }
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            if (manager.getCameraIdList()[1].isEmpty()){
             cameraId= manager.getCameraIdList()[0];
            }else {
                cameraId = manager.getCameraIdList()[1];
            }
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
        isopenCamera=true;
    }
    private void showToastShort(final String text) {
        final Activity activity = mActivity;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            if(!isopenCamera) {
                openCamera();
            }
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
            if (!isopenCamera){
                openCamera();
            }

        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            showToastShort(getString(R.string.saved)+":" + file);
            createCameraPreview();
          //  transformImage(textureView.getWidth(),textureView.getHeight());
        }
    };
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        iscreatedThread=true;
    }
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        iscreatedThread=false;
    }
    protected void takePicture() {
        if(null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
             width = 1280;
             height = 720;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            rutaImagen= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+getString(R.string.directory_images_name)+"/"+getString(R.string.image_name)+"_"+System.currentTimeMillis()+".jpg";
            final File file = new File(rutaImagen);
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"+getString(R.string.directory_images_name));
// Comprobamos si la carpeta está ya creada

// Si la carpeta no está creada, la creamos.

            if(!f.isDirectory()) {
                String newFolder = "/"+getString(R.string.directory_images_name); //cualquierCarpeta es el nombre de la Carpeta que vamos a crear
                String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myNewFolder = new File(extStorageDirectory + newFolder);
                myNewFolder.mkdir(); //creamos la carpeta
            }else{
                Log.d(TAG,"La carpeta ya estaba creada");
            }
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);

                    } finally {
                        if (null != output) {
                            output.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    countDownTextView.setText("");
                                }
                            });
                            Intent intent = new Intent(mActivity, PreviewActivity.class);
                            intent.putExtra("ruta", rutaImagen);
                            intent.putExtra("width",width);
                            intent.putExtra("height",height);
                            closeCamera();
                            startActivity(intent);


                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    showToastShort(getString(R.string.saved)+":" + file);
                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }




    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    showToastShort(getString(R.string.configuration_change));
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if(imageDimension==null||textureView==null){

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Matrix matrix=new Matrix();
                int rotation=getWindowManager().getDefaultDisplay().getRotation();
                RectF textureRectf= new RectF(0,0,textureView.getWidth(),textureView.getHeight());
                RectF previewRectf= new RectF(0,0,imageDimension.getWidth(),imageDimension.getHeight());
                float centerX=textureRectf.centerX();
                float centerY=textureRectf.centerY();
                if(rotation==Surface.ROTATION_90||rotation==Surface.ROTATION_270){
                    previewRectf.offset(centerX-previewRectf.centerX(),centerY-previewRectf.centerY());
                    matrix.setRectToRect(textureRectf,previewRectf, Matrix.ScaleToFit.FILL);
                     float scale1=(float)textureView.getHeight()/imageDimension.getWidth();
                    float scale2= (float)textureView.getWidth()/imageDimension.getHeight();
                      matrix.postScale(scale1,scale2,centerX,centerY);
                    matrix.postRotate(90*(rotation-2),centerX,centerY);
                }
                textureView.setTransform(matrix);
            }
        });

    }

    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
        isopenCamera=false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                showToastShort(getString(R.string.permission_deny));
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        fullScreenCall();
        Log.e(TAG, "onResume");
        if(!iscreatedThread) {
            startBackgroundThread();
        }
        if (textureView.isAvailable()) {
           // transformImage(textureView.getWidth(),textureView.getHeight());
            if(!isopenCamera) {
                openCamera();
            }
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    public void fullScreenCall() {
        //ocultar el menu de navegacion
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

    }
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        if(iscreatedThread) {
            stopBackgroundThread();
        }
        super.onPause();
    }
    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        if(isopenCamera) {
            closeCamera();
        }
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        if(iscreatedThread) {
            stopBackgroundThread();
        }
        if(isopenCamera) {
            closeCamera();
        }
        super.onDestroy();
    }

}
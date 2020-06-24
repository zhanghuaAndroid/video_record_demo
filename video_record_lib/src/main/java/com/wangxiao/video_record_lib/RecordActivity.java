package com.wangxiao.video_record_lib;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.util.Pair;

import com.wangxiao.common_lib.base.BaseActivity;

import java.io.IOException;
import java.util.List;

public class RecordActivity extends BaseActivity {
    private SurfaceView svRecord;
    private ImageView ivClose;
    private ImageView ivSwitchPhoto;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private int currentCameraType;

    @Override
    protected int setLayout() {
        return R.layout.activity_record;
    }

    @Override
    protected void initListener() {
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback2() {

            @Override
            public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
                mSurfaceHolder = surfaceHolder;
            }

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mSurfaceHolder = surfaceHolder;
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                currentCameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
                mCamera.setDisplayOrientation(90);
                setRecordView();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                mSurfaceHolder = surfaceHolder;
                mCamera.startPreview();
                mCamera.cancelAutoFocus();
                mCamera.unlock();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mSurfaceHolder = surfaceHolder;
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivSwitchPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCameraType == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    closeCamera();
                    currentCameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    mCamera.setDisplayOrientation(90);
                    setRecordView();
                } else {
                    closeCamera();
                    currentCameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    mCamera.setDisplayOrientation(90);
                    setRecordView();
                }
            }
        });
    }

    private void setRecordView() {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            Camera.Parameters parameters = mCamera.getParameters();
            Pair previewSize = getPreviewSize();
            parameters.setPictureSize((int)previewSize.first,(int)previewSize.second);
            parameters.setJpegQuality(100);
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();//停掉原来摄像头的预览
            mCamera.release();//释放资源
            mCamera = null;//取消原来摄像头
//            CameraManager.get().getPreviewCallback().setHandler(null, 0);
//            CameraManager.get().getAutoFocusCallback().setHandler(null, 0);
//            CameraManager.get().setPreviewing(false);
        }

    }

    private Pair getPreviewSize() {
        // 可不可以直接获取
        int bestPreviewWidth = 1920;
        int bestPreviewHeight = 1080;
        int mCameraPreviewWidth;
        int mCameraPreviewHeight;
        int diffs = Integer.MAX_VALUE;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point screenResolution = new Point(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        List<Camera.Size> availablePreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        for (Camera.Size previewSize : availablePreviewSizes){
            mCameraPreviewWidth = previewSize.width;
            mCameraPreviewHeight = previewSize.height;
            int newDiffs = Math.abs(mCameraPreviewWidth - screenResolution.y) + Math.abs(mCameraPreviewHeight - screenResolution.x);
            if (newDiffs == 0) {
                bestPreviewWidth = mCameraPreviewWidth;
                bestPreviewHeight = mCameraPreviewHeight;
                break;
            }
            if (diffs > newDiffs) {
                bestPreviewWidth = mCameraPreviewWidth;
                bestPreviewHeight = mCameraPreviewHeight;
                diffs = newDiffs;
            }
        }
        return new Pair<Integer, Integer>(bestPreviewWidth,bestPreviewHeight);
    }

    @Override
    protected void initView() {
        svRecord = findViewById(R.id.sv_record);
        ivClose = findViewById(R.id.iv_close);
        ivSwitchPhoto = findViewById(R.id.iv_switch_photo);
        mSurfaceHolder = svRecord.getHolder();
        initMedia();
    }

    private void initMedia() {
        mMediaPlayer = new MediaPlayer();

    }
}
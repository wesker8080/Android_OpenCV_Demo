package com.wesker.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wesker.R;
import com.wesker.com.wesker.model.ListConstants;
import com.wesker.listener.DialogItemListener;
import com.wesker.util.BitmapUtil;
import com.wesker.util.ImageProcessUtils;
import com.wesker.util.UiUtils;

import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProcessImageActivity extends AppCompatActivity implements View.OnClickListener,DialogItemListener {
    private String TAG = "ProcessImageActivity";
    private Bitmap bitmap;
    private Bitmap bitmapcopy;
    public ImageView imgView;
    private final int IMAGE_SIZE = 768;
    private int REQUEST_CODE = 1;
    private String process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initOpenCVLibs();
        process = getIntent().getStringExtra("process");
        Button btn_process = (Button)findViewById(R.id.process_img);
        btn_process.setText(process);
        btn_process.setTag("process");
        btn_process.setOnClickListener(this);
        Button selectImg = (Button)findViewById(R.id.select_img);
        selectImg.setTag("select");
        selectImg.setOnClickListener(this);
        imgView = (ImageView)findViewById(R.id.imageView);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeResource(ProcessImageActivity.this.getResources(),R.drawable.lady,opt);
        imgView.setImageBitmap(bitmap);
    }
    private void initOpenCVLibs() {
        boolean success = OpenCVLoader.initDebug();
        if(success){
            Log.i(TAG,"OpenCVLibsInitSuccess!!!");
        }
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
       if(tag instanceof String){
           if("select".equals(tag.toString())){
               selectImage();
           } else if("process".equals(tag.toString())){
                processImage();
            }
        }
    }

    /*
        从相册里选择一张图片
     */
    private void selectImage() {
        //打开系统相册选择图片
        Intent intent;
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Browser Image..."), REQUEST_CODE);
    }
    /*

     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            //防止图片过大
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStream,null,opt);
                int height = opt.outHeight;
                int width = opt.outWidth;
                int sampleSize = 1;
                int max = Math.max(height,width);
                if(max > IMAGE_SIZE){
                    int newWidth = width/2;
                    int newHight = height/2;
                    while ((newHight/sampleSize) >= IMAGE_SIZE || (newWidth/sampleSize) >= IMAGE_SIZE){
                        sampleSize *= 2;
                    }
                }
                opt.inSampleSize = sampleSize;
                opt.inJustDecodeBounds = false;
                opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,opt);
                //调整图片方向start
                int imageAngle = BitmapUtil.getExifOrientation(getContentResolver().openInputStream(uri));
                if(imageAngle == 90 || imageAngle == 180 || imageAngle == 270){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(imageAngle);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
                //调整图片方向end
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d(TAG,"no file");
            }
        }
    }

    private void processImage() {
        //BitmapFactory.Options opt = new BitmapFactory.Options();
        //opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //Bitmap bitmap = BitmapFactory.decodeResource(ProcessImageActivity.this.getResources(),R.drawable.lady,opt);
        bitmapcopy = bitmap.copy(bitmap.getConfig(),true);//拷贝原图
        Log.d(TAG,"process:"+process+",bitmapcopy == "+bitmapcopy);
        switch (process){
            case ListConstants.TEST_ENV_COMMAND:
                bitmapcopy = ImageProcessUtils.bitMap2Gray(bitmapcopy,ListConstants.TEST_ENV_COMMAND);break;
            case  ListConstants.BITMAP_GRAY_COMMAND:
                bitmapcopy = ImageProcessUtils.LocalBitMap2Gray(bitmapcopy,ListConstants.BITMAP_GRAY_COMMAND);break;
            case ListConstants.MAT_PIXEL_INVERT_COMMAND:
                bitmapcopy = ImageProcessUtils.bitMap2Invert(bitmapcopy,ListConstants.MAT_PIXEL_INVERT_COMMAND);break;
            case ListConstants.BITMAP_PIXEL_INVERT_COMMAND:
                bitmapcopy = ImageProcessUtils.LocalBitMap2Invert(bitmapcopy,ListConstants.BITMAP_PIXEL_INVERT_COMMAND);break;
            case ListConstants.MAT_PIXEL_SUBSTARCT_COMMAND:
                bitmapcopy = ImageProcessUtils.subStract(bitmapcopy,ListConstants.MAT_PIXEL_SUBSTARCT_COMMAND);break;
            case ListConstants.MAT_PIXEL_ADDSTARCT_COMMAND:
                bitmapcopy = ImageProcessUtils.addStract(bitmapcopy,ListConstants.MAT_PIXEL_ADDSTARCT_COMMAND);break;
            case ListConstants.MAT_PIXEL_MULSTARCT_COMMAND:
                bitmapcopy = ImageProcessUtils.mulStract(bitmapcopy,ListConstants.MAT_PIXEL_MULSTARCT_COMMAND);break;
            case ListConstants.MAT_PIXEL_NATURE_COMMAND:
                DialogItemListener listener ;
                UiUtils mUiUtils = new UiUtils(this);
                mUiUtils.showDialog(this,this);
                return;
            case ListConstants.IMAGE_CONTENT_COMMAND:
                bitmapcopy = ImageProcessUtils.demoMatUsage();break;
            case ListConstants.SUB_IMAGE_COMMAND:
                bitmapcopy = ImageProcessUtils.getROIArea(bitmapcopy);break;
            case ListConstants.BLUR_IMAGE_COMMAND:
                ImageProcessUtils.meanBlur(bitmapcopy);break;
            case ListConstants.GAUSSIAN_BLUR_IMAGE_COMMAND:
                ImageProcessUtils.gaussianBlur(bitmapcopy);break;
            case  ListConstants.MEDIAN_FILTER_IMAGE_COMMAND:
                ImageProcessUtils.medianBlur(bitmapcopy); break;
            case ListConstants.BI_BLUR_COMMAND:
                ImageProcessUtils.biBlur(bitmapcopy);break;
            case ListConstants.CUSTOM_BLUR_COMMAND:
                ImageProcessUtils.customFilter(ListConstants.CUSTOM_BLUR_COMMAND,bitmapcopy);break;
            case ListConstants.CUSTOM_EDGE_COMMAND:
                ImageProcessUtils.customFilter(ListConstants.CUSTOM_EDGE_COMMAND,bitmapcopy);break;
            case ListConstants.CUSTOM_SHARPEN_COMMAND:
                ImageProcessUtils.customFilter(ListConstants.CUSTOM_SHARPEN_COMMAND,bitmapcopy);break;
            case ListConstants.ERODE_MIN_FILTER_COMMAND:
                ImageProcessUtils.erodeOrDilation(ListConstants.ERODE_MIN_FILTER_COMMAND,bitmapcopy);break;
            case ListConstants.DILATION_MAX_FILTER_COMMAND:
                ImageProcessUtils.erodeOrDilation(ListConstants.DILATION_MAX_FILTER_COMMAND,bitmapcopy);break;
            case ListConstants.OPEN_COMMAND:
                ImageProcessUtils.openOrClose(ListConstants.OPEN_COMMAND,bitmapcopy);break;
            case ListConstants.CLOSE_COMMAND:
                ImageProcessUtils.openOrClose(ListConstants.CLOSE_COMMAND,bitmapcopy);break;
            case ListConstants.MORPH_LINE_COMMAND:
                ImageProcessUtils.morphLineDetection(bitmapcopy);break;
            case ListConstants.THRESHOLD_BINARY_COMMAND:
                ImageProcessUtils.thresholdImg(ListConstants.THRESHOLD_BINARY_COMMAND,bitmapcopy);break;
            case ListConstants.THRESHOLD_BINARY_INV_COMMAND:
                ImageProcessUtils.thresholdImg(ListConstants.THRESHOLD_BINARY_INV_COMMAND,bitmapcopy);break;
            case ListConstants.THRESHOLD_BINARY_TRUNC_COMMAND:
                ImageProcessUtils.thresholdImg(ListConstants.THRESHOLD_BINARY_TRUNC_COMMAND,bitmapcopy);break;
            case ListConstants.THRESHOLD_BINARY_TOZERO_COMMAND:
                ImageProcessUtils.thresholdImg(ListConstants.THRESHOLD_BINARY_TOZERO_COMMAND,bitmapcopy);break;
            case ListConstants.THRESHOLD_BINARY_TOZERO_INV_COMMAND:
                ImageProcessUtils.thresholdImg(ListConstants.THRESHOLD_BINARY_TOZERO_INV_COMMAND,bitmapcopy);break;
            case ListConstants.HISTOGRAM_EQ_COMMAND:
                ImageProcessUtils.histogramEq(process,bitmapcopy);break;
            case ListConstants.GRADIENT_SOBEL_X_COMMAND:
            case ListConstants.GRADIENT_SOBEL_Y_COMMAND:
            case ListConstants.GRADIENT_IMG_COMMAND:
                ImageProcessUtils.sobelGradient(process,bitmapcopy);break;
        }

        imgView.setImageBitmap(bitmapcopy);
    }

    @Override
    public void OnItemClick(String style) {
       // bitmapcopy = bitmap.copy(bitmap.getConfig(),true);//拷贝原图
        Log.d(TAG,"bitmapcopy--->"+bitmapcopy);
        bitmapcopy = ImageProcessUtils.natureStyle(bitmapcopy,style,ListConstants.MAT_PIXEL_NATURE_COMMAND);
        imgView.setImageBitmap(bitmapcopy);
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
    }
}

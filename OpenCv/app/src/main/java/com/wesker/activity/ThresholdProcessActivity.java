package com.wesker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wesker.R;
import com.wesker.com.wesker.model.ListConstants;
import com.wesker.listener.DialogItemListener;
import com.wesker.util.BitmapUtil;
import com.wesker.util.ImageProcessUtils;
import com.wesker.util.UiUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThresholdProcessActivity extends Activity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{
    private String TAG = "ThresholdProcessActivity";
    private Bitmap bitmap;
    private Bitmap bitmapcopy;
    public ImageView imgView;
    private final int IMAGE_SIZE = 768;
    private int REQUEST_CODE = 1;
    private String process;
    private SeekBar myseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold_process);
        initOpenCVLibs();
        process = getIntent().getStringExtra("process");
        Button btn_process = (Button)findViewById(R.id.process_img);
        btn_process.setText(process);
        btn_process.setTag("process");
        btn_process.setOnClickListener(this);
        myseekBar = (SeekBar)this.findViewById(R.id.seekBarView);
        myseekBar.setOnSeekBarChangeListener(this);

        TextView textView = (TextView)this.findViewById(R.id.seekBarValueTxtView);
        textView.setText("当前阈值为: " + myseekBar.getProgress());
        Button selectImg = (Button)findViewById(R.id.select_img);
        selectImg.setTag("select");
        selectImg.setOnClickListener(this);
        imgView = (ImageView)findViewById(R.id.imageView);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.lady,opt);
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
                processImage(myseekBar.getProgress());
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
    private void processImage(int value) {
        //BitmapFactory.Options opt = new BitmapFactory.Options();
        //opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //Bitmap bitmap = BitmapFactory.decodeResource(ProcessImageActivity.this.getResources(),R.drawable.lady,opt);
        bitmapcopy = bitmap.copy(bitmap.getConfig(),true);//拷贝原图
        Log.d(TAG,"process:"+process+",bitmapcopy == "+bitmapcopy);
        if(value < 1){
            value = 2;
        }
        if((value % 2) == 0) {
            value = value+1;
        }
        switch (process){
            case ListConstants.THRESHOLD_BINARY_COMMAND:
                ImageProcessUtils.manualThresholdImg(value,bitmapcopy);break;
            case ListConstants.ADAPTIVE_THRESHOLD_COMMAND:
                ImageProcessUtils.adaptiveThresholdOrGaussian(value,process,bitmapcopy);break;
            case ListConstants.ADAPTIVE_GAUSSIAN_COMMAND:
                ImageProcessUtils.adaptiveThresholdOrGaussian(value,process,bitmapcopy);break;
            case ListConstants.CANNY_EDGE_COMMAND:
                ImageProcessUtils.cannyEdge(value,bitmapcopy);break;
            case ListConstants.HOUGH_LINE_COMMAND:
                ImageProcessUtils.houghLinesDet(value,bitmapcopy);break;
        }

        ImageView imgView = (ImageView) this.findViewById(R.id.imageView);
        imgView.setImageBitmap(bitmapcopy);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SeekBar myseekBar = (SeekBar)this.findViewById(R.id.seekBarView);
        int value = myseekBar.getProgress();
        TextView textView = (TextView)this.findViewById(R.id.seekBarValueTxtView);
        textView.setText("当前阈值为: " + value);

        // call process
        processImage(value);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

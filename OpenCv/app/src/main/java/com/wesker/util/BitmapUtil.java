package com.wesker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.wesker.R;
import com.wesker.activity.ProcessImageActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Hello，Mr.Zhang on 2017/7/20.
 * 这个类是调整图片的方向用的。
 */

public class BitmapUtil {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getExifOrientation(InputStream in) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(in);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }

        return degree;
    }
    //颜色查找表
    public static  int[] fogLookUp;
    //彩虹颜色查找表
    public static  int[] rainbowLookUp;

    public static void buildFogLookupTable() {
        fogLookUp = new int[256];
        int fogLimit = 40;
        for(int i=0; i<fogLookUp.length; i++)
        {
            if(i > 127)
            {
                fogLookUp[i] = i - fogLimit;
                if(fogLookUp[i] < 127)
                {
                    fogLookUp[i] = 127;
                }
            }
            else
            {
                fogLookUp[i] = i + fogLimit;
                if(fogLookUp[i] > 127)
                {
                    fogLookUp[i] = 127;
                }
            }
        }
    }
    //根据一张彩虹图片初始化生成彩虹颜色查找表
    public static void buildRainBowLookupTable()
    {
        rainbowLookUp = new int[759];
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Log.d("buildRainBowLookupTable",GolbalApplicationUtil.getContext()+"");
        Bitmap image = BitmapFactory.decodeResource(GolbalApplicationUtil.getContext().getResources(), R.drawable.rainbow,opt);
        if(image != null){
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];
            Log.d("width:",width+"");
            image.getPixels(pixels,0,width,0,0,width,height);
            int sum = 0;
            for (int col = 0; col < width; col++) {
                rainbowLookUp[col] = pixels[col];
            }
        }
    }
    //防止数值越界
    public static int clamp(int value) {
        return value > 255 ? 255 :
                (value < 0 ? 0 : value);
    }
}

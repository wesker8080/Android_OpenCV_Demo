package com.wesker.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.wesker.com.wesker.model.ListConstants;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static com.wesker.com.wesker.model.ListConstants.CUSTOM_BLUR_COMMAND;
import static com.wesker.com.wesker.model.ListConstants.CUSTOM_EDGE_COMMAND;
import static com.wesker.com.wesker.model.ListConstants.CUSTOM_SHARPEN_COMMAND;
import static com.wesker.com.wesker.model.ListConstants.ERODE_MIN_FILTER_COMMAND;
import static com.wesker.com.wesker.model.ListConstants.OPEN_COMMAND;


/**
 * Created by Hello，Mr.Zhang on 2017/7/20.
 */

public class ImageProcessUtils {
    private static final String TAG = "ImageProcessUtils";
    private static long startTime = 0;
    private static long endTime = 0;
    public static Bitmap bitMap2Gray(Bitmap bitmap,String constant){
        startTime = System.currentTimeMillis();
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,dst, Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        return bitmap;
    }

    public static Bitmap bitMap2Invert(Bitmap bitmap,String constant) {
        startTime = System.currentTimeMillis();
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src); //将bitmap对象转为Mat对象，OpenCV里是用Mat对象来进行图片处理的
        Core.bitwise_not(src,src);//OpenCV的按位取反接口，这一句相当于下面注释掉的代码
        //获取图像信息
        //int width = src.cols();
        //int height = src.rows();
        //int cnum = src.channels();//获取图像通道数，如ARGB_8888 表示int字节，32位，每个通道占8位，就是4个通道
        //byte[] bgra = new byte[cnum];//在OpenCV中是BGRA顺序
        //下面的for算法就是对像素取反的操作。因为有多次循环，频繁调用JNI，所以运行速度很慢，不建议使用
//        for (int row = 0; row < height; row++){
//            for (int col = 0;col < width; col++){
//                src.get(row,col,bgra);
//                for (int i = 0; i <cnum; i++){
//                    bgra[i] = (byte) (255 - bgra[i] & 0Xff);//每个像素取反操作
//                }
//                src.put(row,col,bgra);
//            }
//        }
        Utils.matToBitmap(src,bitmap);//将Mat对象转为BitMap对象
        src.release();//记得释放资源
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        return bitmap;
    }
    public static Bitmap LocalBitMap2Gray(Bitmap bitmap,String constant){
        startTime = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
        int index = 0;
        int A=0,R=0,G=0,B=0;
        int color = 0;
        for (int row = 0; row < height; row++){
            index = row * width;
            for (int col = 0; col < width; col++){
                int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                R = (pixel>>16)&0XFF;
                G = (pixel>>8)&0XFF;
                B = pixel&0XFF;
                //开始灰度操作
                color = (R * 38 + G * 75 + B * 15) >> 7;//灰度化算法
                R=G=B=color;
                pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                pixels[index] = pixel;
                index++;
            }
        }
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        bitmap.setPixels(pixels,0,width,0,0,width,height);
        return bitmap;
    }
    public static Bitmap LocalBitMap2Invert(Bitmap bitmap,String constant) {
        startTime = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
        int index = 0;
        int A=0,R=0,G=0,B=0;
        for (int row = 0; row < height; row++){
            index = row * width;
            for (int col = 0; col < width; col++){
                int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                R = (pixel>>16)&0XFF;
                G = (pixel>>8)&0XFF;
                B = pixel&0XFF;
                //开始取反操作
                R = 255 - R;
                G = 255 - G;
                B = 255 - B;
                pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                pixels[index] = pixel;
                index++;
            }
        }
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        bitmap.setPixels(pixels,0,width,0,0,width,height);
        return bitmap;
    }

    /**
     * 像素减法操作
     * @param bitmap
     * @param constant
     * @return
     */
    public static Bitmap subStract(Bitmap bitmap,String constant) {
        startTime = System.currentTimeMillis();
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src); //将bitmap对象转为Mat对象，OpenCV里是用Mat对象来进行图片处理的

        Mat whiteImage = new Mat(src.size(),src.type(), Scalar.all(255));//创建一个白色的图像
        Core.subtract(whiteImage,src,src);

        Utils.matToBitmap(src,bitmap);//将Mat对象转为BitMap对象
        src.release();//记得释放资源
        whiteImage.release();
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        return bitmap;
    }
    /**
     * 像素加法操作
     * @param bitmap
     * @param constant
     * @return
     */
    public static Bitmap addStract(Bitmap bitmap,String constant) {
        startTime = System.currentTimeMillis();
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src); //将bitmap对象转为Mat对象，OpenCV里是用Mat对象来进行图片处理的

        Mat blackImage = new Mat(src.size(),src.type(), Scalar.all(0));//创建一个黑色的图像，最后一个参数的值表示这张图的颜色
        //Core.add(whiteImage,src,src);
        Core.addWeighted(blackImage,0.5,src,0.5,0.0,src);//权重加法操作，因为加的是全黑的图像，所以生成的图像会比原来的暗一点
        Utils.matToBitmap(src,bitmap);//将Mat对象转为BitMap对象
        src.release();//记得释放资源
        blackImage.release();
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        return bitmap;
    }
    /**
     * 像素乘法操作
     * @param bitmap
     * @param constant
     * @return
     */
    public static Bitmap mulStract(Bitmap bitmap,String constant) {
        startTime = System.currentTimeMillis();
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src); //将bitmap对象转为Mat对象，OpenCV里是用Mat对象来进行图片处理的
        src.convertTo(src, CvType.CV_32F);
        Mat whiteImage = new Mat(src.size(),src.type(), Scalar.all(1.25));//创建一个图像，最后一个参数的值表示这张图的颜色
        Mat secondImage = new Mat(src.size(),src.type(), Scalar.all(30));
        //Core.add(whiteImage,src,src);
        Core.multiply(whiteImage,src,src);
        Core.add(secondImage,src,src);//加法和乘法操作
        src.convertTo(src, CvType.CV_8U);
        Utils.matToBitmap(src,bitmap);//将Mat对象转为BitMap对象
        src.release();//记得释放资源
        whiteImage.release();
        secondImage.release();
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        return bitmap;
    }
    public static Bitmap natureStyle(Bitmap bitmap,String style,String constant) {
        startTime = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
        int index = 0;
        int A=0,R=0,G=0,B=0;
        switch (style){
            case "空气":
                for (int row = 0; row < height; row++){
                index = row * width;
                for (int col = 0; col < width; col++){
                    int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                    A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                    R = (pixel>>16)&0XFF;
                    G = (pixel>>8)&0XFF;
                    B = pixel&0XFF;
                    //空气滤镜算法
                    R = (R + G) / 2;
                    G = (R + B) / 2;
                    B = (G + B) / 2;
                    pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                    pixels[index] = pixel;
                    index++;
                }
            }break;
            case "迷雾":
                BitmapUtil.buildFogLookupTable();
                for (int row = 0; row < height; row++){
                    index = row * width;
                    for (int col = 0; col < width; col++){
                        int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                        A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                        R = (pixel>>16)&0XFF;
                        G = (pixel>>8)&0XFF;
                        B = pixel&0XFF;
                        //雾风格
                        R = BitmapUtil.fogLookUp[R];
                        G = BitmapUtil.fogLookUp[G];
                        B= BitmapUtil.fogLookUp[B];
                        pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                        pixels[index] = pixel;
                        index++;
                    }
                }break;
            case "冰冻":
                for (int row = 0; row < height; row++){
                    index = row * width;
                    for (int col = 0; col < width; col++){
                        int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                        A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                        R = (pixel>>16)&0XFF;
                        G = (pixel>>8)&0XFF;
                        B = pixel&0XFF;
                        //冰冻风格
                        R = BitmapUtil.clamp((int)Math.abs((R - G - B) * 1.5));
                        G = BitmapUtil.clamp((int)Math.abs((G - B - R) * 1.5));
                        B = BitmapUtil.clamp((int)Math.abs((B - R - G) * 1.5));
                        pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                        pixels[index] = pixel;
                        index++;
                    }
                }break;
            case "熔岩":
                for (int row = 0; row < height; row++){
                    index = row * width;
                    for (int col = 0; col < width; col++){
                        int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                        A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                        R = (pixel>>16)&0XFF;
                        G = (pixel>>8)&0XFF;
                        B = pixel&0XFF;
                        //熔岩风格
                        int gray = (R + G + B) / 3;
                        R = gray;
                        G = Math.abs(B - 128);
                        B = Math.abs(B - 128);
                        pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                        pixels[index] = pixel;
                        index++;
                    }
                }break;
            case "金属":
                for (int row = 0; row < height; row++){
                    index = row * width;
                    for (int col = 0; col < width; col++){
                        int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                        A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                        R = (pixel>>16)&0XFF;
                        G = (pixel>>8)&0XFF;
                        B = pixel&0XFF;
                        //金属风格
                        float r = Math.abs(R - 64);
                        float g = Math.abs( - 64);
                        float b = Math.abs(g - 64);
                        float gray = ((222 * r + 707 * g + 71 * b) / 1000);
                        r = gray + 70;
                        r = r + (((r - 128) * 100) / 100f);
                        g = gray + 65;
                        g = g + (((g - 128) * 100) / 100f);
                        b = gray + 75;
                        b = b + (((b - 128) * 100) / 100f);
                        R = BitmapUtil.clamp((int)r);
                        G = BitmapUtil.clamp((int)g);
                        B = BitmapUtil.clamp((int)b);
                        pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                        pixels[index] = pixel;
                        index++;
                    }
                }break;
            case "海洋":
                for (int row = 0; row < height; row++){
                    index = row * width;
                    for (int col = 0; col < width; col++){
                        int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                        A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                        R = (pixel>>16)&0XFF;
                        G = (pixel>>8)&0XFF;
                        B = pixel&0XFF;
                        //海洋风格
                        int gray = (R + G + B) / 3;
                        R = BitmapUtil.clamp(gray / 3);
                        G = gray;
                        B = BitmapUtil.clamp(gray * 3);
                        pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                        pixels[index] = pixel;
                        index++;
                    }
                }break;
            case "湖水":
                for (int row = 0; row < height; row++){
                    index = row * width;
                    for (int col = 0; col < width; col++){
                        int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                        A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                        R = (pixel>>16)&0XFF;
                        G = (pixel>>8)&0XFF;
                        B = pixel&0XFF;
                        //湖水风格
                        int gray = (R + G + B) / 3;
                        R = BitmapUtil.clamp(gray - G - B);
                        G = BitmapUtil.clamp(gray - R - B);
                        B = BitmapUtil.clamp(gray - R - G);
                        pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                        pixels[index] = pixel;
                        index++;
                    }
                }break;
            case "彩虹":
                BitmapUtil.buildRainBowLookupTable();
                for (int row = 0; row < height; row++){
                    index = row * width;
                    for (int col = 0; col < width; col++){
                        int pixel = pixels[index];//BitMap的像素是ARGB格式的,也就是4个通道，每个通道8个字节，8888，分别对应ARGB
                        A = (pixel>>24)&0XFF;//假如每位数代表8位，1234右移24位得到的就是1，&0xff表示只取低8位，高位补0
                        R = (pixel>>16)&0XFF;
                        G = (pixel>>8)&0XFF;
                        B = pixel&0XFF;
                        //彩虹风格
                        R = BitmapUtil.rainbowLookUp[R];
                        G = BitmapUtil.rainbowLookUp[G];
                        B = BitmapUtil.rainbowLookUp[B];
                        pixel = ((A&0XFF)<<24) | ((R&0XFF)<<16) | ((G&0XFF)<<8) | (B&0XFF);//再组装回去还给pixel,因为之前把像素拆了重新赋值。
                        pixels[index] = pixel;
                        index++;
                    }
                }break;
        }
        endTime = System.currentTimeMillis() - startTime;
        Log.d(TAG,constant+"Time--->"+endTime);
        bitmap.setPixels(pixels,0,width,0,0,width,height);
        return bitmap;
    }

    /*
    创建图像
     */
    public static Bitmap demoMatUsage() {
        //Mat src = new Mat();
        //Utils.bitmapToMat(bitmap,src);
        //Mat dst = new Mat(src.size(),src.type(),Scalar.all(127));
        Bitmap bitmap = Bitmap.createBitmap(400,600, Bitmap.Config.ARGB_8888);
        //关于通道的问题，8UC4表示是4个通道，那么Scalar里可以接收4个值（RGBA）.8位的指byte，16位的指int,32位的指float或double.
        //rows 行对应高，col 列对应宽
        Mat dst = new Mat(bitmap.getHeight(),bitmap.getWidth(),CvType.CV_8UC3,Scalar.all(0));
        Utils.matToBitmap(dst,bitmap);
        dst.release();
        return bitmap;
    }

    /*
        图像截取
     */
    public static Bitmap getROIArea(Bitmap bitmap){
        Rect roi = new Rect(100,100,200,300);//创建一个矩形
        Bitmap roimap = Bitmap.createBitmap(roi.width,roi.height, Bitmap.Config.ARGB_8888);//创建一个bitmap对象，大小和矩形的一致
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat roiMat = src.submat(roi);//
        Mat roiDstMat = new Mat();
        Imgproc.cvtColor(roiMat,roiDstMat,Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(roiDstMat,roimap);

        roiDstMat.release();
        roiMat.release();
        src.release();
        return roimap;
    }
    /*
    均值模糊，是指比如取n*n的值为m的卷积（眼膜），n的值为奇数，例如3*3的值全为1的矩阵，然后用每个图像像素里的
    每3*3的像素大小去乘以1累加，再除以9得到3*3像素中的中位数的像素大小。如果卷积里的值不完全一致的话，即权重不一样的话，就是高斯模糊

     */
    public static void meanBlur(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);

        Imgproc.blur(src,dst,new Size(25,25),new Point(-1,-1),4);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
   中值滤波器
    */
    public static void medianBlur(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);

        Imgproc.medianBlur(src,dst,15);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
        高斯模糊
     */
    public static void gaussianBlur(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);

        Imgproc.GaussianBlur(src,dst,new Size(11,11),0,0,4);//这里的new Size(11,11),指卷积的算子窗口大小，只能取奇数。
         Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }

    /**
     * 双边模糊滤波
     * @param bitmap
     */
    public static void biBlur(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2BGR);//因为双边模糊只接收单通道或3通道的，当前的是4通道的
        Imgproc.bilateralFilter(src,dst,15,150,15,4);
        //public static void bilateralFilter(Mat src,Mat dst,int d,double sigmaColor,double sigmaSpace,int borderType)
        //d是眼膜窗口大小，一般取15-30窗口越大，参与计算的像素就越多，速度越慢，
        // sigmaColor是颜色空间上的预差值，若两个像素点的差值大于150就参与计算，
        //sigmaSpace是空间上的纬度，borderType是对边缘的处理方法

        //实现锐化
        Mat kernel = new Mat(3,3,CvType.CV_16S);//CvType是每一个通道占多少位
        kernel.put(0,0,0,-1,0,-1,5,-1,0,-1,0);//锐化的算子
        /*关于算子
        均值模糊        拉普拉斯边缘         拉普拉斯锐化
        1   1   1       -1   -1    -1        -1   -1    -1
        1   1   1       -1   8     -1        -1    9    -1
        1   1   1       -1   -1    -1        -1   -1    -1
         */
        Imgproc.filter2D(dst,dst,-1,kernel,new Point(-1,-1),0.0,4);
        //filter2D(src, dst, ddepth, kernel, anchor, delta, borderType)
        //ddepth 输出图像位图深度，常见ARGB位图深度是8，还有16，32
        //anchor 默认克隆位置，卷积眼膜计算之后替换的像素位置
        //delta滤波之后加上的数值
        //borderType
        Utils.matToBitmap(dst,bitmap);
        kernel.release();
        src.release();
        dst.release();
    }
    public static void customFilter(String command, Bitmap bitmap){
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat kernel = getCustomOperator(command);//CvType是每一个通道占多少位
        Imgproc.filter2D(src,dst,-1,kernel,new Point(-1,-1),0.0,4);
        Utils.matToBitmap(dst,bitmap);
        kernel.release();
        src.release();
        dst.release();
    }

    private static Mat getCustomOperator(String command) {
        Mat kernel = new Mat(3,3,CvType.CV_32FC1);
        if(CUSTOM_BLUR_COMMAND.equals(command)){//均值模糊
            kernel.put(0,0,
                    1.0/9.0, 1.0/9.0, 1.0/9.0,
                    1.0/9.0, 1.0/9.0, 1.0/9.0,
                    1.0/9.0, 1.0/9.0, 1.0/9.0);
        } else if(CUSTOM_EDGE_COMMAND.equals(command)){//拉普拉斯边缘
            kernel.put(0,0,
                    -1, -1,  -1,
                    -1,  8,  -1,
                    -1, -1,  -1);
        } else if(CUSTOM_SHARPEN_COMMAND.equals(command)){//拉普拉斯锐化
            kernel.put(0,0,
                    -1, -1,  -1,
                    -1,  9,  -1,
                    -1, -1,  -1);
        }
        return kernel;
    }
    /*  图像形态学
        腐蚀：最小值滤波
        膨胀  最大值滤波 膨胀原理是拿B中的中心点和A上的点比对，若B中有一个点在A的范围内，则用结构元素中的最大值替换该点
        原理：在特殊领域运算形式——结构元素（Sturcture Element），在每个像素位置上与二值图像对应的区域进行特定的逻辑运算。
             运算结构是输出图像的相应像素。运算效果取决于结构元素大小内容以及逻辑运算性质。
             拿B中的中心点和A上的点一个个去对比，如果B上的点都在A的范围内，
             则用结构元素中的最小值替换该点（这里因为全是1，所以min和max相等），否则舍弃
             可以看出C是A的子集，即C属于A
               A腐蚀前        B 结构元素       C腐蚀后         D膨胀后
          0 0 0 0 0 0         0 0 0           0 0 0 0 0 0       0 1 1 1 0 0
          0 0 1 1 1 0         0 1 1           0 0 1 1 0 0       1 1 1 1 1 0
          0 1 1 1 1 0         0 0 1           0 1 1 0 0 0       1 1 1 1 1 0
          0 0 1 1 0 0                         0 0 0 0 0 0       0 1 1 1 0 0
     */
    public static void erodeOrDilation(String command, Bitmap bitmap) {
        boolean isErode = command.equals(ERODE_MIN_FILTER_COMMAND);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 3), new Point(-1, -1));
        if(isErode){
            Imgproc.erode(src,dst,kernel,new Point(-1, -1),1);
        } else{
            Imgproc.dilate(src,dst,kernel,new Point(-1, -1),1);
        }
        //erode(src, dst, kernel, anchor, iterations)
        //kernel 结构元素  anchor 替换的像素的位置 iterations 执行几次，次数越多腐蚀或膨胀越深
        //kernel：CV_SHAPE_RECT 矩形（常用）CV_SHAPE_CROSS 十字形  MORPH_ELLIPSE 椭圆  MORPH_TOPHAT 高帽 CV_SHAPE_CUSTOM
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
        开闭操作
        作用
        开：可以去除主体外的噪点，保留主体，
        团：可以去除主体内的噪点，
     */
    public static void openOrClose(String command, Bitmap bitmap) {
        boolean isOpen = command.equals(OPEN_COMMAND);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        Imgproc.threshold(src,src,0,255,Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 3), new Point(-1, -1));
        if(isOpen){
            Imgproc.morphologyEx(src,dst,Imgproc.MORPH_OPEN,kernel);
        } else{
            Imgproc.morphologyEx(src,dst,Imgproc.MORPH_CLOSE,kernel);
        }
        //morphologyEx(src, dst, op, kernel)
        //op 图像形态学属性
        //kernel 结构元素  anchor 替换的像素的位置 iterations 执行几次，次数越多腐蚀或膨胀越深
        //kernel：CV_SHAPE_RECT 矩形（常用）CV_SHAPE_CROSS 十字形  MORPH_ELLIPSE 椭圆  MORPH_TOPHAT 高帽 CV_SHAPE_CUSTOM
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
    图像形态学直线检测
     */
    public static void morphLineDetection(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        Imgproc.threshold(src,src,0,255,Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(30, 1), new Point(-1, -1));

            Imgproc.morphologyEx(src,dst,Imgproc.MORPH_OPEN,kernel);

        //morphologyEx(src, dst, op, kernel)
        //op 图像形态学属性
        //kernel 结构元素  anchor 替换的像素的位置 iterations 执行几次，次数越多腐蚀或膨胀越深
        //kernel：CV_SHAPE_RECT 矩形（常用）CV_SHAPE_CROSS 十字形  MORPH_ELLIPSE 椭圆  MORPH_TOPHAT 高帽 CV_SHAPE_CUSTOM
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
    域值二值化
     */
    public static void thresholdImg(String command, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        Imgproc.threshold(src,dst,0,255,getType(command));//二值化 如果不自动计算的话 默认域值需要自己设置 例如0改为127
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
        二值化类型
     */
    private static int getType(String command){
        switch (command){
            case ListConstants.THRESHOLD_BINARY_COMMAND:
                return (Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);//THRESH_BINARY 二值化图像 自动计算
            case ListConstants.THRESHOLD_BINARY_INV_COMMAND:
                return (Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);//THRESH_BINARY_INV反二值化 自动计算
            case ListConstants.THRESHOLD_BINARY_TRUNC_COMMAND:
                return (Imgproc.THRESH_TRUNC | Imgproc.THRESH_OTSU);//THRESH_OTSU 二值化截断
            case ListConstants.THRESHOLD_BINARY_TOZERO_COMMAND:
                return (Imgproc.THRESH_TOZERO | Imgproc.THRESH_OTSU);//THRESH_OTSU 二值化取零
            case ListConstants.THRESHOLD_BINARY_TOZERO_INV_COMMAND:
                return (Imgproc.THRESH_TOZERO_INV | Imgproc.THRESH_OTSU);//THRESH_OTSU 二值化取零取反
        }
        return (Imgproc.THRESH_BINARY | Imgproc.THRESH_TRUNC);
    }
    /*
    自定义域值二值化
     */
    public static void manualThresholdImg(int value, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        Imgproc.threshold(src,dst,value,255,Imgproc.THRESH_BINARY);//二值化 如果不自动计算的话 默认域值需要自己设置 例如0改为127
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
    自适应域值-均值C ADAPTIVE_THRESH_MEAN_C
    自适应域值-高斯  ADAPTIVE_THRESH_GAUSSIAN_C
     */
    public static void adaptiveThresholdOrGaussian(int value,String command ,Bitmap bitmap) {
        boolean isGaussian = command.equals(ListConstants.ADAPTIVE_GAUSSIAN_COMMAND);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        Imgproc.adaptiveThreshold(src,dst,255,
                (isGaussian?Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C:Imgproc.ADAPTIVE_THRESH_MEAN_C), Imgproc.THRESH_BINARY,value,0.0);
        //adaptiveThreshold(src, dst, maxValue, adaptiveMethod, thresholdType, blockSize, C)
        //maxValue 0-255 如果是float图像 0-1 那么maxValue就是1 要根据实际图像最大值来确定
        //adaptiveMethod 只支持高斯和均值两种 thresholdType 只支持THRESH_BINARY 和THRESH_BINARY_INV
        //blockSize 把图像分成指定Size的block，Size的大小决定二值化效果的强度。 然后对block计算域值再进行二值化 只能为大于1的奇数
        //C 表示在原图像上加的常量
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    /*
    直方图均衡化,统计每个像素的出现频率
     */
    public static void histogramEq(String process, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        Imgproc.equalizeHist(src,dst);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }

    /**
     * 图像梯度 有X方向和Y方向
     *
     * @param process
     * @param bitmap
     *          算子与像素点做相应相乘再累加得到中心点的像素值
     *              Soble 算子                                    Scharr算子
     *      -1  0  +1       -1  -2  -1                -3   0  +3        -3  10  -3
     *  Gx= -2  0  +2   Gy=  0   0   0           Gx= -10  0  +10    Gy=  0   0   0    G = sqrt(Gx^2 + Gy^2) 或 G = |Gx| + |Gy|
     *      -1  0  +1       +1  +2  +1               -3   0  +3         +3  +10  +3
     */
    public static void sobelGradient(String process, Bitmap bitmap) {
        int type = 0;
        if(process.equals(ListConstants.GRADIENT_SOBEL_X_COMMAND)){
            type = 1;
        }else if(process.equals(ListConstants.GRADIENT_SOBEL_Y_COMMAND)){
            type = 2;
        }else{
            type = 3;//合成梯度
        }
        Mat src = new Mat();
        Mat xdst = new Mat();
        Mat ydst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        if(type == 1){
            Imgproc.Sobel(src,xdst,CvType.CV_16S,1,0);//Soble X方向算子
            Core.convertScaleAbs(xdst,xdst);//取绝对值，因为运算过后可能是负值
            //Sobel(src, dst, ddepth, dx, dy)
            //ddepth 图像深度  -1为跟随原图
            Utils.matToBitmap(xdst,bitmap);
        } else if(type == 2){
            Imgproc.Sobel(src,ydst,CvType.CV_16S,0,1);//Soble Y方向算子
            Core.convertScaleAbs(ydst,ydst);//取绝对值，因为运算过后可能是负值
            Utils.matToBitmap(ydst,bitmap);
        } else{
            Imgproc.Scharr(src,xdst,CvType.CV_16S,1,0);//Soble X方向算子
            Imgproc.Scharr(src,ydst,CvType.CV_16S,0,1);//Soble Y方向算子
            Core.convertScaleAbs(xdst,xdst);//取绝对值，因为运算过后可能是负值
            Core.convertScaleAbs(ydst,ydst);//取绝对值，因为运算过后可能是负值
            Mat dst = new Mat();
            Core.addWeighted(xdst,0.5,ydst,0.5,30,dst);
            Utils.matToBitmap(dst,bitmap);
            dst.release();
        }
        xdst.release();
        ydst.release();
        src.release();
    }

    /**
     * Canny边缘提取
     * @param value
     * @param bitmap
     */
    public static void cannyEdge(int value, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.GaussianBlur(src,src,new Size(3,3),0,0,4);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);//转为灰度图像
        Imgproc.Canny(src,dst,value,value*2,3,false);
        //Canny(image, edges, threshold1, threshold2, apertureSize, L2gradient)
        //threshold1:低域值 threshold2：高域值 一般高是低的2-3倍
        //apertureSize: Soble算子 3*3 L2gradient：true: G = sqrt(Gx^2 + Gy^2) 或 false: G = |Gx| + |Gy|  一般选择false，计算速度原因
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }

    /**
     * 霍夫直线检测
     * 步骤：
     * @param value
     * @param bitmap
     */
    public static void houghLinesDet(int value, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Mat lines = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0, 4);
        Imgproc.Canny(src, dst, value, value*2, 3, false);
        Mat drawImg = new Mat(src.size(), src.type());
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180.0, value);
        double[] linep = new double[2];
        for(int i=0; i<lines.cols(); i++) {
            linep = lines.get(0, i);
            double rho = linep[0];
            double theta = linep[1];
            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0 = a*rho;
            double y0 = b*rho;
            Point p1 = new Point(x0+1000*(-b), y0 + 1000*a);
            Point p2 = new Point(x0-1000*(-b), y0 - 1000*a);
            Imgproc.line(drawImg, p1, p2, new Scalar(255, 0, 0, 0), 2, 8, 0);
        }

        // 直接得到直线的方法
        /*Imgproc.HoughLinesP(dst, lines, 1, Math.PI/180, value, 15, 3);
        double[] pts = new double[4];
        for(int i=0; i<lines.cols(); i++) {
            pts = lines.get(0, i);
            Point p1 = new Point(pts[0], pts[1]);
            Point p2 = new Point(pts[2], pts[3]);
            Imgproc.line(drawImg, p1, p2, new Scalar(255, 0, 0, 0), 2, 8, 0);
        }*/

        Utils.matToBitmap(drawImg, bitmap);
        src.release();
        lines.release();
        drawImg.release();
        dst.release();
    }
}

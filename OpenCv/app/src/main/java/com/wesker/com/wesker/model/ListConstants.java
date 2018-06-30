package com.wesker.com.wesker.model;

/**
 * Created by Hello，Mr.Zhang on 2017/7/19.
 */

public interface ListConstants {
    public static final String TEST_ENV_COMMAND = "环境测试-灰度";
    public static final String  MAT_PIXEL_INVERT_COMMAND= "Mat像素操作-取反";
    public static final String  BITMAP_PIXEL_INVERT_COMMAND= "BitMap像素操作-取反";
    public static final String BITMAP_GRAY_COMMAND = "BitMap灰度化操作";
    public static final String  MAT_PIXEL_SUBSTARCT_COMMAND= "Mat像素操作-相减";
    public static final String  MAT_PIXEL_ADDSTARCT_COMMAND= "Mat像素操作-相加";
    public static final String  MAT_PIXEL_MULSTARCT_COMMAND= "Mat像素操作-相乘";
    public static final String  MAT_PIXEL_NATURE_COMMAND= "BitMap自然滤镜";
    public static final String  IMAGE_CONTENT_COMMAND= "图像容器";
    public static final String  SUB_IMAGE_COMMAND= "图像截取";
    public static final String  BLUR_IMAGE_COMMAND= "均值模糊";
    public static final String  GAUSSIAN_BLUR_IMAGE_COMMAND= "高斯模糊";
    public static final String  MEDIAN_FILTER_IMAGE_COMMAND= "中值滤波器";
    public static final String  BI_BLUR_COMMAND= "双边模糊";
    public static final String  CUSTOM_BLUR_COMMAND= "自定义算子-模糊";
    public static final String  CUSTOM_EDGE_COMMAND= "自定义算子-边缘";
    public static final String  CUSTOM_SHARPEN_COMMAND= "自定义算子-锐化";
    public static final String  ERODE_MIN_FILTER_COMMAND= "腐蚀-最小值滤波";
    public static final String  DILATION_MAX_FILTER_COMMAND= "膨胀-最大值滤波";
    public static final String  OPEN_COMMAND= "开操作";
    public static final String  CLOSE_COMMAND= "闭操作";
    public static final String  MORPH_LINE_COMMAND= "形态学直线检测";
    public static final String  THRESHOLD_BINARY_COMMAND= "域值二值化";
    public static final String  THRESHOLD_BINARY_INV_COMMAND= "域值反二值化";
    public static final String  THRESHOLD_BINARY_TRUNC_COMMAND= "域值化-截断";
    public static final String  THRESHOLD_BINARY_TOZERO_COMMAND= "域值化-取零";
    public static final String  THRESHOLD_BINARY_TOZERO_INV_COMMAND= "域值化-取零-取反";
    public static final String  ADAPTIVE_THRESHOLD_COMMAND = "自适应域值-均值C";
    public static final String  ADAPTIVE_GAUSSIAN_COMMAND = "自适应域值-高斯";
    public static final String  HISTOGRAM_EQ_COMMAND = "直方图均衡化";
    public static final String  GRADIENT_SOBEL_X_COMMAND = "X方向梯度";
    public static final String  GRADIENT_SOBEL_Y_COMMAND = "Y方向梯度";
    public static final String  GRADIENT_IMG_COMMAND = "XY梯度";
    public static final String  CANNY_EDGE_COMMAND = "边缘提取";
    public static final String  HOUGH_LINE_COMMAND = "霍夫直线检测";


    //public static final int TEST_OBJ_COMMAND = 2;
    public static final String[] NATURE_STYLE={"空气","迷雾","冰冻","熔岩","金属","海洋","湖水","彩虹"};//滤镜效果列表
}

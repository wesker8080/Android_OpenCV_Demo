package com.wesker.com.wesker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hello，Mr.Zhang on 2017/7/19.
 */

public class ListData implements ListConstants {
    private long id;
    private String name;
    private String command;
    public ListData(String command,long id){
        this.command = command;
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public static List<ListData> getCommandList(){
        List<ListData> mCommandList = new ArrayList<>();
        mCommandList.add(new ListData(TEST_ENV_COMMAND, 1));
        mCommandList.add(new ListData(BITMAP_GRAY_COMMAND, 2));
        mCommandList.add(new ListData(MAT_PIXEL_INVERT_COMMAND, 3));
        mCommandList.add(new ListData(BITMAP_PIXEL_INVERT_COMMAND, 4));
        mCommandList.add(new ListData(MAT_PIXEL_SUBSTARCT_COMMAND, 5));
        mCommandList.add(new ListData(MAT_PIXEL_ADDSTARCT_COMMAND, 6));
        mCommandList.add(new ListData(MAT_PIXEL_MULSTARCT_COMMAND, 7));
        mCommandList.add(new ListData(MAT_PIXEL_NATURE_COMMAND, 8));
        mCommandList.add(new ListData(IMAGE_CONTENT_COMMAND, 9));
        mCommandList.add(new ListData(SUB_IMAGE_COMMAND, 10));
        mCommandList.add(new ListData(BLUR_IMAGE_COMMAND, 11));
        mCommandList.add(new ListData(GAUSSIAN_BLUR_IMAGE_COMMAND, 12));
        mCommandList.add(new ListData(MEDIAN_FILTER_IMAGE_COMMAND, 13));
        mCommandList.add(new ListData(BI_BLUR_COMMAND, 14));
        mCommandList.add(new ListData(CUSTOM_BLUR_COMMAND, 15));
        mCommandList.add(new ListData(CUSTOM_EDGE_COMMAND, 16));
        mCommandList.add(new ListData(CUSTOM_SHARPEN_COMMAND, 17));
        mCommandList.add(new ListData(ERODE_MIN_FILTER_COMMAND, 18));
        mCommandList.add(new ListData(DILATION_MAX_FILTER_COMMAND, 19));
        mCommandList.add(new ListData(OPEN_COMMAND, 20));
        mCommandList.add(new ListData(CLOSE_COMMAND, 21));
        mCommandList.add(new ListData(MORPH_LINE_COMMAND, 22));
        mCommandList.add(new ListData(THRESHOLD_BINARY_COMMAND, 23));
        mCommandList.add(new ListData(THRESHOLD_BINARY_INV_COMMAND, 24));
        mCommandList.add(new ListData(THRESHOLD_BINARY_TRUNC_COMMAND, 25));
        mCommandList.add(new ListData(THRESHOLD_BINARY_TOZERO_COMMAND, 26));
        mCommandList.add(new ListData(THRESHOLD_BINARY_TOZERO_INV_COMMAND, 27));
        //自适应域值
        mCommandList.add(new ListData(ADAPTIVE_THRESHOLD_COMMAND, 28));
        mCommandList.add(new ListData(ADAPTIVE_GAUSSIAN_COMMAND, 29));
        //直方图均衡化
        mCommandList.add(new ListData(HISTOGRAM_EQ_COMMAND, 30));
        //图像梯度
        mCommandList.add(new ListData(GRADIENT_SOBEL_X_COMMAND, 31));
        mCommandList.add(new ListData(GRADIENT_SOBEL_Y_COMMAND, 32));
        mCommandList.add(new ListData(GRADIENT_IMG_COMMAND, 33));
        //边缘提取
        mCommandList.add(new ListData(CANNY_EDGE_COMMAND, 34));
        //霍夫直线检测
        mCommandList.add(new ListData(HOUGH_LINE_COMMAND,35));
        return mCommandList;
    }
}

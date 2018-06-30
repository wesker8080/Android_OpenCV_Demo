package com.wesker.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.wesker.com.wesker.model.ListConstants;
import com.wesker.listener.DialogItemListener;

/**
 * Created by Hello，Mr.Zhang on 2017/7/21.
 */

public class UiUtils {
    private  final String TAG = "UiUtils";
    private  DialogItemListener liss;
    /*列表对话框*/
    private Context context;
    public UiUtils(Context context){
        this.context = context;
    }
    public  void showDialog(final Context context,final DialogItemListener listener) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("选择风格");//设置标题
        builder.setItems(ListConstants.NATURE_STYLE,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,which+"",Toast.LENGTH_SHORT).show();

                listener.OnItemClick(ListConstants.NATURE_STYLE[which]);

               //Toast.makeText(context,"我点击了"+ListConstants.NATURE_STYLE[which],Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog=builder.create();//获取dialog
        dialog.show();//显示对话框
    }
    public  void setListener(DialogItemListener lis){
        liss = lis;
    }
    public  Context getApplictionContext(){
        return context;
    }
}

package com.wesker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wesker.R;
import com.wesker.adapter.MlistViewAdapter;
import com.wesker.com.wesker.model.ListConstants;
import com.wesker.com.wesker.model.ListData;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String command = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView)findViewById(R.id.listview);
        MlistViewAdapter mlistViewAdapter = new MlistViewAdapter(getApplicationContext());
        listView.setAdapter(mlistViewAdapter);
        listView.setOnItemClickListener(this);
        mlistViewAdapter.getModel().addAll(ListData.getCommandList());
        mlistViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tag = view.getTag(R.id.data);
        if(tag instanceof ListData){
            ListData listData = (ListData)tag;
            command = listData.getCommand();
            Log.d("zzk","position:"+listData.getCommand()+"----id:"+id);
        }
        processData();
    }

    private void processData() {
        if(command.equals(ListConstants.THRESHOLD_BINARY_COMMAND) ||
                command.equals(ListConstants.ADAPTIVE_THRESHOLD_COMMAND) ||
                command.equals(ListConstants.ADAPTIVE_GAUSSIAN_COMMAND ) ||
                command.equals(ListConstants.CANNY_EDGE_COMMAND) ||
                command.equals(ListConstants.HOUGH_LINE_COMMAND)){
            Intent intent = new Intent(this,ThresholdProcessActivity.class);
            intent.putExtra("process",command);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this,ProcessImageActivity.class);
            intent.putExtra("process",command);
            startActivity(intent);
        }
    }
}

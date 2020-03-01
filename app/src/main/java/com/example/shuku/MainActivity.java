package com.example.shuku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shuku.dataHandler.DataLoad;
import com.example.shuku.dataHandler.Datahandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView main_gr;
    Button start,clear;
    TextView sign,len,index;
    ArrayList<ChunkBean> mdata;
    ImageButton left,right;
    Datahandler.ChangeUI changeUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化UI
        initview();
        //初始化数据
        mdata=new ArrayList<>();
        initData();
        //设置main_recyclerview
        main_gr.setLayoutManager(new GridLayoutManager(MainActivity.this,9));
        main_gr.setAdapter(new MainGrAdapter(this,mdata));
        changeUI=new Datahandler.ChangeUI() {
            @Override
            //设置一系列的提示
            public void StartSign() {
                sign.setText("计算中...");
            }

            @Override
            public void ChangeLen() {
                String s="发现"+DataLoad.all_results.size()+"个结果";
                len.setText(s);
            }

            @Override
            public void changeSign() {
                sign.setText("计算完成");
            }

            @Override
            public void ChangeScreen() {
                ArrayList<ArrayList<Integer>> results= DataLoad.all_results.get(Datahandler.getCurrentPosition()-1);
                String s=String.valueOf(Datahandler.getCurrentPosition());
                index.setText(s);
                //将数据取出，添加到bean中
                for (int num=0;num<9;num++){
                    ArrayList<Integer> result=results.get(num);
                    for (int y = 0; y < 9; y++) {
                        int position=xyToPosition(result, y);
                        mdata.get(position).setNum(String.valueOf(num+1));
                    }
                }
                //显示到屏幕里
                flush();
            }
        };
        //设置回调接口
        Datahandler.addMayResultHandler.setChangeUI(changeUI);
    }

    private int xyToPosition(ArrayList<Integer> result, int y) {
        int x=result.get(y);
        int postion=(8-y)*9+x;
        return postion;
    }

    //设置控件的点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_start:
                start();
                //如果点了一次就不让点了
                break;
            case R.id.btu_clear:
                //清空数据过于麻烦，直接重启
                restartApplication();
                break;
            //检测是否计算完成，已经是否是第一个或最后一个
            case R.id.left_arrows:
                if (Datahandler.getCanChangePosition() && Datahandler.getCurrentPosition()>1) {
                    Datahandler.subPositon();
                    Datahandler.addMayResultHandler.getChangeUI().ChangeScreen();
                     }
                break;
            case R.id.right_arrows:
                if (Datahandler.getCanChangePosition() && Datahandler.getCurrentPosition()<DataLoad.all_results.size()) {
                    Datahandler.addPosition();
                    Datahandler.addMayResultHandler.getChangeUI().ChangeScreen();
                }
        }
    }
    private void start() {
        sign.setText("开始计算...");
        //收集
        DataLoad.collect(mdata);
        //处理
        DataLoad.load();
        //检测数据是否已经被收集过一次
    }
    private void flush(){
        main_gr.setAdapter(new MainGrAdapter(this,mdata));
    }
    void initview(){
        main_gr=findViewById(R.id.main_gr);
        start=findViewById(R.id.btu_start);
        len=findViewById(R.id.tv_lenOfResults);
        start.setOnClickListener(this);
        clear=findViewById(R.id.btu_clear);
        clear.setOnClickListener(this);
        sign=findViewById(R.id.tv_sign);
        index=findViewById(R.id.index);
        left=findViewById(R.id.left_arrows);
        right=findViewById(R.id.right_arrows);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
    }
    void initData(){
        for (int i = 0; i < 81; i++) {
            ChunkBean bean=new ChunkBean();
            mdata.add(bean);
        }
    }
    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

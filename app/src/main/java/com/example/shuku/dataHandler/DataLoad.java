package com.example.shuku.dataHandler;

import com.example.shuku.ChunkBean;

import java.util.ArrayList;
import java.util.List;

public class DataLoad {
    public static ArrayList<ArrayList<Integer>> getUnable_x() {
        return unable_x;
    }

    public static ArrayList<ArrayList<ArrayList<Integer>>> getAll_locations() {
        return all_locations;
    }
    //所有可能的结果
    public static ArrayList<ArrayList<ArrayList<Integer>>> all_results;
    //所有数字在每行的位置
    private static ArrayList<ArrayList<Integer>> unable_x;
    //所有数字的位置
    private static ArrayList<ArrayList<ArrayList<Integer>>> all_locations;
    //所有可能的结果
    public static ArrayList<ArrayList<ArrayList<Integer>>> mayResults;
    public static void collect(List<ChunkBean> data){
        //填充数据空壳
        all_results=new ArrayList<ArrayList<ArrayList<Integer>>>();
        unable_x=new ArrayList<ArrayList<Integer>>();
        all_locations= new ArrayList<ArrayList<ArrayList<Integer>>>();
        mayResults=new ArrayList<ArrayList<ArrayList<Integer>>>();
        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> g=new ArrayList<Integer>();
            unable_x.add(g);
            ArrayList<ArrayList<Integer>> ghost=new ArrayList<ArrayList<Integer>>();
            ArrayList<ArrayList<Integer>> Mgost=new ArrayList<ArrayList<Integer>>();
            mayResults.add(Mgost);
            all_locations.add(ghost);
        }
        //便利所有的数据
        for (ChunkBean bean:data){
            String index= bean.getNum();
            //只对有数字的进行收集
            if (!("".equals(index))) {
                int x,y;
                index=index.substring(0,1);
                //收集每个数字所有的坐标
                x=bean.getLocation()[0];
                y=bean.getLocation()[1];
                ArrayList<Integer> xy=new ArrayList<>();
                xy.add(x);
                xy.add(y);
                all_locations.get(dataConvert.StringToInt(index)-1).add(xy);
                //收集每行占用的x
                unable_x.get(y).add(x);
            }
       }
    }
    public static void load(){
        Datahandler.CountMayResults();
    }
}

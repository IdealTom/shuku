package com.example.shuku.dataHandler;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class Datahandler {

    private static boolean canChangePosition=false;
    private static int CurrentPosition=0;
    private static final int START_SIGN=0;
    private static final int ADD_MAY_RESULT=1;
    private static final int CHANGE_SIGN=2;
    private static final int CHANGE_LEN=3;
    private static final int CHANGE_SCREEN=4;
    private static final addMayResultHandler handler=new addMayResultHandler();
    public static boolean getCanChangePosition() {
        return canChangePosition;
    }
    public static int getCurrentPosition() {
        return CurrentPosition;
    }
    public static void addPosition(){
        CurrentPosition+=1;
    }
    public static void subPositon() { CurrentPosition -= 1; }

    public interface ChangeUI{
        void StartSign();
        void ChangeScreen();
        void changeSign();
        void ChangeLen();
    }
    public static class addMayResultHandler extends Handler {
        private int finish=0;

        public static void setChangeUI(ChangeUI changeUI) {
            addMayResultHandler.changeUI = changeUI;
        }

        public static ChangeUI getChangeUI() {
            return changeUI;
        }

        private static ChangeUI changeUI;
        private boolean add(){
            ++finish;
            return finish == 9;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //添加到指定的数字
            switch (msg.what) {
                case START_SIGN:
                    changeUI.StartSign();
                case ADD_MAY_RESULT:
                    DataLoad.mayResults.get(msg.arg1).add((ArrayList<Integer>) msg.obj);
                    break;
                case CHANGE_LEN:
                    changeUI.ChangeLen();
                    break;
                case CHANGE_SIGN:
                    changeUI.changeSign();
                    break;
                case CHANGE_SCREEN:
                    changeUI.ChangeScreen();
            }
        }
    }
    //判断这一行是否已有当前数字
    private static int IfThere(int CurrentY, ArrayList<ArrayList<Integer>> xys){
        for (List<Integer> xy:xys) {
            if (CurrentY == xy.get(1)) {
                return xy.get(0);
            }
        }
        return -1;
    }
    //判断当前数字是否占用其它数字
    private static Boolean SameX(int NextX, ArrayList<Integer> unablex){
        for (int x:unablex){
            if (NextX==x) {
                return true;
            }
        }

        return false;
    }
    private static Boolean SameArea(int CurrentY,int NextX,ArrayList<ArrayList<Integer>> xys){
        for (ArrayList<Integer> xy:xys){
            if (xy.get(0)>CurrentY && xy.get(0)/3==NextX/3 && xy.get(1)/3==CurrentY/3) {
                return true;
            }
        }
        return false;
    }
    //判断是否与该数字的其它位置在同一宫中
    private static Boolean SameAreaSelf(int CurrentY,int NextX,
                                    ArrayList<Integer> may_result){
        for (int i = 0; i < CurrentY; i++) {
            if (may_result.get(i) - NextX == 0 || (i / 3 == CurrentY / 3 && NextX /3 == may_result.get(i) / 3)) {
                return true;
            }
        }
        return false;
    }
    //判断当前数字是否与该数字其它行的x重复
    private static boolean Occupy(int NextX, ArrayList<ArrayList<Integer>> xys) {
        for (ArrayList<Integer> xy:xys){
            if (xy.get(0)==NextX){
                return true;
            }
        }
        return false;
    }
    private static Boolean checkMatch(ArrayList<ArrayList<Integer>> results,ArrayList<Integer> may_result){
            for (ArrayList<Integer> result:results) {
                for (int i = 0; i < 9; i++){
                    if (result.get(i) == may_result.get(i)) {
                        return false;
                    }
                }
            }
        return true;
    }
    private static boolean isaBoolean(ArrayList<Integer> may_result,
                                      ArrayList<ArrayList<Integer>> xys,
                                      int size, ArrayList<Integer> unablex,
                                      int nextX) {
        return !Occupy(nextX, xys) && !SameX(nextX, unablex)
                && !SameAreaSelf(size,nextX,may_result) && !SameArea(size,nextX,xys);
    }


    static void CountMayResults(){
        //初始化数据
        //对没一个数字进行统计
        for (int num = 0; num < 9; num++) {
            final int Num = num;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    CountMayResults(Num);
                    //当九个数字的可能位置统计完成时
                    if (handler.add()) {
                        ArrayList<ArrayList<Integer>> results=new ArrayList<ArrayList<Integer>>();
                        //匹配最终结果，result仅仅在计算时使用，最终复制到Dataload.results
                        match(DataLoad.mayResults,results);
                        //匹配完成时即可改变结果索引
                        canChangePosition=true;
                        handler.sendEmptyMessage(CHANGE_SIGN);
                    }
                }
            }.start();
        }
    }
    private static void CountMayResults(int num) {

        ArrayList<Integer> may_result=new ArrayList<>();
        //调取数据
        ArrayList<ArrayList<Integer>> xys=DataLoad.getAll_locations().get(num);
        ArrayList<ArrayList<Integer>> unable_x=DataLoad.getUnable_x();
        //看看当前行是否有次数字
        //may_result.size()即为当前行坐标
        int size=may_result.size();
        int x=IfThere(size,xys);
        //当前行没有此数字时，筛选可能的位置
        if (x==-1) {
            //调取当行的unablex
            ArrayList<Integer> unablex=unable_x.get(size);
            for (int NextX = 0; NextX < 9; NextX++) {;
                //不占用其它数字，不与该数字的其它行重复，即可行
                if (!SameX(NextX,unablex) && !Occupy(NextX,xys) && !SameArea(size,NextX,xys)){
                    may_result.add(NextX);
                    CountMayResults(num,xys,unable_x,may_result);
                    //回到上一层循环时回到原始
                    may_result.remove(size);
                }
            }
        }
        else {
            may_result.add(x);
            CountMayResults(num,xys,unable_x,may_result);
            may_result.remove(size);
        }
    }
    private static void CountMayResults(
                                int num,
                                ArrayList<ArrayList<Integer>> xys,
                                ArrayList<ArrayList<Integer>> unable_x,
                                ArrayList<Integer> may_result)
    {
        int size=may_result.size();
        //当集齐九个时进行保存
        if (size==9){
            Object result;
            result= may_result.clone();
            handler.obtainMessage(ADD_MAY_RESULT,num,0,result).sendToTarget();

       }
        else {
            int x=IfThere(size,xys);
            //当前行没有此数字时，筛选可能的位置
            if (x==-1) {
                ArrayList<Integer> unablex=unable_x.get(size);
                for (int NextX = 0; NextX < 9; NextX++) {
                    //不占用其它数字，不与该数字的其它行重复，即可行
                    if (isaBoolean(may_result, xys, size, unablex, NextX)){
                        may_result.add(NextX);
                        CountMayResults(num,xys,unable_x,may_result);
                        //回到上一层循环时回到原始
                        may_result.remove(size);
                    }
                }
            }
            else {
                may_result.add(x);
                CountMayResults(num,xys,unable_x,may_result);
                may_result.remove(size);
            }
        }
    }
    private static void match(ArrayList<ArrayList<ArrayList<Integer>>> mayResults,
                              ArrayList<ArrayList<Integer>> results){
        int len=results.size();
        //如果已经达到九个就复制的results
        if (len==9) {
            DataLoad.all_results.add((ArrayList<ArrayList<Integer>>) results.clone());
            if (DataLoad.all_results.size()==1){
                CurrentPosition=1;
                handler.sendEmptyMessage(CHANGE_SCREEN);
            }
            handler.sendEmptyMessage(CHANGE_LEN);
        }
        //继续匹配
        else {
            for (ArrayList<Integer> may_result : mayResults.get(len)) {
                if (len==0) {
                    results.add(may_result);
                    match(mayResults,results);
                    results.remove(len);
                }
                else if (checkMatch(results, may_result)) {
                        results.add(may_result);
                    match(mayResults, results);
                    results.remove(len);

                }
            }
        }
    }
}

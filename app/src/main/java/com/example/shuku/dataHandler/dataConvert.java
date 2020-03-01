package com.example.shuku.dataHandler;

public class dataConvert {
    public static int StringToInt(String s){
        return Integer.valueOf(s);
    }
    public static byte[] intToByteArray(int value){
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (value & 0xFF);
        byteArray[1] = (byte) (value >> 8  & 0xFF);
        byteArray[2] = (byte) (value >> 16 & 0xFF);
        byteArray[3] = (byte) (value >> 24 & 0xFF);
        return byteArray;
    }
    public static int byteArrayToInt(byte[] byteArray){
        if(byteArray.length != 4){
            return 0;
        }
        int value = byteArray[0] & 0xFF;
        value |= byteArray[1] << 8;
        value |= byteArray[2] << 16;
        value |= byteArray[3] << 24;
        return value;
    }

    public static void main(String[] args) {
        int a=6;
        byte[] data=intToByteArray(a);
        int b=byteArrayToInt(data);
        System.out.println(b);
    }
}

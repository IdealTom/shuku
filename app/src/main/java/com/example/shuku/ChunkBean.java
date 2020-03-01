package com.example.shuku;

public class ChunkBean {
    private String num;

    public String getNum() {
        return num;
    }
    ChunkBean(){
        num="";
    }
    public void setNum(String num) {
        this.num = num;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    private int[] location;

}

package com.tse.www.game;

import java.io.Serializable;
import java.util.ArrayList;

public class FileHandler implements Serializable {

    //variables which hold the data from save game
    public ArrayList<Integer> list = new ArrayList<Integer>();
    public int count;
    public int dim;

    public ArrayList<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
}

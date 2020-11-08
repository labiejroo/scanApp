package com.scandit.datacapture.matrixscanbubblessample.models;

import java.util.ArrayList;
import java.util.List;

public class AppManager {

    public int listOfThings = 0;
    public List<MyItem> myItems = new ArrayList<>();
    private static AppManager mInstance = null;

    protected AppManager(){}

    public static synchronized AppManager getInstance(){
        if(mInstance == null){
            mInstance = new AppManager();
        }
        return mInstance;
    }

    public List<MyItem> getMyItems() {
        return this.myItems;
    }

    public void setMyItems(List<MyItem> myItems)
    {
        this.myItems = myItems;
    }
}

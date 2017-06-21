package com.example.tanxiaokao.brainwave;

/**
 * Created by tanxiaokao on 2017/5/11.
 */

public class InformationListItem {
    private int imageId;
    private String title;
    private String describe;
    private int image;





    public InformationListItem(int imageId,String title,String describe,int image){
        this.imageId = imageId;
        this.title = title;
        this.describe = describe;
        this.image = image;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescribe() {
        return describe;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


}
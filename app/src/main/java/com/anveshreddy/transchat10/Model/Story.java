package com.anveshreddy.transchat10.Model;

public class Story {
    private  String imageURL;
    private String userid;
    private String storyid;
    private long storyStart;
    private long storyEnd;

    public Story(String imageURL,long storyStart,long storyEnd,String userid,String storyid) {
        this.imageURL = imageURL;
        this.storyStart = storyStart;
        this.storyEnd = storyEnd;
        this.userid = userid;
        this.storyid = storyid;

    }

    public Story() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getStoryEnd() {
        return storyEnd;
    }

    public void setStoryEnd(long storyEnd) {
        this.storyEnd = storyEnd;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public long getStoryStart() {
        return storyStart;
    }

    public void setStoryStart(long storyStart) {
        this.storyStart = storyStart;
    }
}

package com.anveshreddy.transchat10.Adapters;

import android.content.Context;

import com.anveshreddy.transchat10.Model.Post;

import java.util.List;

public class MyPhotosAdapter {
    private Context mContext;
    private List<Post> mPosts;

    public MyPhotosAdapter(Context mContext,List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts=mPosts;
    }

}

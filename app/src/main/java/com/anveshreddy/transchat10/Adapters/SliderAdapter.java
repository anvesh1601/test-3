package com.anveshreddy.transchat10.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.anveshreddy.transchat10.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater li;
    int description[]={
            R.string.slider1_firsttext,
            R.string.slider1_Secondtext,
            R.string.slider1_thirdtext,
            R.string.slider1_fouthtext,


    };

    public SliderAdapter(Context context) {
        this.context = context;
    }
    int images[]={
            R.drawable.slider1_first,
    R.drawable.slider1_second,
    R.drawable.slider1_third,
    R.drawable.slider1_fourth,
    };

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        li=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
View v= li.inflate(R.layout.slides,container,false);

        ImageView  iv= v.findViewById(R.id.slider1);
        TextView tv=v.findViewById(R.id.slider1text);

        iv.setImageResource(images[position]);
        tv.setText(description[position]);
        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((ConstraintLayout) object);
    }
}


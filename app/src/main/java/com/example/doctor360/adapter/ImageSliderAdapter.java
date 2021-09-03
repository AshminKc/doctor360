package com.example.doctor360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doctor360.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private int[] mSliderItems = new int[ ] {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5};

    public ImageSliderAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.image_slider_single_item, container, false);
        ImageView imageView = view.findViewById(R.id.featured_image);

        Picasso.with(context)
                .load(mSliderItems[position])
                .placeholder(R.drawable.noimage).
                into(imageView);

        ((ViewPager)container).addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mSliderItems.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}

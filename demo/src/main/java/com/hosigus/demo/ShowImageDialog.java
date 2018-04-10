package com.hosigus.demo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.hosigus.imageloader.ImageLoader;

/**
 * Created by Hosigus on 2018/4/7.
 */

public class ShowImageDialog extends Dialog {
    private String address;
    public ShowImageDialog(@NonNull Context context,String address) {
        super(context);
        this.address = address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_recycleview);
        ImageView iv = findViewById(R.id.iv);
        ImageLoader.with(getContext()).load(address).into(iv,false);
    }
}

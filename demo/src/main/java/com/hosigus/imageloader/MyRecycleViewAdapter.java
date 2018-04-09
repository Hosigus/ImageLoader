package com.hosigus.imageloader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hosigus.imageloader.CompressStrategies.ScaleCompress;
import com.hosigus.imageloader.Options.CompressOptions;

import java.util.List;

/**
 * Created by 某只机智 on 2018/4/7.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> {
    private List<String> urlList;
    private Context context;
    private int page = 1;
    private ImageLoader.ImageLoaderBuilder builder;

    public MyRecycleViewAdapter(List<String> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;
        builder = ImageLoader.with(context).withOriginal(true)
                .place(R.drawable.load)
                .error(R.drawable.error);
    }

    public void refresh() {
        page = 0;
        urlList.clear();
        NetUtils.requestNet(++page, new NetUtils.NetCallBack() {
            @Override
            public void connectOK(List<String> newUrlList) {
                urlList.addAll(newUrlList);
                notifyDataSetChanged();
            }

            @Override
            public void connectFail(String res) {

            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.loadImage(urlList.get(position));
        if (position == getItemCount() - 1) {
            NetUtils.requestNet(++page, new NetUtils.NetCallBack() {
                @Override
                public void connectOK(List<String> newUrlList) {
                    add(newUrlList);
                }

                @Override
                public void connectFail(String res) {

                }
            });
        }
    }

    public void add(List<String> newUrlList) {
        int p = getItemCount();
        urlList.addAll(newUrlList);
        notifyItemInserted(p);
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
        }

        void loadImage(final String url) {
            Log.d("Adapter Test", "loadImage: "+url+" w/h "+imageView.getWidth()+"/"+imageView.getHeight());
            builder.load(url).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ShowImageDialog(context, url).show();
                }
            });
        }
    }
}

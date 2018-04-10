package com.hosigus.demo;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Hosigus on 2018/4/7.
 */

public class NetUtils {
    private final static Handler handler = new Handler();
    public static void requestNet(final int page,final NetCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url=new URL("http://gank.io/api/data/%e7%a6%8f%e5%88%a9/15/"+page);
                    conn=(HttpURLConnection)url.openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");

                    final int resCode=conn.getResponseCode();
                    if(resCode== HttpsURLConnection.HTTP_OK){
                        InputStream inputStream=conn.getInputStream();
                        Scanner in=new Scanner(inputStream);
                        StringBuilder builder=new StringBuilder();
                        while (in.hasNextLine()){
                            builder.append(in.nextLine()).append("\n");
                        }
                        final String resStr=builder.toString();
                        JSONArray array = new JSONObject(resStr).getJSONArray("results");
                        final List<String> urlList = new ArrayList<>();
                        for (int i = 0, length = array.length(); i < length; i++) {
                            urlList.add(array.getJSONObject(i).getString("url"));
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.connectOK(urlList);
                            }
                        });
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.connectFail(String.valueOf(resCode));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.connectFail("请检查网络");
                        }
                    });
                }
            }
        }).start();
    }

    interface NetCallBack {
        void connectOK(List<String> urlList);
        void connectFail(String res);
    }

}

package com.littlejie.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final String URL = "http://www.baidu.com";
    private static final String TAG = MainActivity.class.getSimpleName();
    private OkHttpClient mClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public void get() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Gist gist = gson.fromJson(response.body().charStream(), Gist.class);
        for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().content);
        }
    }

    static class Gist {
        Map<String, GistFile> files;
    }

    static class GistFile {
        String content;
    }

}

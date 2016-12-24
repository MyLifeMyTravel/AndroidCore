package com.littlejie.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.littlejie.core.net.HttpManager;
import com.littlejie.core.net.RequestCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpManager.init();
        mTv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpManager.getAsync(Constant.API_GREETING + "?name=厉圣杰", new RequestCallback() {
                    @Override
                    public void onFailure(Request call, IOException e) {
                        Log.d("TAG", "failed");
                    }

                    @Override
                    public void after() {

                    }

                    @Override
                    public void before() {

                    }

                    @Override
                    public void onResponse(Response response, String body) {
                        mTv.setText(body);
                    }
                });
            }
        });

        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpManager.postAsync(Constant.API_PARSE, "小明", new RequestCallback() {
                    @Override
                    public void before() {

                    }

                    @Override
                    public void onResponse(Response response, String body) {
                        if (response.isSuccessful()) {
                            mTv.setText(body);
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d("TAG", "failed");
                    }

                    @Override
                    public void after() {

                    }
                });
            }
        });

        findViewById(R.id.btn_post_json).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 1; i++) {
                    Person person = new Person(i, "厉圣杰", "浙江省杭州市西湖区文三西路");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("host", "99.99.99.99");
                    HttpManager.postJsonAsync(Constant.API_PERSON_PARSE, person, map, new RequestCallback() {
                        @Override
                        public void before() {

                        }

                        @Override
                        public void onResponse(Response response, String body) {
                            Log.d(TAG, Thread.currentThread().getName());
                            Log.d(TAG, "code = " + response.code() + "\nmessage = " + response.message()
                                    + "\nmethod = " + response.protocol().name());
                            StringBuilder sb = new StringBuilder();
                            Headers headers = response.headers();
                            for (int i = 0; i < headers.size(); i++) {
                                sb.append(headers.name(i)).append(":").append(headers.value(i)).append("\n");
                            }
                            if (response.isSuccessful()) {
                                mTv.setText(sb.append(body).toString());
                            }
                        }

                        @Override
                        public void onFailure(Request request, IOException e) {

                        }

                        @Override
                        public void after() {

                        }
                    });
                }

            }
        });
    }
}

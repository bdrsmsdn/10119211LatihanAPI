package com.programmer.a10119211latihanapi;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    EditText editText, editText2;
    Button button;
    ImageView imageView;
    TextView desc;
    private Long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        editText = findViewById(R.id.Link);
        editText2 = findViewById(R.id.Link2);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.image);
        desc = findViewById(R.id.desc);

        editText.addTextChangedListener(textWatcher);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
                showw();
                Handler handler = new Handler();
                  handler.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                        beginDownload();
                     }
                  }, 10000);
            }
        });

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String Linkk = editText.getText().toString().trim();
            button.setEnabled(!Linkk.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

        public void showw(){
            imageView.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
        }

        private void beginDownload(){
            String lunk = editText2.getText().toString();
                            File file = new File(getExternalFilesDir(null), "Twitter");

                            DownloadManager.Request request = null;
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                request = new DownloadManager.Request(Uri.parse(lunk))
                                    .setTitle("Twitter Video")
                                    .setDescription("Downloading")
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    .setDestinationUri(Uri.fromFile(file))
                                    .setRequiresCharging(false)
                                    .setAllowedOverMetered(true)
                                    .setAllowedOverRoaming(true);
                            } else {
                                request = new DownloadManager.Request(Uri.parse(lunk))
                                    .setTitle("Twitter Video")
                                    .setDescription("Downloading")
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    .setDestinationUri(Uri.fromFile(file));
                                }

                            DownloadManager downloadManager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                            downloadId = downloadManager.enqueue(request);

                            };

        private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(downloadId == id){
                    Toast.makeText(MainActivity.this, "Download Complete",Toast.LENGTH_SHORT).show();
                }
            }
        };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    public void download()
        {

            final String yuerel = editText.getText().toString();
            String url = "https://api.akuari.my.id/downloader/twitter?link="+yuerel;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //calling API

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        //Finding thumb
                        String thumb = jsonObject.getString("thumb");
                        Picasso.get().load(thumb).into(imageView);

                        //Finding Desc
                        String des = jsonObject.getString("desc");
                        desc.setText(des);

                        //Finding Link to Download
                        String dow = jsonObject.getString("HD");
                        editText2.setText(dow);
                        


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                }

            });

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }

}
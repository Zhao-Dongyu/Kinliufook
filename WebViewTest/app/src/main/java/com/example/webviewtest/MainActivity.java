package com.example.webviewtest;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button_on;
    Button button_off;
    private String sendon_buff=null;
    private String sendoff_buff=null;
    Socket socket = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        //webView.loadUrl("http://192.168.43.58:7654/?action=stream");
        webView.loadUrl("http://192.168.43.58:8080/?action=stream");
        initView();
        Toast.makeText(MainActivity.this,"开始建立socket！", Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    System.out.println("311111111111");
                    socket = new Socket("192.168.43.58" , 7654);
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"建立socket失败！", Toast.LENGTH_LONG).show();
                    if (socket!=null) {
                        System.out.println("22222");
                        Toast.makeText(MainActivity.this,"建立socket成功！", Toast.LENGTH_LONG).show();
                        send();
                        Looper.loop();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"socket失败 socket为空！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                        System.out.println("111111111111");
                    }

                } catch (IOException e) {
                    System.out.println("211111111111");
                    e.printStackTrace();
                }
            }
        }).start();
        send();
    }
    private void send()  {
        button_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socket == null) {
                    Toast.makeText(MainActivity.this, "建立socket失败,请检查连接，开门失败！", Toast.LENGTH_LONG).show();

                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendoff_buff = "1";
                            //向服务器端发送消息
                            System.out.println("-11------");
                            OutputStream outputStream = null;
                            try {
                                outputStream = socket.getOutputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (outputStream != null) {
                                try {
                                    outputStream.write(sendoff_buff.getBytes());
                                    Looper.prepare();
                                    Toast.makeText(MainActivity.this, "开门成功！", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                    System.out.println("发送成功1");
                                    outputStream.flush();
                                } catch (IOException e) {
                                    Looper.prepare();
                                    Toast.makeText(MainActivity.this, "开门失败！", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();
                }
            }
        });
        button_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socket == null) {
                    Toast.makeText(MainActivity.this, "建立socket失败,请检查连接，关门失败！", Toast.LENGTH_LONG).show();

                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendoff_buff = "0";
                            //向服务器端发送消息
                            System.out.println("--------------0----------");
                            OutputStream outputStream = null;
                            try {
                                outputStream = socket.getOutputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (outputStream != null) {
                                try {
                                    outputStream.write(sendoff_buff.getBytes());
                                    System.out.println("发送成功0");
                                    Looper.prepare();
                                    Toast.makeText(MainActivity.this, "关门成功！", Toast.LENGTH_LONG).show();
                                    Looper.loop();

                                    outputStream.flush();
                                } catch (IOException e) {
                                    Looper.prepare();
                                    Toast.makeText(MainActivity.this, "关门失败！", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        });

    }
    private void initView() {
        button_off = (Button) findViewById(R.id.button_off);
        button_on = (Button) findViewById(R.id.button_on);
    }
}

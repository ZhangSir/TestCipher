package com.itzs.testcipher;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText etEncrypt, etDecrypt;
    private Button btnEncrypt, btnDecrypt;
    private TextView tvStatus;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvStatus.setText((String)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initView();
        this.initListener();
    }

    private void initView(){
        etEncrypt = (EditText) findViewById(R.id.et_encrypt);
        etDecrypt = (EditText) findViewById(R.id.et_decrypt);
        btnEncrypt = (Button) findViewById(R.id.btn_encrypt);
        btnDecrypt = (Button) findViewById(R.id.btn_decrypt);
        tvStatus = (TextView) findViewById(R.id.tv_status);
    }

    private void initListener(){

        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSuccess = CustomFileCipherUtil.encrypt(etEncrypt.getText().toString(), new CustomFileCipherUtil.CipherListener() {
                            @Override
                            public void onProgress(long current, long total) {
                                Message msg = uiHandler.obtainMessage();
                                msg.obj = "加密：" + current + "/" + total + "(" + current * 100 / total + "%)";
                                uiHandler.sendMessage(msg);
                            }
                        });
                        Message msg = uiHandler.obtainMessage();
                        if(isSuccess){
                            msg.obj = "加密成功";
                        }else{
                            msg.obj = "加密失败";
                        }
                        uiHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSuccess = CustomFileCipherUtil.decrypt(etDecrypt.getText().toString(), new CustomFileCipherUtil.CipherListener() {
                            @Override
                            public void onProgress(long current, long total) {
                                Message msg = uiHandler.obtainMessage();
                                msg.obj = "解密：" + current + "/" + total + "(" + current * 100 / total + "%)";
                                uiHandler.sendMessage(msg);
                            }
                        });
                        Message msg = uiHandler.obtainMessage();
                        if(isSuccess){
                            msg.obj = "解密成功";
                        }else{
                            msg.obj = "解密失败";
                        }
                        uiHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }
}

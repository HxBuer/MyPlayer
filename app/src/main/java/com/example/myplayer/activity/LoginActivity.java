package com.example.myplayer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myplayer.R;

import org.jetbrains.annotations.NotNull;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/17
 * TODO:登录
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText et_user_name;
    private EditText et_user_pwd;
    private Button bt_login;
    private Button bt_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_user_pwd = findViewById(R.id.et_user_pwd);
        bt_login = findViewById(R.id.bt_login);
        bt_register = findViewById(R.id.bt_register);

        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    /**
     * TODO:登录与注册的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                clickOnLogin();
                break;
            case R.id.bt_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * TODO：1.响应点击事件
     *       2.与建立连接，发送请求
     *       3.接收服务器返回的数据
     *       4.使用Intent更新用户UI
     */
    private void clickOnLogin() {
        final String user_name = et_user_name.getText().toString();
        final String user_pwd = et_user_pwd.getText().toString();
        final ProgressDialog dialog = ProgressDialog.show(this,null,"正在登录···");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(com.example.myplayer.utils.URL.LOGIN_URL);            //设置URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();//打开连接
                    connection.setRequestMethod("POST");                                    //请求方式
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(3000);
                    connection.connect();
                    OutputStream os = connection.getOutputStream();
                    String form = "user_name="+user_name+"&user_pwd="+user_pwd;
                    os.write(form.getBytes(StandardCharsets.UTF_8));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200){                                               //请求成功
                        InputStream is = connection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len=-1;
                        while((len = is.read(buffer)) != -1 ){
                            baos.write(buffer,0,len);
                        }
                        final String result = baos.toString();

                        baos.close();
                        is.close();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("user_data",result);
                                Log.i("login_data:", result);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                    os.close();
                } catch (Exception e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }

            }
        }).start();
        }

}

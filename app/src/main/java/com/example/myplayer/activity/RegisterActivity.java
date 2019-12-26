package com.example.myplayer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myplayer.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/19
 * TODO:注册页面
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private Button bt_back;
    private Button bt_register;
    private EditText et_user_name;
    private EditText et_user_pwd;
    private EditText et_user_pwd_sure;
    private EditText et_user_email;
    private EditText et_user_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_user_pwd = findViewById(R.id.et_user_pwd);
        et_user_pwd_sure = findViewById(R.id.et_user_sure_pwd);
        et_user_email = findViewById(R.id.et_user_email);
        et_user_phone = findViewById(R.id.et_user_phone);
        bt_back = findViewById(R.id.bt_back);
        bt_register = findViewById(R.id.bt_register);


        bt_back.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    /**
     * TODO：编辑框监听
     *
     */
//    TextWatcher userNameWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if(s.length() > 10){
//                et_user_name.setFocusable(true);
//                Toast.makeText(RegisterActivity.this,"超出10位",Toast.LENGTH_SHORT);
//            }
//        }
//    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back:
                this.finish();
                break;
            case R.id.bt_register:
                String userEmail = et_user_email.getText().toString();
                String userName = et_user_name.getText().toString();
                String userPwd = et_user_pwd.getText().toString();
                String userPwdSure = et_user_pwd_sure.getText().toString();
                String userPhone = et_user_phone.getText().toString();
                int position = -1;
                String[] faultString = {"用户名格式错误，应在3-10个字符间", "密码格式错误，应在3-10个字符间","前后密码不相同","Email格式错误","手机号格式错误"};
                if (userName.length() < 3 || userName.equals("   ") || userName.length() > 10) {
                    position = 0;
                }else if (userPwd.length() > 10 || userPwd.length()<3){
                    position = 1;
                }else if (!userPwd.equals(userPwdSure)){
                    position = 2;
                }else if (!checkEmail(userEmail)){
                    position = 3;
                }else if (userPhone.length() != 11){
                    position = 4;
                }

                if(position == -1){
                    registerRequest(userName,userPwd,userEmail,userPhone);
                }else{
                    Toast.makeText(this,faultString[position],Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkEmail(String userEmail) {
        if(userEmail.contains(".")){
            String[] strings = userEmail.split("\\.");
            return strings.length == 2 && (strings[0].contains("@") && strings[1].contains("com"));
        }else return userEmail.equals("");
    }


    private void registerRequest(String user_name ,String user_pwd,String user_email,String user_phone) {

        final String form = "user_name="+user_name+"&user_pwd="+user_pwd+"&user_Email="+user_email+"&user_phone="+user_phone; //请求体
        Log.e("", "userRegisterData="+form);
        final ProgressDialog dialog = ProgressDialog.show(this,null,"正在注册···");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SystemClock.sleep(1500);
                    URL url = new URL(com.example.myplayer.utils.URL.REGISTER_URL);            //设置URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();//打开连接
                    connection.setRequestMethod("POST");                                    //请求方式
                    connection.setConnectTimeout(5000);                                     //最大连接时长
                    connection.setReadTimeout(3000);                                        //最大读数据时长
                    connection.connect();
                    OutputStream os = connection.getOutputStream();
                    os.write(form.getBytes(StandardCharsets.UTF_8));                        //发送请求

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
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Log.e("", "RegisterOK: "+result);
                                RegisterActivity.this.finish();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"注册失败！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    os.close();
                } catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"网络错误！",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }).start();
    }




}

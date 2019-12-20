package com.example.myplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myplayer.R;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/19
 * TODO:
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private Button bt_back;
    private Button bt_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bt_back = findViewById(R.id.bt_back);
        bt_register = findViewById(R.id.bt_register);

        bt_back.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back:
                this.finish();
                break;
            case R.id.bt_register:
                Toast.makeText(this,"注册",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

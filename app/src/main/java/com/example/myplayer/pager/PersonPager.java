package com.example.myplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myplayer.R;
import com.example.myplayer.activity.LoginActivity;
import com.example.myplayer.base.BasePager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/9
 * TODO:个人信息
 */
public class PersonPager extends BasePager implements View.OnClickListener {

    private EditText et_person_email;
    private EditText et_person_phone;
    private TextView tv_person_name;
    private TextView tv_person_status;
    private LinearLayout ll_login_status;

    public PersonPager(Context context){
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.person,null);
        ll_login_status = view.findViewById(R.id.ll_login_status);
        tv_person_name = view.findViewById(R.id.tv_person_name);
        et_person_email = view.findViewById(R.id.et_person_email);
        et_person_phone = view.findViewById(R.id.et_person_phone);
        tv_person_status = view.findViewById(R.id.tv_person_status);
        ll_login_status.setOnClickListener(this);
        return view;
    }

    @Override
    public void initUserData(String json) {
        Log.i("", "initUserData: 更新用户数据···");
        try {
            JSONObject jsonObject = new JSONObject(json);
            String user_name = jsonObject.getString("user_name");
            String user_email = jsonObject.getString("user_Email");
            String user_phone = jsonObject.getString("user_phone");
            tv_person_name.setText(user_name);
            tv_person_status.setText("已登录");
            et_person_email.setText(user_email);
            et_person_phone.setText(user_phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_login_status:
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
        }
    }


}

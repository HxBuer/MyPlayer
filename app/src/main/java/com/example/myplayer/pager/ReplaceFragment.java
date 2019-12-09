package com.example.myplayer.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myplayer.base.BasePager;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/9
 * TODO:
 */
public class ReplaceFragment extends Fragment {

    private BasePager basePager;

    public ReplaceFragment(BasePager basePager){
        this.basePager = basePager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return basePager.rootView;
    }
}

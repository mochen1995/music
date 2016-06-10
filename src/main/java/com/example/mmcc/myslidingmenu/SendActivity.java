package com.example.mmcc.myslidingmenu;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import Utils.ToolbarUtil;

/**
 * Created by mmcc on 2016/1/24.
 */
public class SendActivity extends BaseActivity {
    private ToolbarUtil toolbarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(this).inflate(R.layout.top_tabs,null);
        toolbarUtil = getToolbarUtil();
        toolbarUtil.setDisplayShowHomeEnabled(true).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setTitle(null).setSubTitle(null);
       toolbarUtil.setCustomView(view);
    }

    @Override
    int getLayoutId() {
        return R.layout.second_activity;
    }
}

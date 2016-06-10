package Utils;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mmcc.myslidingmenu.R;

/**
 * Created by mmcc on 2016/1/25.
 */
public class ToolbarUtil {
    private Toolbar toolbar;
    private AppCompatActivity activity;
    private ActionBar actionBar;
    private RelativeLayout relativeLayout;
    public ToolbarUtil(Toolbar toolbar,AppCompatActivity activity){
        this.toolbar=toolbar;
        this.activity=activity;
        this.activity.setSupportActionBar(this.toolbar);
        this.actionBar=this.activity.getSupportActionBar();
        relativeLayout= (RelativeLayout) toolbar.findViewById(R.id.id_toolbar_relative);

    }
    public ToolbarUtil setTitle(String title){
        actionBar.setTitle(title);
        return this;
    }
    public ToolbarUtil setSubTitle(String title){
        actionBar.setSubtitle(title);
        return this;
    }
    //设置导航栏图标
    public ToolbarUtil setIcon(int id){
        actionBar.setIcon(id);
        return this;
    }
    public ToolbarUtil setBackgroundColor(int color){
        toolbar.setBackgroundColor(color);
        return this;
    }
    public ToolbarUtil setDisplayShowHomeEnabled(boolean displayShowHomeEnabled){
        actionBar.setDisplayHomeAsUpEnabled(displayShowHomeEnabled);
        return this;
    }
    public Toolbar getToolbar(){
        return toolbar;
    }
    //设置左上角返回按钮的图标
    public ToolbarUtil setNavigationIcon(int resId){
        toolbar.setNavigationIcon(resId);
        return this;
    }
    public ToolbarUtil setNavigationOnClickListener(View.OnClickListener listener){
        toolbar.setNavigationOnClickListener(listener);
        return this;
    }
    public ToolbarUtil setCustomView(View view){
        relativeLayout.removeAllViews();
        relativeLayout.addView(view);
        return this;
    }
}

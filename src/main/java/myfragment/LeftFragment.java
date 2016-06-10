package myfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mmcc.myslidingmenu.R;

/**
 * Created by mmcc on 2016/1/23.
 */
public class LeftFragment extends BasedFragment{
    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.left_fragment,container,false);
        return mView;
    }
}

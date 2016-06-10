package myfragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.mmcc.myslidingmenu.R;

/**
 * Created by mmcc on 2016/1/28.
 * mmcc
 */
public class BasedFragment extends Fragment{

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_in_right_left,R.anim.activity_out_right_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().startActivityForResult(intent,requestCode);
        getActivity().overridePendingTransition(R.anim.activity_in_right_left,R.anim.activity_out_right_left);
    }
}

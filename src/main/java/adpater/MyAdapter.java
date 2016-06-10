package adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mmcc.myslidingmenu.R;

import java.util.List;

import Bean.MusicBean;
import Utils.Utils;

/**
 * Created by mmcc on 2016/1/23.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHoder> {

    private List<MusicBean> mDatas;
    private Context mContext;
    private Utils StringUtils;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public interface OnRecyclerItemClickListener{
        void OnItemClickListener(int position);
    }

    public MyAdapter(Context context, List<MusicBean> datas) {
        mDatas = datas;
        mContext = context;
        StringUtils=new Utils();
    }

    @Override
    public myViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false);
        myViewHoder hoder = new myViewHoder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(myViewHoder holder, final int position) {
        holder.item_name.setText(mDatas.get(position).getName());
        holder.item_artist.setText(mDatas.get(position).getArtist());
        holder.item_duration.setText(StringUtils.stringForTime((int) mDatas.get(position).getDuration()));
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRecyclerItemClickListener!=null)
                {
                    onRecyclerItemClickListener.OnItemClickListener(position);
                }
            }
        });
    }
    //添加歌曲
    public void addData(MusicBean bean){
        mDatas.add(bean);
    }
    //清空列表
    public void clearData(){
        mDatas.clear();
    }
    //删除列表中的
    public void removeData(int position){
        if(mDatas.size()>0)
        mDatas.remove(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class myViewHoder extends RecyclerView.ViewHolder {
        private TextView item_name;
        private TextView item_artist;
        private TextView item_duration;
        private RelativeLayout item_layout;

        public myViewHoder(View itemView) {
            super(itemView);
            item_layout= (RelativeLayout) itemView.findViewById(R.id.item_layout);
            item_name= (TextView) itemView.findViewById(R.id.item__music_name);
            item_artist= (TextView) itemView.findViewById(R.id.item_music_author);
            item_duration= (TextView) itemView.findViewById(R.id.item_time);
        }
    }
}

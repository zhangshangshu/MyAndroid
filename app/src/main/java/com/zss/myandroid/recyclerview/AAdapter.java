package com.zss.myandroid.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zss.myandroid.R;

import java.util.List;

/**
 * 最基本的使用
 */
public class AAdapter extends RecyclerView.Adapter<AAdapter.MyViewHolder> {

    private List<String> mDatas;
    private Context mContext;

    public AAdapter(List<String> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_a,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = holder.getLayoutPosition();
                mOnItemClickLitener.onItemClick(holder.itemView, pos);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                int pos = holder.getLayoutPosition();
                mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                //拦截事件。如果返回false，事件会继续传递给OnClick，如松开手后会触发OnClick事件。
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.id_num);
        }
    }

    /**
     * 这里更新数据集不是用adapter.notifyDataSetChanged()而是
     * notifyItemInserted(position)与notifyItemRemoved(position)
     * 否则没有动画效果。
     * @param position
     */
    public void addData(int position) {
        mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 点击事件
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}

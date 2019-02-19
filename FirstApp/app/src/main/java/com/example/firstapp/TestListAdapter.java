package com.example.firstapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * @Package: com.example.firstapp
 * @ClassName: TestListAdapter
 * @Description: java类作用描述
 * @Author: Nowy
 * @CreateDate: 2019/2/15 12:46
 * @UpdateUser: Nowy
 * @UpdateDate: 2019/2/15 12:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> mData ;




    public TestListAdapter(List<String> data){
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(ItemViewHolder.res_layout,viewGroup,false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ItemViewHolder)viewHolder).bingData(i);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        static final int res_layout = R.layout.item_test;
        private final ImageView imageView;
        int[] colorArr = new int[]{
                0xFFFFF68F,0xFFFF7F24,0xFFFF0000,
                0xFFFFBBFF,0xFFEE00EE,0xFFB22222

        };
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_test_IvItem);
        }

        private void bingData(int position){
            imageView.setBackgroundColor(colorArr[position%colorArr.length]);
        }
    }
}

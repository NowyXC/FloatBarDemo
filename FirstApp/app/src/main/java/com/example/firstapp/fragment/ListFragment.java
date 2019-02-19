package com.example.firstapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firstapp.R;
import com.example.firstapp.TestListAdapter;
import com.example.firstapp.widget.detailView.fragment.BaseDetailFrag;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.example.firstapp.fragment
 * @ClassName: ListFragment
 * @Description: java类作用描述
 * @Author: Nowy
 * @CreateDate: 2019/2/15 12:02
 * @UpdateUser: Nowy
 * @UpdateDate: 2019/2/15 12:02
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ListFragment extends BaseDetailFrag {

    private RecyclerView recyclerView;


    public static ListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_recycler,container,false);
        initView(rootView);
        initRecyclerView();
        return rootView;
    }

    private void initView(View rootView){
        recyclerView = rootView.findViewById(R.id.list_RecyclerView);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        List<String> data = new ArrayList<>();
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        recyclerView.setAdapter(new TestListAdapter(data));
        recyclerView.addOnScrollListener(getOnScrollListener());
    }
}

package com.example.firstapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firstapp.R;
import com.example.firstapp.widget.detailView.fragment.BaseDetailFrag;

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
public class TextViewFragment extends BaseDetailFrag {



    public static TextViewFragment newInstance() {

        Bundle args = new Bundle();

        TextViewFragment fragment = new TextViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_text,container,false);
        return rootView;
    }


}

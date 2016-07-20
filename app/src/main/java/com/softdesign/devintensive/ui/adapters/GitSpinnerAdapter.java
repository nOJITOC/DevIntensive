package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;

import com.softdesign.devintensive.R;

import java.util.List;

/**
 * Created by Иван on 18.07.2016.
 */
public class GitSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    List<String> mRepositories;
    Context mContext;
    private LayoutInflater mInflater;
    public GitSpinnerAdapter(Context context,List<String> gitET) {
        mRepositories = gitET;
        mContext = context;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRepositories.size();
    }

    @Override
    public String getItem(int i) {
        return mRepositories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView=mInflater.inflate(R.layout.git_spinner_item,parent,false);

        }
        EditText repoName = (EditText) itemView.findViewById(R.id.github_et);
        repoName.setText(mRepositories.get(i));
        return itemView;
    }
}

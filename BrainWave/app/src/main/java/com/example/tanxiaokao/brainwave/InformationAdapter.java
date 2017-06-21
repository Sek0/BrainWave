package com.example.tanxiaokao.brainwave;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tanxiaokao on 2017/5/12.
 */

public class InformationAdapter extends ArrayAdapter<InformationListItem>{
    private int resourceId;
    public InformationAdapter(Context context, int textViewResourceId, List<InformationListItem> object) {
        super(context, textViewResourceId,object);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        InformationListItem informationListItem = getItem(position);//获取当前项的InformationListItem实例
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.informationImage = (ImageView) view.findViewById(R.id.information_image);
            viewHolder.informationTitle = (TextView) view.findViewById(R.id.information_title);
            viewHolder.informationDescribe = (TextView) view.findViewById(R.id.information_describe);
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.informationImage.setImageResource(informationListItem.getImageId());
        viewHolder.informationTitle.setText(informationListItem.getTitle());
        viewHolder.informationDescribe.setText(informationListItem.getDescribe());
        viewHolder.image.setImageResource(informationListItem.getImage());
        return view;
    }
    class ViewHolder{
        ImageView informationImage;
        TextView informationTitle;
        TextView informationDescribe;
        ImageView image;

    }
}

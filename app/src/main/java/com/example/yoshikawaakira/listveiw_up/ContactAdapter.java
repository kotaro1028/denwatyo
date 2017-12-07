package com.example.yoshikawaakira.listveiw_up;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yoshikawaakira on 2017/11/30.
 */

public class ContactAdapter extends ArrayAdapter<ContactDetail> {
    private Activity activity;
    private int row;
    private List<ContactDetail> items;
    private ContactDetail contactDetail;

    public ContactAdapter(Activity activity, int row, List<ContactDetail> items){
        super(activity,row,items);
        this.activity = activity;
        this.row = row;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null){

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// runtimeに毎回携帯の中に入ってるデータにアクセスしてからlayoutを決めるのでinflaterを使う。
            view = inflater.inflate(row,null);//ここでのrowはsingleuser.xmlのことである。
            holder = new ViewHolder();
            view.setTag(holder);//  データをここで入れる。

        }else
        {
            holder = (ViewHolder) view.getTag();//データをここからとる。
        }


        if ((items == null) || ((position+1) > items.size())){//もし携帯の中に誰も登録してない時のエラーをはじく。
            return view;
        }

        contactDetail = items.get(position);
        holder.tvname = view.findViewById(R.id.name);
        holder.tvphone = view.findViewById(R.id.phone);

        if (holder.tvname != null && contactDetail.getName() != null && contactDetail.getName().trim().length()>0){
            holder.tvname.setText(contactDetail.getName());
        }

        if (holder.tvphone != null && contactDetail.getPhone_number() != null && contactDetail.getPhone_number().trim().length()>0){
            holder.tvphone.setText(contactDetail.getPhone_number());
        }


        return view;

    }

    class ViewHolder{
        public TextView tvname, tvphone;
    }
}

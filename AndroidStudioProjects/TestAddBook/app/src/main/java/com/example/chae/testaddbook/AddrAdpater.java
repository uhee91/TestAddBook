package com.example.chae.testaddbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by muhan_chae on 2017-06-07.
 */

public class AddrAdpater extends ArrayAdapter<SetAddrData> {
    private Context context;
    private int layoutId;
    private ArrayList<SetAddrData> data;

   public AddrAdpater(Context context, int layoutId, ArrayList<SetAddrData> data){
        super(context,layoutId, data);

       this.context = context;
       this.layoutId = layoutId;
       this.data = data;
   }
   @NonNull
   @Override
   public View getView(int position , @Nullable View convertView, @NonNull ViewGroup parent){
       ViewHolder holder;
       if(convertView == null){
           convertView = LayoutInflater.from(context).inflate(layoutId,null);

           holder = new ViewHolder();
           holder.addrName = (TextView) convertView.findViewById(R.id.addrName);
           holder.addrPnumber = (TextView) convertView.findViewById(R.id.addrPnumber);
           holder.conText = (TextView) convertView.findViewById(R.id.conText);
           holder.conLayout = (LinearLayout) convertView.findViewById(R.id.conLayout); //**수정부분

           convertView.setTag(holder);
       }else{
           holder = (ViewHolder)convertView.getTag();
       }

       //처리된 데이터 값을 position으로 가져온다.
       SetAddrData item= data.get(position);
       //할당 받으면 동작 부분을 넣는다.
       holder.addrName.setText(item.getName());
       holder.addrPnumber.setText(item.getpNumber());
       holder.conText.setText(String.valueOf(item.getInital()));
       if(item.getStatus() == 0) { //수정된 부분
           holder.conLayout.setVisibility(View.VISIBLE); // **수정부분
       } else if (item.getStatus() ==1 ){
           holder.conLayout.setVisibility(View.GONE); //** 수정부분
       } //수정된 부분

       return convertView;
   }

   class ViewHolder{
       LinearLayout conLayout;
       TextView addrName;
       TextView addrPnumber;
       TextView conText;
   }
}

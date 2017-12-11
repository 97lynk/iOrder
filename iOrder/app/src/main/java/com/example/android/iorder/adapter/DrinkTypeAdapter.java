package com.example.android.iorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.iorder.R;
import com.example.android.iorder.model.DrinkType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class DrinkTypeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DrinkType> objects;

    public DrinkTypeAdapter(Context context, ArrayList<DrinkType> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // TODO custom cho drinktype

        // holder để giữ các controls trên dòng view đó
        DrinkTypeHolder holder = null;

        if (view == null) { // lần đầu view được tạo
            // lấy drinktype_item đã tạo
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drinktype_item, null);

            // tạo holder
            holder = new DrinkTypeHolder();

            // cất các controls vào holder
            holder.imgDrinkType = (ImageView) view.findViewById(R.id.imgDrinkType);
            holder.txtDrinkTypeName = (TextView) view.findViewById(R.id.txtDrinkTypeName);

            // cất holder vào tag
            view.setTag(holder);
        } else {
            // lấy holder ra nếu là lần sau
            holder = (DrinkTypeHolder) view.getTag();
        }
        // lấy data
        DrinkType drinkType = (DrinkType) this.getItem(i);

        // show lên các controls
        holder.txtDrinkTypeName.setText(drinkType.getDrinkTypeName());
        holder.drinkType = drinkType;
        Picasso.with(context)
                .load(drinkType.getIcon())
                .placeholder(R.drawable.loading)
                .error(R.drawable.milktea_icon)
                .into(holder.imgDrinkType);
        return view;
    }

    public class DrinkTypeHolder {
        // TODO Holder Drinktyp
        public DrinkType drinkType;
        public ImageView imgDrinkType;
        public TextView txtDrinkTypeName;
    }
}

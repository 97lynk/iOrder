package com.example.android.iorder.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.iorder.R;
import com.example.android.iorder.model.Drink;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class DrinkAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Drink> objects;

    public DrinkAdapter(Context context, ArrayList<Drink> objects) {
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
        // TODO custom view cho Drink

        // holder để giữ các controls trên dòng view đó
        DrinkHolder holder = null;

        if (view == null) {// lần đầu view được tạo
            // lấy drink_item đã tạo
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drink_item, null);

            // tạo mới holder
            holder = new DrinkHolder();

            // cất các controls vào holder
            holder.imgDrink = (ImageView) view.findViewById(R.id.imgDrink);
            holder.txtDrinkName = (TextView) view.findViewById(R.id.txtDrinkName);
            holder.txtDrinkPrice = (TextView) view.findViewById(R.id.txtDrinkPrice);

            // bỏ holder vào tag của view
            view.setTag(holder);
        } else {
            // lấy holder ra nếu đây không phải lần đầy tạo view
            holder = (DrinkHolder) view.getTag();
        }
        // lấy dữ liệu drink
        Drink drink = (Drink) this.getItem(i);
        // format cho kiểu số chấm động
        DecimalFormat format = new DecimalFormat("###,###,###");
        // show lên các controls
        holder.txtDrinkName.setText(drink.getDrinkName());
        holder.txtDrinkPrice.setText(format.format(drink.getUnitPrice()) + " đ");
        holder.drink = drink;
        // load ảnh bằng picasso
        Picasso.with(context)
                .load(drink.getIcon()) // load từ url
                .placeholder(R.drawable.milktea_icon)   // icon khi đang load
                .error(R.drawable.milktea_icon) // icon lúc bị lỗi
                .into(holder.imgDrink); // load vào imageview
        return view;
    }

    public class DrinkHolder {
        // TODO Holder Drink
        public Drink drink;
        public ImageView imgDrink;
        public TextView txtDrinkName;
        public TextView txtDrinkPrice;

    }
}

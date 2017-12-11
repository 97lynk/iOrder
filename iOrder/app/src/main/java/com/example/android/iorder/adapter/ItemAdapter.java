package com.example.android.iorder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.iorder.R;
import com.example.android.iorder.model.Item;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> objects;

    public ItemAdapter(Context context, ArrayList<Item> objects) {
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
        // TODO custom item

        // holder giữa các controls
        ItemHolder holder = null;

        if (view == null) { // lần đầu tạo view
            // load item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, null);

            // tạo mới holder
            holder = new ItemHolder();

            // cất hết vào itemholder
            holder.imgItem = (ImageView) view.findViewById(R.id.imgItem);
            holder.txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
            holder.txtItemAmount = (TextView) view.findViewById(R.id.txtItemAmount);
            holder.txtItemTotal = (TextView) view.findViewById(R.id.txtItemTotal);
            holder.txtItemName = (TextView) view.findViewById(R.id.txtItemName);
            holder.txtItemType = (TextView) view.findViewById(R.id.txtItemType);

            // cất itemholder vào tag
            view.setTag(holder);
        } else {
            // lấy lại holder
            holder = (ItemHolder) view.getTag();
        }
        // lấy data
        Item item = (Item) this.getItem(i);
        // định dạng số dấu chấm động
        DecimalFormat format = new DecimalFormat("###,###,###");
        // show lên các controls
        holder.txtItemName.setText(item.getDrink().getDrinkName());
        holder.txtItemType.setText(item.getDrinkTypeName());
        holder.txtItemPrice.setText(format.format(item.getDrink().getUnitPrice()) + " đ");
        holder.txtItemAmount.setText(item.getAmount() + "ly");
        holder.txtItemTotal.setText(format.format(item.getDrink().getUnitPrice() * item.getAmount()) + " đ");
        Picasso.with(context)
                .load(item.getDrink().getIcon())
                .placeholder(R.drawable.milktea_icon)
                .error(R.drawable.milktea_icon)
                .into(holder.imgItem);

        return view;
    }

    public class ItemHolder {
        // TODO Holder Item
        public ImageView imgItem;
        public TextView txtItemPrice;
        public TextView txtItemAmount;
        public TextView txtItemTotal;
        public TextView txtItemName;
        public TextView txtItemType;
    }
}

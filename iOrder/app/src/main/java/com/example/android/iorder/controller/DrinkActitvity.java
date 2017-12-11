package com.example.android.iorder.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.iorder.R;
import com.example.android.iorder.adapter.DrinkAdapter;
import com.example.android.iorder.model.Drink;
import com.example.android.iorder.model.DrinkType;
import com.example.android.iorder.model.Item;
import com.example.android.iorder.util.MyContext;
import com.example.android.iorder.util.MyToast;

import java.util.ArrayList;


public class DrinkActitvity extends AppCompatActivity {

    ArrayList<Drink> drinks;
    DrinkAdapter drinkAdapter;
    ListView lvDrink;
    DrinkType drinkType;
    Toolbar toolBarDrink;

    EditText txtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_drink);

        initializeControls();
        addEvents();
    }


    private void initializeControls() {
        Log.i("l", "initializeControls: he");
        lvDrink = (ListView) findViewById(R.id.lvDrink);
        toolBarDrink = (Toolbar) findViewById(R.id.toolBarDrink);
        drinkType = (DrinkType) getIntent().getSerializableExtra("DRINKTYPE");
        toolBarDrink.setTitle(drinkType.getDrinkTypeName());
        drinks = new ArrayList<>();
        for (Drink d : MyContext.drinks) {
            if (d.getDrinkTypeID() == drinkType.getDrinkTypeID())
                drinks.add(d);
        }
        drinkAdapter = new DrinkAdapter(getApplicationContext(), drinks);
        lvDrink.setAdapter(drinkAdapter);


    }

    private AlertDialog.Builder createDialog(final Drink drink, String defaultText) {
        // tạo prompt_dialog.xml
        LayoutInflater layoutInflater = LayoutInflater.from(DrinkActitvity.this);
        View promptView = layoutInflater.inflate(R.layout.prompt_dialog, null);
        txtInput = promptView.findViewById(R.id.txtInput);
        txtInput.setText(defaultText);
        txtInput.setSelection(1);
        txtInput.requestFocus();
        final AlertDialog.Builder inputDialog = new AlertDialog.Builder(DrinkActitvity.this);
        inputDialog.setView(promptView);
        inputDialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                //TODO gửi items về server
                Item item = MyContext.findItemByID(drink.getDrinkID());
                int amount = Integer.parseInt(txtInput.getText().toString());
                if (item == null) {
                    MyContext.items.add(new Item(drink, toolBarDrink.getTitle().toString(), amount, 0.0));
                } else {
                    item.setAmount(amount);
                }
                //MyToast.fastShow(getApplicationContext() , amount  + " ly");
                //drinkAdapter.notifyDataSetChanged();

            }
        });
        inputDialog.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO nhấn cancel trên dialog xác nhận
                dialog.dismiss();
            }
        });
        return inputDialog;
    }

    private void addEvents() {
        actionBar();

        lvDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrinkAdapter.DrinkHolder drinkHolder = (DrinkAdapter.DrinkHolder) view.getTag();
                Drink drink = drinkHolder.drink;
                Item item = MyContext.findItemByID(drink.getDrinkID());
                if (item == null) {
                    item = new Item(drink, toolBarDrink.getTitle().toString(), 1, 0.0);
                    MyContext.items.add(item);
                } else {
                    item.setAmount(item.getAmount() + 1);
                }
                MyToast.fastShow(DrinkActitvity.this, item.getAmount() + " ly " + drink.getDrinkName());

            }
        });
        lvDrink.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DrinkAdapter.DrinkHolder drinkHolder = (DrinkAdapter.DrinkHolder) view.getTag();
                Drink drink = drinkHolder.drink;

                Item item = MyContext.findItemByID(drink.getDrinkID());
                String defaultText = ((item != null) ? item.getAmount() : 1) + "";

                AlertDialog alertDialog = createDialog(drink, defaultText).create();
                alertDialog.setTitle("Nhập số lượng " + drink.getDrinkName());
                alertDialog.show();

                return true;
            }
        });
    }


    private void actionBar() {
        setSupportActionBar(toolBarDrink);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //       toolBarDrink.setNavigationIcon(android.R.drawable.);
        toolBarDrink.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

package com.example.android.iorder.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.iorder.R;
import com.example.android.iorder.model.*;
import com.example.android.iorder.adapter.*;
import com.example.android.iorder.util.*;

import static com.example.android.iorder.util.MyDomain.*;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // layout gốc
    CoordinatorLayout root;

    //
    Toolbar toolBarHome;
    NavigationView navHome;
    DrawerLayout drawerDrinkType;

    //
    DrinkTypeAdapter drinkTypeAdapter;
    ListView lvDrinktype;

    //
    ItemAdapter itemAdapter;
    ListView lvItems;

    //
    ArrayAdapter<Table> tableAdapter;
    Spinner spTable;

    //
    FloatingActionButton btnViewBill;
    FloatingActionButton btnSubmit;
    FloatingActionButton btnClearBill;

    //
    double grandTotal = 0.0;
    TextView txtGrandTotal;
    TextView txtStatus;
    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initializeControls();
            addEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // tạo button refresh góc phải
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // load loại data khi nhấn refresh
        loadData();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshGrandtotal();
    }

    private void initializeControls() {

        root = (CoordinatorLayout) findViewById(R.id.root);
        //
        toolBarHome = (Toolbar) findViewById(R.id.toolBarHome);
        navHome = (NavigationView) findViewById(R.id.navHome);
        drawerDrinkType = (DrawerLayout) findViewById(R.id.drawerDrinkType);
        //
        lvDrinktype = (ListView) findViewById(R.id.lvDrinkType);
        drinkTypeAdapter = new DrinkTypeAdapter(getApplicationContext(), MyContext.drinkTypes);
        lvDrinktype.setAdapter(drinkTypeAdapter);
        //
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemAdapter = new ItemAdapter(getApplicationContext(), MyContext.items);
        lvItems.setAdapter(itemAdapter);
        //
        Picasso.with(getApplicationContext())
                .load("http://store.bobapop.com.vn/resource/themes/bobapop/images/logo2.png")
                .placeholder(R.drawable.milktea_icon)
                .error(R.drawable.milktea_icon)
                .into((ImageView) findViewById(R.id.imgLogo));
        //
        btnViewBill = (FloatingActionButton) findViewById(R.id.btnViewBill);
        btnClearBill = (FloatingActionButton) findViewById(R.id.btnClearBill);
        btnSubmit = (FloatingActionButton) findViewById(R.id.btnSubmit);
        //
        spTable = (Spinner) findViewById(R.id.spTable);
        tableAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                MyContext.tables
        );
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTable.setAdapter(tableAdapter);
        //
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtGrandTotal = (TextView) findViewById(R.id.txtGrandTotal);
        txtGrandTotal.setText(grandTotal + " d");
        //
        loadData();
    }


    private void addEvents() {
        actionBar();
        lvItemsEvents();

        btnViewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // mở webview
                    Intent intent = new Intent(MainActivity.this, BillActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show lên dialog xác nhận
                AlertDialog alertDialog = createSubmitDialog().create();
                alertDialog.setTitle("Xác nhận gửi hóa đơn?");
                alertDialog.show();
            }
        });

        btnClearBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // đem các hết item cần xóa copy qua deletedItems
                final ArrayList<Item> deletedItems = (ArrayList<Item>) MyContext.items.clone();
                // xóa hết item trên bill
                MyContext.items.clear();
                // refresh lại grandtotal
                refreshGrandtotal();
                // tạo snackbar yêu cầu hoàn tác lại
                Snackbar snackbar = Snackbar
                        .make(root, "Đã xóa hết ", Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // restore lại
                                if (deletedItems != null && deletedItems.size() > 0) {
                                    MyContext.items.addAll(deletedItems);
                                    refreshGrandtotal();
                                    MyToast.show(MainActivity.this, "Đã hoàn lại");
                                }
                            }
                        });
                // show snackbar
                snackbar.show();
            }
        });
    }

    private void lvItemsEvents() {
        // nhấn giữ trên item - xóa item
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // xóa nó và bỏ vào deletedItem
                final Item deletedItem = MyContext.items.remove(position);
                // load lại tổng tiền
                refreshGrandtotal();
                // tạo snackbar hoàn toác
                Snackbar snackbar = Snackbar
                        .make(root, "Đã xóa thức uống", Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (deletedItem != null) {
                                    // thêm lại vào
                                    MyContext.items.add(deletedItem);
                                    refreshGrandtotal();
                                    MyToast.show(MainActivity.this, "Đã hoàn lại");
                                }
                            }
                        });
                // show nackbar
                snackbar.show();
                return true;
            }
        });

        // nhấn vào item show prompt dialog nhập số lượng mới
        lvItems.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = MyContext.items.get(position);
                AlertDialog promptDialog = createPromptDialog(item).create();
                promptDialog.setTitle("Nhập số lượng " + item.getDrink().getDrinkName());
                promptDialog.show();
            }
        });
    }

    private AlertDialog.Builder createPromptDialog(final Item item) {
        // tạo prompt_dialog.xml
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.prompt_dialog, null);
        // edittext để nhập số lượng mới
        final EditText txtInput = promptView.findViewById(R.id.txtInput);
        txtInput.setText(item.getAmount() + "");
        txtInput.setSelection(txtInput.getText().toString().length());

        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setView(promptView);
        inputDialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                //TODO đồng ý số lượng thức uống mới
                if (txtInput.getText().toString() != null
                        || txtInput.getText().toString().trim().length() > 0)
                    item.setAmount(Integer.parseInt(txtInput.getText().toString()));
                refreshGrandtotal();
            }
        });
        inputDialog.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO nhấn cancel bỏ sửa số lượng
                dialog.dismiss();
            }
        });
        return inputDialog;
    }

    private AlertDialog.Builder createSubmitDialog() {
        // tạo dialog thông báo thành công
        final AlertDialog.Builder infoDialog = new AlertDialog.Builder(MainActivity.this);
        infoDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // tạo submit dialog

        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                //TODO gửi items về server
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        MyDomain.URL_SUBMIT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // gửi mà ko có lỗi
                                if (response != null) {
                                    response = response.trim();
                                    AlertDialog myDialog = infoDialog.create();

                                    // gửi thành công
                                    if (response.equals("1")) {
                                        myDialog.setTitle("Gửi hóa đơn thành công");
                                        MyContext.items.clear();
                                        refreshGrandtotal();

                                        txtStatus.setVisibility(View.VISIBLE);
                                        lvItems.setVisibility(View.INVISIBLE);
                                    }
                                    // gửi lỗi ở phía server
                                    if (response.equals("0"))
                                        myDialog.setTitle("Gửi hóa trống");
                                    if (response.equals("-1"))
                                        myDialog.setTitle("Gửi hóa đơn thất bại");
                                    myDialog.show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // gửi bị lỗi
                                MyToast.show(MainActivity.this, error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //TODO tạo query chứa items và tableid gửi về server

                        // JSON array chứa các Items
                        JSONArray itemsArray = new JSONArray();
                        try {
                            for (Item i : MyContext.items) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("DrinkID", i.getDrink().getDrinkID());
                                jsonObject.put("Quantity", i.getAmount());
                                jsonObject.put("Total", i.getDrink().getUnitPrice() * i.getAmount());
                                itemsArray.put(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // table cần gửi về (chỉ gửi id)
                        Table table = (Table) spTable.getSelectedItem();

                        // map chứa query string
                        HashMap<String, String> params = new HashMap<>();
                        params.put("TableID", table.getTableID() + "");
                        params.put("Items", itemsArray.toString());
                        return params;
                    }
                };
                queue.add(stringRequest);
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

    private void refreshGrandtotal() {
        // update lítview
        itemAdapter.notifyDataSetChanged();

        // ẩn hiện txtStatus nếu không có item nào
        txtStatus.setVisibility(View.VISIBLE);
        lvItems.setVisibility(View.INVISIBLE);
        if (MyContext.items.size() > 0) {
            txtStatus.setVisibility(View.INVISIBLE);
            lvItems.setVisibility(View.VISIBLE);
        }

        // tính lại tổng cộng
        grandTotal = 0.0;
        for (Item i : MyContext.items) {
            grandTotal += i.getAmount() * i.getDrink().getUnitPrice();
        }
        // show lên
        txtGrandTotal.setText(grandTotal + "đ");
    }

    private void actionBar() {
        setSupportActionBar(toolBarHome);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerDrinkType,
                toolBarHome, R.string.app_name, R.string.cancel);
        drawerDrinkType.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
        toolBarHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerDrinkType.openDrawer(GravityCompat.START);
            }
        });

    }

    private void loadData() {
        if (TestConnection.haveNetWorkConnection(getApplicationContext())) {
            MyContext.drinkTypes.clear();
            MyContext.drinks.clear();

            MyToast.fastShow(MainActivity.this, "Đang tải dữ liệu...");
            lvDrinktype.setOnItemClickListener(listener);
            MyJSON.get(getApplicationContext(), URL_DRINK_TYPES,
                    drinkTypeResponseListener, null);

            MyJSON.get(getApplicationContext(), URL_DRINKS,
                    drinkResponseListener, null);

            MyJSON.get(getApplicationContext(), URL_TABLES,
                    tableResponseListener, null);
        } else {
            MyToast.show(getApplicationContext(), "Không có kết nối internet");
        }
    }


    OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (TestConnection.haveNetWorkConnection(getApplicationContext())) {
                DrinkTypeAdapter.DrinkTypeHolder holder = (DrinkTypeAdapter.DrinkTypeHolder) view.getTag();
                DrinkType drinkType = holder.drinkType;
                Intent intent = new Intent(MainActivity.this, DrinkActitvity.class);
                intent.putExtra("DRINKTYPE", drinkType);
                startActivity(intent);
            } else {
                MyToast.fastShow(getApplicationContext(), "Không có kết nối Internet");
            }
            drawerDrinkType.closeDrawer(GravityCompat.START);
        }
    };


    private Response.Listener<String> drinkTypeResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (response != null && response.length() > 2) {
                int id = 0;
                String name = "";
                String icon = "";

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id = object.getInt("DrinkTypeID");
                        name = object.getString("DrinkTypeName");
                        icon = object.getString("Icon");
                        MyContext.drinkTypes.add(new DrinkType(id, name, icon));
                        drinkTypeAdapter.notifyDataSetChanged();
                    }
                    MyToast.fastShow(MainActivity.this, "Dữ liệu loại thức uống đã tải xong...");
                    Log.i("RESPONSE", "drink type complete");
                } catch (JSONException e) {
                    MyToast.show(getApplicationContext(), e.getMessage());
                    Log.i("ERR", "onResponse: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    };

    private Response.Listener<String> tableResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (response != null && response.length() > 2) {
                int id = 0;
                String name = "";
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id = object.getInt("TableID");
                        name = object.getString("TableName");
                        MyContext.tables.add(new Table(id, name));
                        tableAdapter.notifyDataSetChanged();
                    }
                    MyToast.fastShow(MainActivity.this, "Dữ liệu bàn đã tải xong...");
                    Log.i("RESPONSE", "table complete");
                } catch (JSONException e) {
                    MyToast.show(getApplicationContext(), e.getMessage());
                    Log.i("ERR", "onResponse: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    };
    private Response.Listener<String> drinkResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (response != null && response.length() > 2) {
                int id = 0;
                int typeid = 0;
                String name = "";
                double price = 0.0;
                String icon = "";
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id = object.getInt("DrinkID");
                        typeid = object.getInt("DrinkTypeID");
                        name = object.getString("DrinkName");
                        price = object.getDouble("UnitPrice");
                        icon = object.getString("Icon");

                        MyContext.drinks.add(new Drink(typeid, id, name, icon, price));

                    }
                    MyToast.show(MainActivity.this, "Dữ liệu thức uống đã tải xong...");
                    Log.i("RESPONSE", " drink complete ");
                } catch (JSONException e) {
                    MyToast.show(getApplicationContext(), e.getMessage());
                }
            }
        }
    };

}

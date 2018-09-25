package com.example.gian.gapakelama.Navigations;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gian.gapakelama.Database.Model.ModelDB.Cart;
import com.example.gian.gapakelama.Helper.BottomNavigationViewHelper;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.Orders.OrdersAdapter;
import com.example.gian.gapakelama.R;
import com.example.gian.gapakelama.Sign.SigninActivity;
import com.example.gian.gapakelama.Util.RecyclerItemTouchHelper;
import com.example.gian.gapakelama.Util.RecyclerItemTouchHelperListener;
import com.example.gian.gapakelama.Util.Server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class ChartActivity extends Activity implements RecyclerItemTouchHelperListener{

    private RecyclerView recyclerView;

    CompositeDisposable compositeDisposable;

    @BindView(R.id.title_struck)
    TextView nostruck;

    @BindView(R.id.date_trans)
    TextView date_trans;

    @BindView(R.id.confirmOrder)
    Button confirm;

    List<Cart> localCart = new ArrayList<>();

    RelativeLayout rootLayout;

    OrdersAdapter ordersAdapter;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }

        compositeDisposable = new CompositeDisposable();
        
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(getBaseContext());

        String no_struck = SharedPrefManager.getInstance(this).getNoStruk();
        nostruck.setText("No. Transaksi : "+no_struck);

        DateFormat df = new SimpleDateFormat("YYYY-MM-dd");

        String date = df.format(Calendar.getInstance().getTime());
        date_trans.setText("Date :"+date);


        recyclerView = (RecyclerView) findViewById(R.id.cartRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        loadCartList();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.taborder:
                        Intent intent0 = new Intent(ChartActivity.this, OrderActivity.class);
                        startActivity(intent0);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.tabmenu:
                        Intent intent1 = new Intent(ChartActivity.this, MenuActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.tabchart:

                        break;
            }

                return false;

            }
        });

    }

    @OnClick(R.id.confirmOrder)
    public void setConfirm(View view){

        postOrder();

        Intent intent0 = new Intent(ChartActivity.this, OrderActivity.class);
        startActivity(intent0);
        overridePendingTransition(0, 0);
        Toast.makeText(this, "Pesanan anda terkirim, silahkan lakukan konfirmasi pembayaran !",
                Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartList();
    }

    private void loadCartList() {
        compositeDisposable.add(
                Server.cartRepository.getCartList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartList(carts);
                    }
                })
        );
        
    }

    private void displayCartList(List<Cart> carts) {
        localCart = carts;
        ordersAdapter = new OrdersAdapter(this, carts);
        recyclerView.setAdapter(ordersAdapter);
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof OrdersAdapter.ProductViewHolder){
            String name = localCart.get(viewHolder.getAdapterPosition()).nama_menu;

            final Cart deleteItem = localCart.get(viewHolder.getAdapterPosition());

            final int deletedIndex = viewHolder.getAdapterPosition();

            Log.d(TAG, "onSwiped: "+deletedIndex);

            ordersAdapter.removeItem(deletedIndex);

            Server.cartRepository.deleteCartItem(deleteItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" berhasil dihapus ! ").toString(),Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ordersAdapter.restoreItem(deleteItem,deletedIndex);
                    Server.cartRepository.insertToCart(deleteItem);
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void postOrder(){

        final String url = "http://gapakelama.net/JSON/postOrders.php";

        for(int i = 0; i <ordersAdapter.getItemCount(); i++){

            Log.d(TAG, "loop ke: "+i);

            final String id_transaksi = SharedPrefManager.getInstance(this).getNoStruk();
            final String id_menu = localCart.get(i).id_menu;
            final String qty_menu = String.valueOf(localCart.get(i).qty_menu);
            final String harga_menu = String.valueOf(localCart.get(i).harga_menu);
            final String catatan = localCart.get(i).catatan_menu;


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("myTag", response);
//                        Toast.makeText(DashboardActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("myTag", error.toString());
//                        Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("order_id",id_transaksi);
                    params.put("id_menu",id_menu);
                    params.put("qty", qty_menu);
                    params.put("harga", harga_menu);
                    params.put("catatan", catatan);
                    return params;
                }

            };
            requestQueue.add(stringRequest);
            Log.d(TAG, "postOrder: "+stringRequest);
        }

    }
}

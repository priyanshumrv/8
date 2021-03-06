package com.example.bookmart1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookmart1.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewProductActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_product);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef,AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewholder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewholder holder, final int position, @NonNull final AdminOrders model)
                    {
                        holder.username.setText("Name: "+model.getName());
                        holder.userPhoneNumber.setText("Phone: "+model.getPhone());
                        holder.userTotalPrice.setText("Total Amount= $: "+model.getAmount());
                        holder.userDataTime.setText("Order at: "+model.getDate() + " " +model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: "+model.getAddress() + " " +model.getCity());




                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[]= new CharSequence[]
                                        {

                                                "Yes",
                                                "no"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewProductActivity.this);
                                builder.setTitle("Have you shipped this order ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if(i==0)
                                        {
                                            String uid=getRef(position).getKey();

                                            RemoveOrder(uid);




                                        }
                                        else
                                        {
                                            finish();
                                        }

                                    }
                                });
                                builder.show();


                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewholder(view);
                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class AdminOrdersViewholder extends RecyclerView.ViewHolder
    {
        public TextView username,userPhoneNumber,userTotalPrice,userDataTime,userShippingAddress;
        public Button showOrderBttn;

        public AdminOrdersViewholder(@NonNull View itemView)
        {
            super(itemView);

            username=itemView.findViewById(R.id.order_user_name);
            userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDataTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address_city);



        }
    }

    private void RemoveOrder(String uid)
    {
        ordersRef.child(uid).removeValue();
    }
}

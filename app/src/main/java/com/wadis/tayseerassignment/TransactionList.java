package com.wadis.tayseerassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Objects;

public class TransactionList extends AppCompatActivity {

    private DatabaseReference mDatabaseTransactionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        final ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        mDatabaseTransactionsList = FirebaseDatabase.getInstance("https://tayseerassignment-87bf0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Transactions");
        mDatabaseTransactionsList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Log.d("snapshot", String.valueOf(postSnapshot));
                    Gson gson = new Gson();
                    Transaction transaction = gson.fromJson( String.valueOf(postSnapshot.getValue()),Transaction.class);
                    System.out.println(transaction.getAmtRec());
                    transactionArrayList.add(transaction);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    CustomAdapter customAdapter = new CustomAdapter(transactionArrayList);
                    recyclerView.setAdapter(customAdapter);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }
}
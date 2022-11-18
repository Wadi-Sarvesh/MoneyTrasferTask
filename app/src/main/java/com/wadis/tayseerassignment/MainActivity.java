package com.wadis.tayseerassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabasetrxns;
    private static final String TAG = "MainActivity";


    //Array for Dropdown menu
    String[] purpose = new String[]{
            "None Selected",
            "Expenses",
            "Admissions",
            "Bills",
            "Recharge"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Views
        final EditText sendName = findViewById(R.id.sendName);
        final EditText recName = findViewById(R.id.recName);

        EditText sendAmt = findViewById(R.id.sendAmt);
        final TextView recAmt = findViewById(R.id.amt);
        final Button makePayment = findViewById(R.id.makePayment);


        //Show amt recieved after 2 sec of inputting data
        sendAmt.addTextChangedListener(
                new TextWatcher() {
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    private Timer timer = new Timer();
                    private final long DELAY = 2000; // Delay of 2 sec after inputting data

                    @Override
                    public void afterTextChanged(final Editable s) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        final int sendAmount;
                                        if(!s.toString().equals(""))
                                            sendAmount = Integer.parseInt(Objects.requireNonNull(s.toString()));
                                        else
                                            sendAmount = 0;
                                        Log.d("sendAmt",String.valueOf(sendAmount));
                                        //Initialize Firebase
                                        FirebaseApp.initializeApp(MainActivity.this);
                                        //Get Ref of Exchange rate data( realtime DB)
                                        mDatabase = FirebaseDatabase.getInstance("https://tayseerassignment-87bf0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("ExchangeRate");

                                        Log.d("fBase",String.valueOf(mDatabase));
                                        mDatabase.addValueEventListener( new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                //  use the values to update the UI
                                                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                                    Log.d("Base",String.valueOf(postSnapshot.getValue()));
                                                    int USDtoINR = Integer.parseInt(Objects.requireNonNull(postSnapshot.getValue()).toString());
                                                    long RecievedAmt = (sendAmount)*(USDtoINR);
                                                    Log.d("recAmt",String.valueOf(RecievedAmt));
                                                    recAmt.setText(String.valueOf(RecievedAmt));
                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Getting Post failed, log a message
                                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                            }

                                        });
                                    }
                                },
                                DELAY // 2 sec of delay
                        );
                    }
                }
        );
        //Dropdown menu
        final Spinner spinner=findViewById(R.id.dropdown);
        List<String> purposeList = new ArrayList<>
                (Arrays.asList(purpose));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item, purposeList)
        {
            @Override
            public boolean isEnabled(int position){
            // Disable the first item from Spinner
            // First item will be used for hint
            return position != 0;
        }
            @Override
            public View getDropDownView(
            int position, View convertView,
                @NonNull ViewGroup parent) {

            // Get the item view
            View view = super.getDropDownView(
                    position, convertView, parent);
            TextView textView = (TextView) view;
            if(position == 0){
                // Set the hint text color gray
                textView.setTextColor(Color.GRAY);
            }
            else { textView.setTextColor(Color.BLACK); }
            return view;
        }};

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selPurpose = spinner.getSelectedItem().toString();
                String senderName = sendName.getText().toString();
                String receiverName = recName.getText().toString();
                Transaction transaction;
                String recAmount = recAmt.getText().toString();
                if( !senderName.equals("") && !receiverName.equals("") && !recAmount.equals(""))
                {transaction = new Transaction(senderName,receiverName,recAmount + " " + "₹",selPurpose);
                Gson gson = new Gson();
                final String json = gson.toJson(transaction);
                System.out.println(json);
                mDatabasetrxns = FirebaseDatabase.getInstance("https://tayseerassignment-87bf0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Transactions");
                System.out.println(mDatabasetrxns.toString());
                mDatabasetrxns.child(transaction.getTrxnID()).setValue(json);
                    Toast.makeText(getApplicationContext(),"Payment Done!",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please Enter Data",Toast.LENGTH_SHORT).show();



            }


    });

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showrecords, menu);
        return true;

    }
    //To do when menuitem is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.showRec:
                Intent i = new Intent(MainActivity.this, TransactionList.class);
                startActivity(i);
                break;
        }
        return true;
}}


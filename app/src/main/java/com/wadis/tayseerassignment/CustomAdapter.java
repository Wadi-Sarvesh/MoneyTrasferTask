package com.wadis.tayseerassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private List<Transaction> TransactionList;
    CustomAdapter(List<Transaction> transactionList){
        this.TransactionList = transactionList ;
    }
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, final int position) {
        final Transaction trxn = TransactionList.get(position);
        holder.senderNameDisp.setText("From " + trxn.getSender());
        holder.recNameDisp.setText( trxn.getReceiver());
        holder.amtRecDisp.setText(trxn.getAmtRec());
        holder.purposeDisp.setText(trxn.getPurpose());


    }
    @Override
    public int getItemCount() {
        return TransactionList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView senderNameDisp;
        private TextView recNameDisp;
        private TextView amtRecDisp;
        private TextView purposeDisp;


        public MyViewHolder(View itemView) {
            super(itemView);
            senderNameDisp = itemView.findViewById(R.id.senderNameDisp);
            recNameDisp = itemView.findViewById(R.id.recNameDisp);
            amtRecDisp = itemView.findViewById(R.id.amtRecDisp);
            purposeDisp = itemView.findViewById(R.id.purposeDisp);


        }
    }
}
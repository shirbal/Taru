package com.taru.taru.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taru.taru.R;
import com.taru.taru.models.enums.TransactionType;

/**
 * Created by Shiran Maor on 9/26/2015.
 */
public class TransactionAdapter extends ArrayAdapter<TransactionViewModel> {

    Context context;
    int layoutResourceId;
    TransactionViewModel data[] = null;

    public TransactionAdapter(Context context, int layoutResourceId, TransactionViewModel[] data) {
        super(context,layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TransactionHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TransactionHolder();
            holder.category = (TextView)row.findViewById(R.id.category);
            holder.date = (TextView)row.findViewById(R.id.transDate);
            holder.amount = (TextView)row.findViewById(R.id.transAmount);

            row.setTag(holder);
        }
        else
        {
            holder = (TransactionHolder)row.getTag();
        }

        TransactionViewModel transactionViewModel = data[position];
        if (transactionViewModel != null) {
            if(transactionViewModel.getType() == TransactionType.OUT) {
                holder.category.setTextColor(Color.RED);
                holder.date.setTextColor(Color.RED);
                holder.amount.setTextColor(Color.RED);
            } else {
                holder.category.setTextColor(Color.GREEN);
                holder.date.setTextColor(Color.GREEN);
                holder.amount.setTextColor(Color.GREEN);
            }
            holder.category.setText(transactionViewModel.getCategory());
            holder.date.setText(transactionViewModel.getDate());
            holder.amount.setText(Double.toString(transactionViewModel.getAmount()));
        }

        return row;
    }

    static class TransactionHolder
    {
        TextView category;
        TextView date;
        TextView amount;
    }

}

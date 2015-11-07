package com.taru.taru.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taru.taru.R;
import com.taru.taru.models.enums.TransactionType;

import java.util.List;

/**
 * Created by Shiran Maor on 11/3/15.
 */
public class DailyTransactionAdapter extends ArrayAdapter<DailyTransactionViewModel> {

    Context context;
    int layoutResourceId;
    List<DailyTransactionViewModel> data = null;

    public DailyTransactionAdapter(Context context, int layoutResourceId,
                                   List<DailyTransactionViewModel> data) {
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
            holder.category = (TextView)row.findViewById(R.id.daily_category);
            holder.amount = (TextView)row.findViewById(R.id.daily_amount);

            row.setTag(holder);
        }
        else
        {
            holder = (TransactionHolder)row.getTag();
        }

        DailyTransactionViewModel transactionViewModel = data.get(position);
        if (transactionViewModel != null) {
            if(transactionViewModel.getType() == TransactionType.OUT) {
                holder.category.setTextColor(getContext().getResources().getColor( R.color.red));
                holder.amount.setTextColor(getContext().getResources().getColor( R.color.red));
            } else {
                holder.category.setTextColor(getContext().getResources().getColor( R.color.green));
                holder.amount.setTextColor(getContext().getResources().getColor( R.color.green));
            }

            holder.category.setText(transactionViewModel.getCategory());
            setPrice(holder.amount,transactionViewModel.getAmount());

        }

        return row;
    }

    private void setPrice(TextView text, Double amount) {
        int i = amount.intValue();
        text.setText("$"+i);
    }

    static class TransactionHolder
    {
        TextView category;
        TextView amount;
    }

}
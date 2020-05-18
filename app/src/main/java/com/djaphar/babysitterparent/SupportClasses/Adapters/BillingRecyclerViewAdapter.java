package com.djaphar.babysitterparent.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.babysitterparent.Fragments.BillingFragment;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Bill;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.ViewDriver;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillingRecyclerViewAdapter extends RecyclerView.Adapter<BillingRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Bill> bills;
    private BillingFragment billingFragment;

    public BillingRecyclerViewAdapter(ArrayList<Bill> bills, BillingFragment billingFragment) {
        this.bills = bills;
        this.billingFragment = billingFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.billing_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView -> billingFragment.openBillContainer(bills.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.billingListThemeTv.setText(bill.getTheme());
//        holder.billingListTargetTv.setText("");
        float price = bill.getSum();
        String priceStr;
        if (price == (int) price) {
            priceStr = (int) price + "р.";
        } else {
            priceStr = String.format(Locale.US, "%.2f", price) + "р";
        }
        holder.billingListPriceTv.setText(priceStr);
        ViewDriver.setStatusTvOptions(holder.billingListStatusTv, billingFragment.getResources(), bill.getStatus());
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parentLayout;
        private TextView billingListThemeTv, billingListPriceTv, billingListStatusTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_billing);
            billingListThemeTv = itemView.findViewById(R.id.billing_list_theme_tv);
            billingListPriceTv = itemView.findViewById(R.id.billing_list_price_tv);
            billingListStatusTv = itemView.findViewById(R.id.billing_list_status_tv);
        }
    }
}

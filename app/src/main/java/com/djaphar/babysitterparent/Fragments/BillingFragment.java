package com.djaphar.babysitterparent.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.Adapters.BillingRecyclerViewAdapter;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Bill;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitterparent.ViewModels.BillingViewModel;

import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BillingFragment extends MyFragment {

    private BillingViewModel billingViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView billingRecyclerView;
    private RelativeLayout billingListLayout;
    private ConstraintLayout billInfoContainer;
    private TextView billThemeContent, billPriceContent, billStatusContent, billDescriptionContent;
    private HashMap<String, String> authHeader = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        billingViewModel = new ViewModelProvider(this).get(BillingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_billing, container, false);
        billingRecyclerView = root.findViewById(R.id.billing_recycler_view);
        billingListLayout = root.findViewById(R.id.billing_list_layout);
        billInfoContainer = root.findViewById(R.id.bill_info_container);
        billThemeContent = root.findViewById(R.id.bill_theme_content);
        billPriceContent = root.findViewById(R.id.bill_price_content);
        billStatusContent = root.findViewById(R.id.bill_status_content);
        billDescriptionContent = root.findViewById(R.id.bill_description_content);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_billing));
            setBackBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        billingViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            billingViewModel.requestMyBills(authHeader);
        });

        billingViewModel.getBills().observe(getViewLifecycleOwner(), bills -> {
            if (bills == null) {
                return;
            }
            billingRecyclerView.setAdapter(new BillingRecyclerViewAdapter(bills, this));
            billingRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });
    }

    public boolean everythingIsClosed() {
        return billInfoContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        billingViewModel.requestMyBills(authHeader);
        setActionBarTitle(getString(R.string.title_billing));
        setBackBtnState(View.GONE);
        billingListLayout.setVisibility(View.VISIBLE);
        ViewDriver.hideView(billInfoContainer, R.anim.hide_right_animation, context);
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    public void openBillContainer(Bill bill) {
        setBillOptions(bill);
        setActionBarTitle(getString(R.string.bill_container_title));
        setBackBtnState(View.VISIBLE);
        ViewDriver.showView(billInfoContainer, R.anim.show_right_animation, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                billingListLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private void setBillOptions(Bill bill) {
        String theme = bill.getTheme();
        if (theme == null) {
            theme = getString(R.string.some_field_is_null);
        }
        billThemeContent.setText(theme);

        float sum = bill.getSum();
        if (sum == (int) sum) {
            billPriceContent.setText(String.valueOf((int) sum));
        } else {
            billPriceContent.setText(String.format(Locale.US, "%.2f", sum));
        }

        String comment = bill.getComment();
        if (comment == null) {
            comment = getString(R.string.some_field_is_null);
        }
        billDescriptionContent.setText(comment);

        ViewDriver.setStatusTvOptions(billStatusContent, getResources(), bill.getStatus());
    }
}

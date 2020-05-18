package com.djaphar.babysitterparent.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Event;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Meal;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Parent;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Ration;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitterparent.ViewModels.ChildViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

public class ChildFragment extends MyFragment implements DatePickerDialog.OnDateSetListener {

    private ChildViewModel childViewModel;
    private MainActivity mainActivity;
    private Context context;
    private ScrollView eventSv;
    private LinearLayout eventOpenContainer, eventDateContainer, eatingEventContainer;
    private ConstraintLayout kidInfoContainer, eventContainer, kidAddContainer;
    private ImageView kidPhoto;
    private TextView kidNameContent, kidPatronymicContent, kidSurnameContent, kidAgeContent, kidLockerContent, kidBloodTypeContent, eventDateContent,
    scheduleArriveContent, scheduleLeaveContent, sleepStartContent, sleepEndContent, otherTv, eatingEventContent, eatingFoodContent, eatingDenialContent;
    private EditText kidAddEd;
    private Button kidAddBtn;
    private Parent currentParent;
    private Child child;
    private Event event;
    private Calendar calendar;
    private int curYear, curMonth, curDayOfMonth, mealType;
    private HashMap<String, String> authHeader = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childViewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        View root = inflater.inflate(R.layout.fragment_child, container, false);
        eventSv = root.findViewById(R.id.event_sv);
        eventOpenContainer = root.findViewById(R.id.event_open_container);
        eventDateContainer = root.findViewById(R.id.event_date_container);
        kidInfoContainer = root.findViewById(R.id.kid_info_container);
        eventContainer = root.findViewById(R.id.event_container);
        kidAddContainer = root.findViewById(R.id.kid_add_container);
        eatingEventContainer = root.findViewById(R.id.eating_event_container);
        kidPhoto = root.findViewById(R.id.kid_photo);
        kidNameContent = root.findViewById(R.id.kid_name_content);
        kidPatronymicContent = root.findViewById(R.id.kid_patronymic_content);
        kidSurnameContent = root.findViewById(R.id.kid_surname_content);
        kidAgeContent = root.findViewById(R.id.kid_age_content);
        kidLockerContent = root.findViewById(R.id.kid_locker_content);
        kidBloodTypeContent = root.findViewById(R.id.kid_blood_type_content);
        eventDateContent = root.findViewById(R.id.event_date_content);
        scheduleArriveContent = root.findViewById(R.id.schedule_arrive_content);
        scheduleLeaveContent = root.findViewById(R.id.schedule_leave_content);
        sleepStartContent = root.findViewById(R.id.sleep_start_content);
        sleepEndContent = root.findViewById(R.id.sleep_end_content);
        otherTv = root.findViewById(R.id.other_tv);
        eatingEventContent = root.findViewById(R.id.eating_event_content);
        eatingFoodContent = root.findViewById(R.id.eating_food_content);
        eatingDenialContent = root.findViewById(R.id.eating_denial_content);
        kidAddEd = root.findViewById(R.id.kid_add_ed);
        kidAddBtn = root.findViewById(R.id.kid_add_btn);
        context = getContext();
        calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_kid));
            setBackBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        childViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            childViewModel.requestProfile(authHeader);
        });

        childViewModel.getParent().observe(getViewLifecycleOwner(), parent -> {
            if (parent == null) {
                return;
            }
            currentParent = parent;
            childViewModel.requestMyChild(authHeader);
        });

        childViewModel.getChild().observe(getViewLifecycleOwner(), child -> {
            if (child == null) {
                kidAddContainer.setVisibility(View.VISIBLE);
                kidInfoContainer.setVisibility(View.GONE);
                return;
            }
            this.child = child;
            setKidInfo();
            kidAddContainer.setVisibility(View.GONE);
            kidInfoContainer.setVisibility(View.VISIBLE);
        });

        childViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) {
                return;
            }
            this.event = event;
            mealType = 1;
            setEventOptions();
        });

        kidAddEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                kidAddBtn.setEnabled(!editable.toString().equals(""));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });

        eventOpenContainer.setOnClickListener(lView -> showEvent());

        kidAddBtn.setOnClickListener(lView -> {
            currentParent.setChildId(kidAddEd.getText().toString());
            childViewModel.requestUpdateProfile(authHeader, currentParent);
        });

        eventDateContainer.setOnClickListener(lView -> new DatePickerDialog(mainActivity, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        eatingEventContainer.setOnClickListener(lView -> {
            View inflatedView = View.inflate(context, R.layout.meal_event_dialog, null);
            AlertDialog dialog = new AlertDialog.Builder(mainActivity).setView(inflatedView).create();
            dialog.show();
            inflatedView.findViewById(R.id.breakfast).setOnClickListener(mealTv -> {
                mealType = 1;
                setMealTvOptions();
                dialog.cancel();
            });
            inflatedView.findViewById(R.id.lunch).setOnClickListener(mealTv -> {
                mealType = 2;
                setMealTvOptions();
                dialog.cancel();
            });
            inflatedView.findViewById(R.id.snack).setOnClickListener(mealTv -> {
                mealType = 3;
                setMealTvOptions();
                dialog.cancel();
            });
        });
    }

    public boolean everythingIsClosed() {
        return eventContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        setActionBarTitle(getString(R.string.title_kid));
        setBackBtnState(View.GONE);
        kidInfoContainer.setVisibility(View.VISIBLE);
        ViewDriver.hideView(eventContainer, R.anim.hide_right_animation, context);
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    private void setKidInfo() {
        Glide.with(context).load(child.getPhotoLink()).into(kidPhoto);

        String name  = child.getName();
        if (name == null || name.equals("")) {
            name = getString(R.string.some_field_is_null);
        }
        kidNameContent.setText(name);

        String patronymic = child.getPatronymic();
        if (patronymic == null || patronymic.equals("")) {
            patronymic = getString(R.string.some_field_is_null);
        }
        kidPatronymicContent.setText(patronymic);

        String surname = child.getSurname();
        if (surname == null || surname.equals("")) {
            surname = getString(R.string.some_field_is_null);
        }
        kidSurnameContent.setText(surname);

        Integer age = child.getAge();
        String ageStr = getString(R.string.some_field_is_null);
        if (age != null) {
            ageStr = age.toString();
        }
        kidAgeContent.setText(ageStr);

        String locker = child.getLockerNum();
        if (locker == null || locker.equals("")) {
            locker = getString(R.string.some_field_is_null);
        }
        kidLockerContent.setText(locker);

        String bloodType = child.getBloodType();
        if (bloodType == null || bloodType.equals("")) {
            bloodType = getString(R.string.some_field_is_null);
        }
        kidBloodTypeContent.setText(bloodType);
    }

    private void showEvent() {
        setCalendarOptions(curYear, curMonth, curDayOfMonth);
        childViewModel.requestEvent(authHeader, child.getChildId(), getDateForRequest());
        eventDateContent.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        setActionBarTitle(getString(R.string.events));
        setBackBtnState(View.VISIBLE);
        eventSv.scrollTo(0, eventSv.getTop());
        ViewDriver.showView(eventContainer, R.anim.show_right_animation, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                kidInfoContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private String getDateForRequest() {
        @SuppressLint("DefaultLocale") String date =
                String.format("%04d", calendar.get(Calendar.YEAR)) + "-" +
                String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    private void setCalendarOptions(int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        setCalendarOptions(year, month, dayOfMonth);
        childViewModel.requestEvent(authHeader, child.getChildId(), getDateForRequest());
        eventDateContent.setText(DateFormat.getDateInstance().format(calendar.getTime()));
    }

    private void setEventOptions() {
        setEventTv(event.getHasCome(), scheduleArriveContent);
        setEventTv(event.getHasGone(), scheduleLeaveContent);
        setEventTv(event.getAsleep(), sleepStartContent);
        setEventTv(event.getAwoke(), sleepEndContent);
        setEventTv(event.getComment(), otherTv);
        setMealTvOptions();
    }

    private void setEventTv(String value, TextView view) {
        if (value == null || value.equals("")) {
            value = getString(R.string.some_field_is_null);
        }
        view.setText(value);
    }

    private void setMealTvOptions() {
        switch (mealType) {
            case 1:
                eatingEventContent.setText(R.string.breakfast);
                break;
            case 2:
                eatingEventContent.setText(R.string.lunch);
                break;
            case 3:
                eatingEventContent.setText(R.string.snack);
                break;
        }

        ArrayList<Ration> rations = new ArrayList<>();
        ArrayList<Meal> meals = event.getMeals();
        if (meals != null) {
            for (Meal meal : meals) {
                if (meal.getType() == mealType) {
                    rations = meal.getRations();
                    break;
                }
            }
        }

        StringBuilder foodContent = new StringBuilder();
        ArrayList<Ration> denied = new ArrayList<>();
        for (int i = 0; i < rations.size(); i++) {
            Ration ration = rations.get(i);
            foodContent.append(ration.getName());
            if (i != rations.size() - 1) {
                foodContent.append("\n");
            }
            if (ration.getDenial()) {
                denied.add(ration);
            }
        }

        StringBuilder deniedContent = new StringBuilder();
        for (int i = 0; i < denied.size(); i++) {
            deniedContent.append(denied.get(i).getName());
            if (i != denied.size() - 1) {
                deniedContent.append("\n");
            }
        }

        eatingFoodContent.setText(foodContent.toString());
        eatingDenialContent.setText(deniedContent.toString());
    }
}

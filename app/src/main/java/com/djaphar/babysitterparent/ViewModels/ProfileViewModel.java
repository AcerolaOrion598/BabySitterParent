package com.djaphar.babysitterparent.ViewModels;

import android.app.Application;

import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Parent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<Parent> parentMutableLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        Parent parent1 = new Parent("Какой-то", "Азиатский", "Мужык", "Батя", "88005553535",
                "https://cdn.v2ex.com/gravatar/704e7c12cdc2a663fd7c6521dd8a332d?s=1000&d=mm");
        parentMutableLiveData.setValue(parent1);
    }

    public MutableLiveData<Parent> getParent() {
        return parentMutableLiveData;
    }
}
package com.djaphar.babysitterparent.ViewModels;

import android.app.Application;

import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Kid;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Parent;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class KidViewModel extends AndroidViewModel {

    private MutableLiveData<Kid> kidMutableLiveData = new MutableLiveData<>();

    public KidViewModel(@NonNull Application application) {
        super(application);
        Parent parent1 = new Parent("Какой-то", "Азиатский", "Мужык", "Батя", "88005553535",
                "https://cdn.v2ex.com/gravatar/704e7c12cdc2a663fd7c6521dd8a332d?s=1000&d=mm");
        Parent parent2 = new Parent("Ещё Один", "Азиатский", "Мужык", "Второй Батя", "89999999999",
                "https://sun1-83.userapi.com/8VIxiZCE8p0V-eWKGd0erYau3aht-N4Yjo5U1g/GfdITZDwnIA.jpg");
        ArrayList<Parent> parents = new ArrayList<>();
        parents.add(parent1);
        parents.add(parent2);
        Kid kid = new Kid("Мики", "Топ I", "Саяка", "14 лет", "Супер Магическая I",
                "https://cs9.pikabu.ru/post_img/big/2017/05/12/10/1494605959124044472.jpg",
                228, parents,1234);
        kidMutableLiveData.setValue(kid);
    }

    public MutableLiveData<Kid> getKid() {
        return kidMutableLiveData;
    }
}
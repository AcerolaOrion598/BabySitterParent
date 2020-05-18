package com.djaphar.babysitterparent.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitterparent.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Bill;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.MainApi;
import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.User;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private MutableLiveData<ArrayList<Bill>> billsMutableLiveData = new MutableLiveData<>();
    private MainApi mainApi;

    public BillingViewModel(@NonNull Application application) {
        super(application);
        userLiveData = LocalDataRoom.getDatabase(application).localDataDao().getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<ArrayList<Bill>> getBills() {
        return billsMutableLiveData;
    }

    public void requestMyBills(HashMap<String, String> headersMap) {
        mainApi.requestMyBills(headersMap).enqueue(new Callback<ArrayList<Bill>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Bill>> call, @NonNull Response<ArrayList<Bill>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                billsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Bill>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

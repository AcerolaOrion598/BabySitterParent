package com.djaphar.babysitterparent.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitterparent.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Event;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.MainApi;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Parent;
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

public class ChildViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private MutableLiveData<Parent> parentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Child> childMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> eventMutableLiveData = new MutableLiveData<>();
    private MainApi mainApi;

    public ChildViewModel(@NonNull Application application) {
        super(application);
        userLiveData = LocalDataRoom.getDatabase(application).localDataDao().getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<Parent> getParent() {
        return parentMutableLiveData;
    }

    public MutableLiveData<Child> getChild() {
        return childMutableLiveData;
    }

    public MutableLiveData<Event> getEvent() {
        return eventMutableLiveData;
    }

    public void requestProfile(HashMap<String, String> headersMap) {
        mainApi.requestProfile(headersMap).enqueue(new Callback<Parent>() {
            @Override
            public void onResponse(@NonNull Call<Parent> call, @NonNull Response<Parent> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                parentMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Parent> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUpdateProfile(HashMap<String, String> headersMap, Parent parent) {
        mainApi.requestUpdateProfile(headersMap, parent).enqueue(new Callback<Parent>() {
            @Override
            public void onResponse(@NonNull Call<Parent> call, @NonNull Response<Parent> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                parentMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Parent> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestMyChild(HashMap<String, String> headersMap) {
        mainApi.requestMyChild(headersMap).enqueue(new Callback<Child>() {
            @Override
            public void onResponse(@NonNull Call<Child> call, @NonNull Response<Child> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        childMutableLiveData.setValue(null);
                        return;
                    }
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                childMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Child> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestEvent(HashMap<String, String> headersMap, String childId, String date) {
        mainApi.requestEvent(headersMap, childId, date).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        eventMutableLiveData.setValue(new Event(childId, date, null,
                                null, null, null, null, new ArrayList<>()));
                        return;
                    }
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                eventMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.djaphar.babysitterparent.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.MainApi;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Parent;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.UpdatePictureModel;
import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.User;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private MutableLiveData<Parent> parentMutableLiveData = new MutableLiveData<>();
    private MainApi mainApi;

    public ProfileViewModel(@NonNull Application application) {
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

    public void requestUpdateParent(HashMap<String, String> headersMap, Parent parent) {
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

    public void requestUpdatePicture(HashMap<String, String> headersMap, UpdatePictureModel updatePictureModel) {
        mainApi.requestUpdatePicture(headersMap, updatePictureModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplication(), R.string.picture_update_success, Toast.LENGTH_SHORT).show();
                requestProfile(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDeletePicture(HashMap<String, String> headersMap, UpdatePictureModel updatePictureModel) {
        mainApi.requestDeletePicture(headersMap, updatePictureModel).enqueue(new Callback<Parent>() {
            @Override
            public void onResponse(@NonNull Call<Parent> call, @NonNull Response<Parent> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Parent currentParent = parentMutableLiveData.getValue();
                if (currentParent == null) {
                    return;
                }

                Parent responseParent = response.body();
                if (responseParent == null) {
                    return;
                }
                currentParent.setPhotoLink(responseParent.getPhotoLink());
                parentMutableLiveData.setValue(currentParent);
            }

            @Override
            public void onFailure(@NonNull Call<Parent> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
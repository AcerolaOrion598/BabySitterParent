package com.djaphar.babysitterparent.ViewModels;

import android.app.Application;

import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.LocalDataDao;
import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SettingsViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private LocalDataDao dao;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        dao = LocalDataRoom.getDatabase(application).localDataDao();
        userLiveData = dao.getUser();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void logout() {
        LocalDataRoom.databaseWriteExecutor.execute(() -> dao.deleteUser());
    }
}
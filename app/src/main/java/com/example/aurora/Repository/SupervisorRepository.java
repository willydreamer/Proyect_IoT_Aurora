/*package com.example.aurora.Repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aurora.Bean.Supervisor;
import com.example.aurora.Dao.SupervisorDao;
import com.example.aurora.Database.SupervisorDatabase;

import java.util.List;

public class SupervisorRepository extends ViewModel {

    private SupervisorDao supervisorDao;

    private MutableLiveData<List<Supervisor>> listaSupervisores = new MutableLiveData<>();

    public SupervisorRepository(Application application){
        SupervisorDatabase supervisorDatabase = SupervisorDatabase.getDatabase(application);
        supervisorDao = supervisorDatabase.supervisorDao();
    }

    public MutableLiveData<List<Supervisor>> getListaSupervisores() {
        return listaSupervisores;
    }

    public void setListaSupervisores(MutableLiveData<List<Supervisor>> listaSupervisores) {
        this.listaSupervisores = listaSupervisores;
    }

    public SupervisorDao getSupervisorDao() {
        return supervisorDao;
    }

    public void setSupervisorDao(SupervisorDao supervisorDao) {
        this.supervisorDao = supervisorDao;
    }
} */

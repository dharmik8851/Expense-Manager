package com.example.expensemanager.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.views.activities.MainActivity;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivityViewModel extends AndroidViewModel {

    Realm realm;
    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transaction>> categoryTransactions = new MutableLiveData<>();

    public MutableLiveData<Double> income = new MutableLiveData<>();
    public MutableLiveData<Double> expense = new MutableLiveData<>();
    public MutableLiveData<Double> total = new MutableLiveData<>();

    public MutableLiveData<Calendar> selectedDate = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application.getApplicationContext());
        init();
    }

    public void calculateAmount() {
        double incomeTemp = 0, expenseTemp = 0;
        for (Transaction t : transactions.getValue()) {
            if (t.getType().equals(Constants.INCOME)) {
                incomeTemp += t.getAmount();
            } else if (t.getType().equals(Constants.EXPENSE)) {
                expenseTemp += t.getAmount();
            }
        }
        income.setValue(incomeTemp);
        expense.setValue(expenseTemp);
        total.setValue(incomeTemp - expenseTemp);
    }

    public void getTransactions(){
        Calendar c = selectedDate.getValue();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        realm.beginTransaction();
        if(Constants.SELECTED_TAB == Constants.DAILY){
            RealmResults<Transaction> transactionsByDate =
                    realm.where(Transaction.class)
                            .greaterThanOrEqualTo("date", c.getTime())
                            .lessThan("date", new Date(c.getTime().getTime() + (24 * 60 * 60 * 1000)))
                            .findAll();
            transactions.setValue(transactionsByDate);
        }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
            c.set(Calendar.DAY_OF_MONTH,0);
            Date startDate = c.getTime();
            c.add(Calendar.MONTH, 1);
            Date endDate = c.getTime();
            RealmResults<Transaction> transactionsByDate =
                    realm.where(Transaction.class)
                            .greaterThan("date", startDate)
                            .lessThanOrEqualTo("date", endDate)
                            .findAll().sort("date");
            transactions.setValue(transactionsByDate);
        }
        realm.commitTransaction();
        calculateAmount();
    }

    public void getTransactions(String type){
        Calendar c = selectedDate.getValue();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        realm.beginTransaction();
        if(Constants.SELECTED_TAB == Constants.DAILY){
            RealmResults<Transaction> transactionsByDate =
                    realm.where(Transaction.class)
                            .greaterThanOrEqualTo("date", c.getTime())
                            .lessThan("date", new Date(c.getTime().getTime() + (24 * 60 * 60 * 1000)))
                            .equalTo("type",type)
                            .findAll();
            categoryTransactions.setValue(transactionsByDate);
        }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
            c.set(Calendar.DAY_OF_MONTH,0);
            Date startDate = c.getTime();
            c.add(Calendar.MONTH, 1);
            Date endDate = c.getTime();
            RealmResults<Transaction> transactionsByDate =
                    realm.where(Transaction.class)
                            .greaterThan("date", startDate)
                            .lessThanOrEqualTo("date", endDate)
                            .equalTo("type",type)
                            .findAll();
            categoryTransactions.setValue(transactionsByDate);
        }
        realm.commitTransaction();
        calculateAmount();
    }

    public void addTransaction(Transaction transaction) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);
        realm.commitTransaction();
        getTransactions();
    }



    private void init() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        selectedDate.setValue(Calendar.getInstance());
    }


}

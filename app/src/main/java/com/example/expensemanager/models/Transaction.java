package com.example.expensemanager.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Transaction extends RealmObject {
    private String type, category, account, note;
    private double amount;
    private Date date;

    @PrimaryKey
    private long id;
    public Transaction() {
    }

    public Transaction(String type, String category, String account, String note, double amount, Date date, long id) {
        this.type = type;
        this.category = category;
        this.account = account;
        this.note = note;
        this.amount = amount;
        this.date = date;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", account='" + account + '\'' +
                ", note='" + note + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}

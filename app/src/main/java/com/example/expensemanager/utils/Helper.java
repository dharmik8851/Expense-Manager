package com.example.expensemanager.utils;

import android.content.Context;

import com.example.expensemanager.R;
import com.example.expensemanager.models.Account;
import com.example.expensemanager.models.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Helper {
    public static List<Category> categoryList = new ArrayList<>();
    public static List<Account> accountList = new ArrayList<>();

    public static void setCategoryList() {
        categoryList.clear();
        categoryList.add(new Category("Salary", R.drawable.ic_salary, R.color.category1));
        categoryList.add(new Category("Business", R.drawable.ic_business, R.color.category2));
        categoryList.add(new Category("Investment", R.drawable.ic_investment, R.color.category3));
        categoryList.add(new Category("Loan", R.drawable.ic_loan, R.color.category4));
        categoryList.add(new Category("Rent", R.drawable.ic_rent,R.color.category5));
        categoryList.add(new Category("Other", R.drawable.ic_other,R.color.category6));
    }

    public static void setAccountList(){
        accountList.clear();
        accountList.add(new Account(0, "Cash"));
        accountList.add(new Account(0, "Bank"));
        accountList.add(new Account(0, "EasyPaisa"));
        accountList.add(new Account(0, "PayTM"));
        accountList.add(new Account(0, "Other"));
    }

    public static String formateDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy");
        return sdf.format(date);
    }

    public static String formateDateByMonth(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy");
        return sdf.format(date);
    }

    public static int getColorByTransactionType(String type) {
        if(type.equals(Constants.INCOME)){
            return R.color.green;
        }
        else if(type.equals(Constants.EXPENSE)){
            return R.color.red;
        }
        return 0;
    }

    public static Category getCategoryByName(String category) {
        for(Category cat : categoryList){
            if (cat.getCategoryName().equals(category)) return cat;
        }
        return null;
    }

    public static int getAccountColor(String account, Context context) {
        if(account.equals(context.getString(R.string.cash))){
            return R.color.cash_color;
        }
        else if(account.equals(context.getString(R.string.bank))){
            return R.color.bank_color;
        }else {
            return R.color.default_color;
        }
    }
}

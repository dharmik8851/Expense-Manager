package com.example.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.AccountItemBinding;
import com.example.expensemanager.models.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    Context context;
    List<Account> accountList = new ArrayList<>();

    public interface AccountListener{void onAccountClick(Account account);}

    AccountListener accountListener;

    public AccountAdapter(Context context, List<Account> accountList, AccountListener accountListener) {
        this.context = context;
        this.accountList = accountList;
        this.accountListener = accountListener;
    }

    @NonNull
    @Override
    public AccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.account_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.MyViewHolder holder, int position) {
        holder.itemBinding.accountName.setText(accountList.get(position).getAccountName());
        holder.itemView.setOnClickListener(v -> {
            accountListener.onAccountClick(accountList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        AccountItemBinding itemBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBinding = AccountItemBinding.bind(itemView);
        }
    }
}

package com.example.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowTransactionBinding;
import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Helper;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;
    List<Transaction> transactionList;

    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.transactionAmount.setTextColor(context.getColor(Helper.getColorByTransactionType(transaction.getType())));
        holder.binding.transactionDate.setText(Helper.formateDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());
        Category category = Helper.getCategoryByName(transaction.getCategory());
        holder.binding.transactionImg.setImageResource(category.getCategoryImage());
        holder.binding.transactionImg.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));
        holder.binding.accountBill.setText(transaction.getAccount());
        holder.binding.accountBill.setBackgroundTintList(context.getColorStateList(Helper.getAccountColor(transaction.getAccount(), context)));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        RowTransactionBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}

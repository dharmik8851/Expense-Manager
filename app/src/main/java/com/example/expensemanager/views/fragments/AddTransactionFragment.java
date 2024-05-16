package com.example.expensemanager.views.fragments;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.AccountAdapter;
import com.example.expensemanager.adapters.CategoryAdapter;
import com.example.expensemanager.databinding.FragmentAddTransactionBinding;
import com.example.expensemanager.databinding.ListDialogBinding;
import com.example.expensemanager.models.Account;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    private FragmentAddTransactionBinding b;
    BottomSheetBehavior<View> bottomSheetBehavior;
    Transaction transaction = new Transaction();
    public AddTransactionFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Calendar selectedDate = Calendar.getInstance();
        b = FragmentAddTransactionBinding.inflate(inflater);
        transaction.setDate(MainActivity.viewModel.selectedDate.getValue().getTime());
        b.selectDate.setText(Helper.formateDate(MainActivity.viewModel.selectedDate.getValue().getTime()));
        b.selectDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(getContext());
            dialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);
                transaction.setDate(selectedDate.getTime());
                b.selectDate.setText(Helper.formateDate(selectedDate.getTime()));
            });
            dialog.show();
        });

        b.categoryInput.setOnClickListener(v -> {
            ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(listDialogBinding.getRoot());
            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Helper.categoryList,
                    category -> {
                b.categoryInput.setText(category.getCategoryName());
                transaction.setCategory(category.getCategoryName());
                categoryDialog.dismiss();
            });
            listDialogBinding.itemList.setAdapter(categoryAdapter);
            listDialogBinding.itemList.setLayoutManager(new GridLayoutManager(getContext(), 3));
            categoryDialog.show();
        });

        b.accountInput.setOnClickListener(v -> {
            ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(listDialogBinding.getRoot());
            listDialogBinding.itemList.setLayoutManager(new LinearLayoutManager(getContext()));
            List<Account> accountList = Helper.accountList;
            AccountAdapter accountAdapter = new AccountAdapter(getContext(), accountList, account -> {
                b.accountInput.setText(account.getAccountName());
                transaction.setAccount(account.getAccountName());
                accountDialog.dismiss();
            });
            listDialogBinding.itemList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            listDialogBinding.itemList.setAdapter(accountAdapter);
            accountDialog.show();
        });


        b.toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId == R.id.incomeBtn && isChecked){
                    b.incomeBtn.setBackgroundColor(getResources().getColor(R.color.green, null));
                    b.expenseBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
                }else if(isChecked){
                    b.expenseBtn.setBackgroundColor(getResources().getColor(R.color.red, null));
                    b.incomeBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
                }
            }
        });

        b.toggleButton.check(R.id.incomeBtn);

        b.saveBtn.setOnClickListener(v -> {

            if(b.toggleButton.getCheckedButtonId() == R.id.incomeBtn){
                transaction.setType(Constants.INCOME);
            }else if(b.toggleButton.getCheckedButtonId() == R.id.expenseBtn){
                transaction.setType(Constants.EXPENSE);
            }


            String amount = b.amountInput.getText().toString();
            if(amount.isEmpty()) {
                b.amountInput.setError("Empty field");
                return;
            }
            else {
                b.amountInput.setError(null);
                transaction.setAmount(Double.parseDouble(amount));
            }

            if(b.categoryInput.getText().toString().isEmpty()){
                b.categoryInput.setError("Empty field"); return;
            }
            if(b.accountInput.getText().toString().isEmpty()){
                b.accountInput.setError("Empty field"); return;
            }

            String note = b.noteInput.getText().toString();
            if(!note.isEmpty()){
                    transaction.setNote(note);
            }
            transaction.setId(new Date().getTime());
            MainActivity.viewModel.selectedDate.setValue(selectedDate);
            MainActivity.viewModel.addTransaction(transaction);
            Toast.makeText(getContext(), "Transation saved", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    }
}
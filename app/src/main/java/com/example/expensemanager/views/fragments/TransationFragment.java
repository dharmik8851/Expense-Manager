package com.example.expensemanager.views.fragments;

import static com.example.expensemanager.views.activities.MainActivity.viewModel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.TransactionAdapter;
import com.example.expensemanager.databinding.FragmentTransationBinding;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Objects;

public class TransationFragment extends Fragment {

    public TransationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentTransationBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransationBinding.inflate(inflater);
        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.transactionList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        Constants.SELECTED_TAB = Constants.DAILY;
        viewModel.selectedDate.setValue(Calendar.getInstance());
        viewModel.getTransactions();


        viewModel.transactions.observe(getViewLifecycleOwner(), transactions -> {
            binding.transactionList.setAdapter(new TransactionAdapter(getContext(), transactions));
            if(viewModel.transactions.getValue().size() == 0){
                binding.transactionList.setVisibility(View.GONE);
                binding.emptyStateImg.setVisibility(View.VISIBLE);
            }
            else{
                binding.transactionList.setVisibility(View.VISIBLE);
                binding.emptyStateImg.setVisibility(View.GONE);
            }
        });

        viewModel.selectedDate.observe(getViewLifecycleOwner(), calendar -> {
            viewModel.getTransactions();
            if(Constants.SELECTED_TAB == Constants.DAILY) {
                binding.currentDate.setText(Helper.formateDate(viewModel.selectedDate.getValue().getTime()));
            }
            else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                binding.currentDate.setText(Helper.formateDateByMonth(viewModel.selectedDate.getValue().getTime()));
            }
        });

        viewModel.income.observe(getViewLifecycleOwner(), aDouble -> binding.txtIncomeValue.setText(String.valueOf(aDouble)));

        viewModel.expense.observe(getViewLifecycleOwner(), aDouble -> binding.txtExpenseValue.setText(String.valueOf(aDouble)));

        viewModel.total.observe(getViewLifecycleOwner(), aDouble -> binding.txtTotalValue.setText(String.valueOf(aDouble)));

        binding.addBtn.setOnClickListener(v -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.show(getParentFragmentManager(), null);
        });

        binding.previousDateBtn.setOnClickListener(v -> {
            Calendar c = viewModel.selectedDate.getValue();
            if(Constants.SELECTED_TAB == Constants.DAILY){
                c.add(Calendar.DATE,-1);
            }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                c.add(Calendar.MONTH,-1);
            }
            viewModel.selectedDate.setValue(c);
        });

        binding.nextDateBtn.setOnClickListener(v -> {
            Calendar c = viewModel.selectedDate.getValue();
            if(Constants.SELECTED_TAB == Constants.DAILY){
                c.add(Calendar.DATE,1);
            }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                c.add(Calendar.MONTH,1);
            }
            viewModel.selectedDate.setValue(c);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(Objects.equals(tab.getText(), getString(R.string.daily))){
                    Constants.SELECTED_TAB = Constants.DAILY;
                    viewModel.selectedDate.setValue(Calendar.getInstance());
                    viewModel.getTransactions();
                }
                else if (Objects.equals(tab.getText(), getString(R.string.monthly))) {
                    Constants.SELECTED_TAB = Constants.MONTHLY;
                    binding.currentDate.setText(Helper.formateDateByMonth(viewModel.selectedDate.getValue().getTime()));
                    viewModel.getTransactions();
                }
                else if(Objects.equals(tab.getText(), getString(R.string.calendar))){
                    Constants.SELECTED_TAB = Constants.CALENDAR;
                }
                else if(Objects.equals(tab.getText(), getString(R.string.summary))){
                    Constants.SELECTED_TAB = Constants.SUMMARY;
                }
                else if(Objects.equals(tab.getText(), getString(R.string.note))){
                    Constants.SELECTED_TAB = Constants.NOTES;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return binding.getRoot();
    }
}
package com.example.expensemanager.views.fragments;

import static com.example.expensemanager.views.activities.MainActivity.viewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;


import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentStatsBinding;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.RealmResults;

public class StatsFragment extends Fragment {

    public StatsFragment() {
        // Required empty public constructor
    }


    FragmentStatsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater);
        Constants.SELECTED_TAB = Constants.DAILY;
        viewModel.selectedDate.setValue(Calendar.getInstance());
        viewModel.getTransactions(Constants.INCOME);
        binding.anychart.setDrawEntryLabels(true);
        binding.anychart.setUsePercentValues(false);
        binding.anychart.setEntryLabelTextSize(20f);

        binding.toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId == R.id.incomeBtn && isChecked){
                    viewModel.getTransactions(Constants.INCOME);
                    binding.incomeBtn.setBackgroundColor(getResources().getColor(R.color.green, null));
                    binding.expenseBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
                }else if(isChecked){
                    viewModel.getTransactions(Constants.EXPENSE);
                    binding.expenseBtn.setBackgroundColor(getResources().getColor(R.color.red, null));
                    binding.incomeBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
                }
            }
        });


        binding.toggleButton.check(R.id.incomeBtn);




        viewModel.categoryTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                if(transactions.size() > 0){
                    binding.anychart.setVisibility(View.VISIBLE);
                    binding.emptyStateImg.setVisibility(View.GONE);
                    Map<String, Float> categoryMap = new HashMap<>();

                    for(Transaction transaction : transactions){
                        if(categoryMap.containsKey(transaction.getCategory())){
                            Float total = categoryMap.get(transaction.getCategory());
                            total = total + (float)transaction.getAmount();
                            categoryMap.put(transaction.getCategory(), total);
                        }else{
                            categoryMap.put(transaction.getCategory(), (float)transaction.getAmount());
                        }
                    }
                    ArrayList<PieEntry> data = new ArrayList<>();
                    for(Map.Entry<String, Float> category : categoryMap.entrySet()){
                        data.add(new PieEntry(category.getValue(), category.getKey()));
                    }
                    Log.d("dharmik", "onChanged: " + categoryMap);

                    PieDataSet pieDataSet = new PieDataSet(data, "");
                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    pieDataSet.setValueTextSize(20f);
                    binding.anychart.setData(new PieData(pieDataSet));
                    binding.anychart.invalidate();

                }
                else{
                    binding.anychart.setVisibility(View.GONE);
                    binding.emptyStateImg.setVisibility(View.VISIBLE);
                }
            }
        });



        viewModel.selectedDate.observe(getViewLifecycleOwner(), calendar -> {
            if(Constants.SELECTED_TAB == Constants.DAILY) {
                binding.currentDate.setText(Helper.formateDate(viewModel.selectedDate.getValue().getTime()));
            }
            else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                binding.currentDate.setText(Helper.formateDateByMonth(viewModel.selectedDate.getValue().getTime()));
            }
            if(binding.toggleButton.getCheckedButtonId() == R.id.incomeBtn){
                viewModel.getTransactions(Constants.INCOME);
            }else{
                viewModel.getTransactions(Constants.EXPENSE);
            }
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
                if(tab.getText() == getString(R.string.daily)){
                    Constants.SELECTED_TAB = Constants.DAILY;
                    viewModel.selectedDate.setValue(Calendar.getInstance());
                }
                else if (Objects.equals(tab.getText(), getString(R.string.monthly))) {
                    Constants.SELECTED_TAB = Constants.MONTHLY;
                    viewModel.selectedDate.setValue(Calendar.getInstance());
                    binding.currentDate.setText(Helper.formateDateByMonth(viewModel.selectedDate.getValue().getTime()));
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
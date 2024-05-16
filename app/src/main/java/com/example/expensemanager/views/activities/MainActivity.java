package com.example.expensemanager.views.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.viewmodels.MainActivityViewModel;
import com.example.expensemanager.views.fragments.AccountsFragment;
import com.example.expensemanager.views.fragments.MoreFragment;
import com.example.expensemanager.views.fragments.StatsFragment;
import com.example.expensemanager.views.fragments.TransationFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.transactionItem){
                    ft.replace(R.id.fragmentContainerView, new TransationFragment());
                    binding.toolBar.setTitle(getString(R.string.trans));
                }
                else if(item.getItemId() == R.id.statsItem){
                    ft.replace(R.id.fragmentContainerView, new StatsFragment());
                    binding.toolBar.setTitle(getString(R.string.stats));
                }
                else if(item.getItemId() == R.id.accountsItem){
                    ft.replace(R.id.fragmentContainerView, new AccountsFragment());
                    binding.toolBar.setTitle(getString(R.string.accounts));
                }
                else if(item.getItemId() == R.id.moreItem){
                    ft.replace(R.id.fragmentContainerView, new MoreFragment());
                    binding.toolBar.setTitle(getString(R.string.more));
                }
                ft.commit();
                return true;
            }
        });
    }


    /**
     * it will initilize necessary fields for given activity
     */
    private void init() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helper.setCategoryList();
        Helper.setAccountList();
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getTransactions();
    }


}
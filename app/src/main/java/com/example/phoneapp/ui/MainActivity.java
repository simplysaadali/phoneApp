package com.example.phoneapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.phoneapp.R;
import com.example.phoneapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private ContactViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // All 3 bottom nav tabs are top-level destinations (no back arrow)
        Set<Integer> topLevelDests = new HashSet<>();
        topLevelDests.add(R.id.recentCallsFragment);
        topLevelDests.add(R.id.contactListFragment);
        topLevelDests.add(R.id.dialpadFragment);

        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDests).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Wire bottom navigation to NavController with single-top + restore state
        BottomNavigationView bottomNav = binding.bottomNavigation;
        bottomNav.setOnItemSelectedListener(item -> {
            int destId = item.getItemId();
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() == destId) {
                return true; // already there
            }
            NavOptions navOptions = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(R.id.recentCallsFragment, false, true)
                    .build();
            navController.navigate(destId, null, navOptions);
            return true;
        });

        // Keep bottom nav item in sync when back-stack changes
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.recentCallsFragment || id == R.id.contactListFragment
                    || id == R.id.dialpadFragment) {
                if (bottomNav.getSelectedItemId() != id) {
                    bottomNav.setSelectedItemId(id);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public ContactViewModel getViewModel() {
        return viewModel;
    }
}

package com.example.phoneapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.phoneapp.R;
import com.example.phoneapp.data.CallLog;
import com.example.phoneapp.databinding.FragmentRecentCallsBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RecentCallsFragment extends Fragment {

    private FragmentRecentCallsBinding binding;
    private ContactViewModel viewModel;
    private CallLogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecentCallsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        setupRecyclerView();
        setupMenu();
        observeCallLogs();
    }

    private void setupRecyclerView() {
        adapter = new CallLogAdapter(new CallLogAdapter.OnCallLogClickListener() {
            @Override
            public void onCallLogClick(CallLog callLog) {
                dialPhoneNumber(callLog.getPhoneNumber());
            }

            @Override
            public void onQuickCallClick(CallLog callLog) {
                dialPhoneNumber(callLog.getPhoneNumber());
            }
        });

        binding.recyclerViewCallLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewCallLogs.setAdapter(adapter);
    }

    private void dialPhoneNumber(String number) {
        if (number == null || number.isEmpty()) return;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void setupMenu() {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_recent_calls, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_clear_history) {
                    showClearHistoryDialog();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void showClearHistoryDialog() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.clear_history)
            .setMessage("Clear all recent call history?")
            .setNegativeButton("Cancel", (d, w) -> d.dismiss())
            .setPositiveButton("Clear", (d, w) -> viewModel.clearCallLogs())
            .show();
    }

    private void observeCallLogs() {
        viewModel.getCallLogs().observe(getViewLifecycleOwner(), callLogs -> {
            if (callLogs == null || callLogs.isEmpty()) {
                binding.layoutEmptyState.setVisibility(View.VISIBLE);
                binding.recyclerViewCallLogs.setVisibility(View.GONE);
            } else {
                binding.layoutEmptyState.setVisibility(View.GONE);
                binding.recyclerViewCallLogs.setVisibility(View.VISIBLE);
                adapter.submitList(callLogs);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

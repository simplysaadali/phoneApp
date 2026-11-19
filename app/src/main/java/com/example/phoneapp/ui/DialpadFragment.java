package com.example.phoneapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.phoneapp.R;
import com.example.phoneapp.databinding.FragmentDialpadBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialpadFragment extends Fragment {

    public static final String ARG_PREFILL = "prefill_number";

    private FragmentDialpadBinding binding;
    private StringBuilder dialedNumber = new StringBuilder();

    private final ActivityResultLauncher<String> callPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
            if (granted) {
                placeCall();
            } else {
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.permission_required)
                    .setMessage(R.string.call_permission_desc)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            }
        });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDialpadBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pre-fill if argument provided
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_PREFILL)) {
            String prefill = args.getString(ARG_PREFILL, "");
            dialedNumber = new StringBuilder(prefill);
            updateDisplay();
        }

        setupDialpadButtons();
        setupActionButtons();
    }

    private void setupDialpadButtons() {
        int[][] buttonIds = {
            {R.id.btnKey1},
            {R.id.btnKey2},
            {R.id.btnKey3},
            {R.id.btnKey4},
            {R.id.btnKey5},
            {R.id.btnKey6},
            {R.id.btnKey7},
            {R.id.btnKey8},
            {R.id.btnKey9},
            {R.id.btnKeyStar},
            {R.id.btnKey0},
            {R.id.btnKeyPound}
        };
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"};

        for (int i = 0; i < buttonIds.length; i++) {
            final String digit = keys[i];
            LinearLayout btn = binding.getRoot().findViewById(buttonIds[i][0]);
            if (btn != null) {
                btn.setOnClickListener(v -> appendDigit(digit));
            }
        }
    }

    private void setupActionButtons() {
        binding.btnCall.setOnClickListener(v -> handleCallPress());
        binding.btnBackspace.setOnClickListener(v -> {
            if (dialedNumber.length() > 0) {
                dialedNumber.deleteCharAt(dialedNumber.length() - 1);
                updateDisplay();
            }
        });
        binding.btnBackspace.setOnLongClickListener(v -> {
            dialedNumber.setLength(0);
            updateDisplay();
            return true;
        });
    }

    private void appendDigit(String digit) {
        dialedNumber.append(digit);
        updateDisplay();
    }

    private void updateDisplay() {
        String number = dialedNumber.toString();
        binding.tvDialedNumber.setText(number.isEmpty() ? "" : number);
        binding.btnBackspace.setVisibility(number.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    }

    private void handleCallPress() {
        if (dialedNumber.length() == 0) return;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            placeCall();
        } else {
            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }

    private void placeCall() {
        String number = dialedNumber.toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.example.phoneapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.fragment.NavHostFragment;

import com.example.phoneapp.data.Contact;
import com.example.phoneapp.databinding.FragmentAddEditContactBinding;

public class AddEditContactFragment extends Fragment {

    private FragmentAddEditContactBinding binding;
    private ContactViewModel viewModel;
    private long contactId;
    private Contact existingContact = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEditContactBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ((MainActivity) requireActivity()).getViewModel();
        contactId = AddEditContactFragmentArgs.fromBundle(getArguments()).getContactId();

        if (contactId != 0L) {
            viewModel.loadContactById(contactId);
            viewModel.getSelectedContact().observe(getViewLifecycleOwner(), contact -> {
                if (contact != null && contact.getId() == contactId && existingContact == null) {
                    existingContact = contact;
                    populateFields(contact);
                    requireActivity().setTitle("Edit Contact");
                }
            });
        }

        binding.btnSave.setOnClickListener(v -> saveContact());
        setupMenu();
    }

    private void populateFields(Contact contact) {
        binding.etFirstName.setText(contact.getFirstName());
        binding.etLastName.setText(contact.getLastName());
        binding.etPhone.setText(contact.getPhone());
        binding.etEmail.setText(contact.getEmail());
        binding.etAddress.setText(contact.getAddress());
        binding.etNotes.setText(contact.getNotes());
    }

    private void saveContact() {
        String firstName = binding.etFirstName.getText().toString().trim();
        String lastName  = binding.etLastName.getText().toString().trim();
        String phone     = binding.etPhone.getText().toString().trim();
        String email     = binding.etEmail.getText().toString().trim();
        String address   = binding.etAddress.getText().toString().trim();
        String notes     = binding.etNotes.getText().toString().trim();

        // Validate required fields
        if (firstName.isEmpty()) {
            binding.tilFirstName.setError("First name is required");
            return;
        }
        if (phone.isEmpty()) {
            binding.tilPhone.setError("Phone number is required");
            return;
        }
        binding.tilFirstName.setError(null);
        binding.tilPhone.setError(null);

        if (existingContact != null) {
            // Update existing
            existingContact.setFirstName(firstName);
            existingContact.setLastName(lastName);
            existingContact.setPhone(phone);
            existingContact.setEmail(email);
            existingContact.setAddress(address);
            existingContact.setNotes(notes);
            viewModel.update(existingContact);
            Toast.makeText(requireContext(), "Contact updated!", Toast.LENGTH_SHORT).show();
        } else {
            // Create new
            Contact newContact = new Contact(firstName, lastName, phone, email, address, notes);
            viewModel.insert(newContact);
            Toast.makeText(requireContext(), "Contact added!", Toast.LENGTH_SHORT).show();
        }

        NavHostFragment.findNavController(this).popBackStack();
    }

    private void setupMenu() {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear(); // Hide any menu items from parent fragment
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) { return false; }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

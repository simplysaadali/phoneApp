package com.example.phoneapp.ui;

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
import androidx.navigation.fragment.NavHostFragment;

import com.example.phoneapp.R;
import com.example.phoneapp.data.Contact;
import com.example.phoneapp.databinding.FragmentContactDetailBinding;
import com.example.phoneapp.utils.AvatarUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ContactDetailFragment extends Fragment {

    private FragmentContactDetailBinding binding;
    private ContactViewModel viewModel;
    private long contactId;
    private Contact currentContact;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContactDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ((MainActivity) requireActivity()).getViewModel();

        // Retrieve contactId passed via Safe Args
        contactId = ContactDetailFragmentArgs.fromBundle(getArguments()).getContactId();

        // Load the contact
        viewModel.loadContactById(contactId);
        observeContact();

        setupMenu();
    }

    private void observeContact() {
        viewModel.getSelectedContact().observe(getViewLifecycleOwner(), contact -> {
            if (contact != null && contact.getId() == contactId) {
                currentContact = contact;
                displayContact(contact);
            }
        });
    }

    private void displayContact(Contact contact) {
        binding.tvDetailName.setText(contact.getFullName());
        binding.tvDetailInitials.setText(contact.getInitials());
        binding.viewDetailAvatarBg.setBackgroundColor(
            AvatarUtils.getAvatarColor(contact.getFullName())
        );

        binding.tvDetailPhone.setText(
            contact.getPhone().isEmpty() ? "Not provided" : contact.getPhone()
        );
        binding.tvDetailEmail.setText(
            contact.getEmail().isEmpty() ? "Not provided" : contact.getEmail()
        );
        binding.tvDetailAddress.setText(
            contact.getAddress().isEmpty() ? "Not provided" : contact.getAddress()
        );
        binding.tvDetailNotes.setText(
            contact.getNotes().isEmpty() ? "No notes" : contact.getNotes()
        );

        binding.ivDetailFavorite.setImageResource(
            contact.isFavorite()
                ? android.R.drawable.btn_star_big_on
                : android.R.drawable.btn_star_big_off
        );

        binding.ivDetailFavorite.setOnClickListener(v -> {
            viewModel.toggleFavorite(contact.getId(), !contact.isFavorite());
            viewModel.loadContactById(contactId); // Reload to reflect change
        });

        binding.fabEditContact.setOnClickListener(v -> {
            ContactDetailFragmentDirections.ActionContactDetailFragmentToAddEditContactFragment action =
                ContactDetailFragmentDirections.actionContactDetailFragmentToAddEditContactFragment(contact.getId());
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    private void setupMenu() {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_contact_detail, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete) {
                    showDeleteDialog();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void showDeleteDialog() {
        String name = currentContact != null ? currentContact.getFullName() : "this contact";
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete " + name + "?")
            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
            .setPositiveButton("Delete", (dialog, which) -> {
                if (currentContact != null) {
                    viewModel.delete(currentContact);
                    NavHostFragment.findNavController(this).popBackStack();
                }
            })
            .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

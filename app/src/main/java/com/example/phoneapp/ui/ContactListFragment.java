package com.example.phoneapp.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.data.Contact;
import com.example.phoneapp.databinding.FragmentContactListBinding;
import com.google.android.material.snackbar.Snackbar;

public class ContactListFragment extends Fragment {

    private FragmentContactListBinding binding;
    private ContactViewModel viewModel;
    private ContactAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContactListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ((MainActivity) requireActivity()).getViewModel();

        setupRecyclerView();
        setupFab();
        setupMenu();
        observeContacts();
    }

    private void setupRecyclerView() {
        adapter = new ContactAdapter(new ContactAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                ContactListFragmentDirections.ActionContactListFragmentToContactDetailFragment action =
                    ContactListFragmentDirections.actionContactListFragmentToContactDetailFragment(contact.getId());
                NavHostFragment.findNavController(ContactListFragment.this).navigate(action);
            }

            @Override
            public void onFavoriteClick(Contact contact) {
                viewModel.toggleFavorite(contact.getId(), !contact.isFavorite());
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
        setupSwipeToDelete();
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder t) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contact contact = adapter.getContactAt(viewHolder.getAdapterPosition());
                viewModel.delete(contact);
                Snackbar.make(binding.getRoot(),
                        contact.getFullName() + " deleted",
                        Snackbar.LENGTH_LONG)
                    .setAction("UNDO", v -> viewModel.insert(contact))
                    .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#F44336"));
                RectF background = new RectF(
                    itemView.getRight() + dX,
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom()
                );
                c.drawRect(background, paint);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerView);
    }

    private void setupFab() {
        binding.fabAddContact.setOnClickListener(v -> {
            ContactListFragmentDirections.ActionContactListFragmentToAddEditContactFragment action =
                ContactListFragmentDirections.actionContactListFragmentToAddEditContactFragment(0L);
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    private boolean showingFavorites = false;

    private void setupMenu() {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_contact_list, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchView.setQueryHint(getString(R.string.search_contacts));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) { return true; }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        viewModel.setSearchQuery(newText);
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_favorites) {
                    showingFavorites = !showingFavorites;
                    if (showingFavorites) {
                        viewModel.setShowFavorites(true);
                        menuItem.setIcon(android.R.drawable.btn_star_big_on);
                    } else {
                        viewModel.setShowFavorites(false);
                        menuItem.setIcon(android.R.drawable.btn_star_big_off);
                    }
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void observeContacts() {
        viewModel.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            adapter.submitList(contacts);
            binding.tvEmptyState.setVisibility(
                contacts == null || contacts.isEmpty() ? View.VISIBLE : View.GONE
            );
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

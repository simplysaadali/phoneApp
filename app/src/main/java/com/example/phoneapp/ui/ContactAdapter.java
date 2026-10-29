package com.example.phoneapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.data.Contact;
import com.example.phoneapp.utils.AvatarUtils;

public class ContactAdapter extends ListAdapter<Contact, ContactAdapter.ContactViewHolder> {

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
        void onFavoriteClick(Contact contact);
    }

    private final OnContactClickListener listener;

    public ContactAdapter(OnContactClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Contact> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Contact>() {
            @Override
            public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                return oldItem.getId() == newItem.getId()
                    && oldItem.getFirstName().equals(newItem.getFirstName())
                    && oldItem.getLastName().equals(newItem.getLastName())
                    && oldItem.getPhone().equals(newItem.getPhone())
                    && oldItem.isFavorite() == newItem.isFavorite();
            }
        };

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public Contact getContactAt(int position) {
        return getItem(position);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private final View avatarBg;
        private final TextView tvInitials;
        private final TextView tvName;
        private final TextView tvPhone;
        private final ImageView ivFavorite;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarBg  = itemView.findViewById(R.id.viewAvatarBg);
            tvInitials = itemView.findViewById(R.id.tvContactInitials);
            tvName    = itemView.findViewById(R.id.tvContactName);
            tvPhone   = itemView.findViewById(R.id.tvContactPhone);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }

        void bind(Contact contact) {
            tvName.setText(contact.getFullName());
            tvPhone.setText(contact.getPhone());
            tvInitials.setText(contact.getInitials());
            avatarBg.setBackgroundColor(AvatarUtils.getAvatarColor(contact.getFullName()));

            ivFavorite.setImageResource(
                contact.isFavorite()
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star_big_off
            );

            itemView.setOnClickListener(v -> listener.onContactClick(contact));
            ivFavorite.setOnClickListener(v -> listener.onFavoriteClick(contact));
        }
    }
}

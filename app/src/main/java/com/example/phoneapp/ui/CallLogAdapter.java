package com.example.phoneapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.R;
import com.example.phoneapp.data.CallLog;
import com.example.phoneapp.utils.AvatarUtils;

public class CallLogAdapter extends ListAdapter<CallLog, CallLogAdapter.CallLogViewHolder> {

    public interface OnCallLogClickListener {
        void onCallLogClick(CallLog callLog);
        void onQuickCallClick(CallLog callLog);
    }

    private final OnCallLogClickListener listener;

    public CallLogAdapter(OnCallLogClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CallLog> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<CallLog>() {
            @Override
            public boolean areItemsTheSame(@NonNull CallLog oldItem, @NonNull CallLog newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CallLog oldItem, @NonNull CallLog newItem) {
                return oldItem.getId() == newItem.getId()
                    && oldItem.getTimestamp() == newItem.getTimestamp();
            }
        };

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_log, parent, false);
        return new CallLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public CallLog getCallLogAt(int position) {
        return getItem(position);
    }

    class CallLogViewHolder extends RecyclerView.ViewHolder {

        private final View avatarBg;
        private final TextView tvInitials;
        private final TextView tvCallerName;
        private final ImageView ivCallType;
        private final TextView tvCallType;
        private final TextView tvTime;
        private final TextView tvDuration;
        private final ImageButton ivQuickCall;

        CallLogViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarBg    = itemView.findViewById(R.id.viewAvatarBg);
            tvInitials  = itemView.findViewById(R.id.tvInitials);
            tvCallerName = itemView.findViewById(R.id.tvCallerName);
            ivCallType  = itemView.findViewById(R.id.ivCallType);
            tvCallType  = itemView.findViewById(R.id.tvCallType);
            tvTime      = itemView.findViewById(R.id.tvCallTime);
            tvDuration  = itemView.findViewById(R.id.tvCallDuration);
            ivQuickCall = itemView.findViewById(R.id.btnQuickCall);
        }

        void bind(CallLog log) {
            tvCallerName.setText(log.getDisplayName());
            tvInitials.setText(log.getInitials());
            avatarBg.setBackgroundColor(AvatarUtils.getAvatarColor(log.getDisplayName()));
            tvTime.setText(log.getRelativeTime());

            String durStr = log.getFormattedDuration();
            if (durStr.isEmpty()) {
                tvDuration.setVisibility(View.GONE);
            } else {
                tvDuration.setVisibility(View.VISIBLE);
                tvDuration.setText(durStr);
            }

            // Call type icon + label
            switch (log.getCallTypeEnum()) {
                case INCOMING:
                    ivCallType.setImageResource(R.drawable.ic_call_incoming);
                    tvCallType.setText(R.string.incoming);
                    tvCallType.setTextColor(itemView.getContext().getColor(R.color.call_green));
                    break;
                case OUTGOING:
                    ivCallType.setImageResource(R.drawable.ic_call_outgoing);
                    tvCallType.setText(R.string.outgoing);
                    tvCallType.setTextColor(itemView.getContext().getColor(R.color.call_blue));
                    break;
                case MISSED:
                    ivCallType.setImageResource(R.drawable.ic_call_missed);
                    tvCallType.setText(R.string.missed);
                    tvCallType.setTextColor(itemView.getContext().getColor(R.color.call_red));
                    break;
            }

            itemView.setOnClickListener(v -> listener.onCallLogClick(log));
            ivQuickCall.setOnClickListener(v -> listener.onQuickCallClick(log));
        }
    }
}

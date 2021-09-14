package com.djay.locatesecurly.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djay.locatesecurly.R;
import com.djay.locatesecurly.models.Session;
import com.djay.locatesecurly.utils.CommonUtils;
import com.djay.locatesecurly.utils.listeners.RecyclerViewItemClickListener;

import java.util.ArrayList;

/**
 * Adapter class for sessions list extends {@link RecyclerView.Adapter}
 *
 * @author Dhananjay Kumar
 */
public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.MyViewHolder> {
    private ArrayList<Session> sessions;
    private RecyclerViewItemClickListener clickListener;

    public SessionListAdapter(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.tvStart.setText(CommonUtils.formattedDate((long) session.getStartTimeStamp()));
        holder.tvEnd.setText(CommonUtils.formattedDate((long) session.getEndTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public void setClickListener(RecyclerViewItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public Session getItem(int position) {
        return sessions.get(position);
    }

    /**
     * View holder class for recycler view row extends {@link RecyclerView.ViewHolder} and
     * implements {@link android.view.View.OnClickListener}
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvStart, tvEnd;

        private MyViewHolder(View view) {
            super(view);
            tvStart = (TextView) view.findViewById(R.id.tv_start);
            tvEnd = (TextView) view.findViewById(R.id.tv_end);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

}
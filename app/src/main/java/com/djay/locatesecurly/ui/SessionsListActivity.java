package com.djay.locatesecurly.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.djay.locatesecurly.R;
import com.djay.locatesecurly.models.Session;
import com.djay.locatesecurly.ui.adapters.SessionListAdapter;
import com.djay.locatesecurly.utils.listeners.RecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Activity class for showing captured sessions list extends {@link BaseActivity}
 *
 * @author Dhananjay Kumar
 */
public class SessionsListActivity extends BaseActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.rv_session)
    RecyclerView rvSession;

    @BindView(R.id.tv_no_session)
    TextView tvNoSession;

    private SessionListAdapter sessionListAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SessionsListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_list);
        initUi();
    }

    /**
     * initializes UI
     */
    private void initUi() {
        ButterKnife.bind(this);
        setActivityUpEnabled();
        ArrayList<Session> sessions = dataManager.getAllSessions();
        if (sessions != null && sessions.size() > 0) {
            sessionListAdapter = new SessionListAdapter(sessions);
            rvSession.setLayoutManager(new LinearLayoutManager(this));
            rvSession.setAdapter(sessionListAdapter);
            sessionListAdapter.setClickListener(this);
            rvSession.setVisibility(View.VISIBLE);
            tvNoSession.setVisibility(View.GONE);
        } else {
            rvSession.setVisibility(View.GONE);
            tvNoSession.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view, int position) {
        MapActivity.start(this, sessionListAdapter.getItem(position));
    }

}

package com.djay.locatesecurly.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.djay.locatesecurly.R;
import com.djay.locatesecurly.ui.adapters.AudioListAdapter;
import com.djay.locatesecurly.utils.StorageUtil;
import com.djay.locatesecurly.utils.listeners.RecyclerViewItemClickListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Activity class for showing captured sessions list extends {@link BaseActivity}
 *
 * @author Dhananjay Kumar
 */
public class AudioListActivity extends BaseActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.rv_audio)
    RecyclerView rvAudio;

    @BindView(R.id.tv_no_session)
    TextView tvNoSession;

    private AudioListAdapter audioListAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AudioListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        initUi();
    }

    /**
     * initializes UI
     */
    private void initUi() {
        ButterKnife.bind(this);
        setActivityUpEnabled();
        audioListAdapter = new AudioListAdapter();
        rvAudio.setLayoutManager(new LinearLayoutManager(this));
        rvAudio.setAdapter(audioListAdapter);
        audioListAdapter.setClickListener(this);
        ArrayList<File> files = audioListAdapter.getAllItems();
        if (files != null && files.size() > 0) {
            rvAudio.setVisibility(View.VISIBLE);
            tvNoSession.setVisibility(View.GONE);
        } else {
            rvAudio.setVisibility(View.GONE);
            tvNoSession.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view, int position) {
        openFile(audioListAdapter.getItem(position));
    }

    private void openFile(File file) {
        Uri uri = StorageUtil.uriFromFile(this, file);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "audio/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

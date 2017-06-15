package com.djay.locatesecurly.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djay.locatesecurly.R;
import com.djay.locatesecurly.utils.CommonUtils;
import com.djay.locatesecurly.utils.StorageUtil;
import com.djay.locatesecurly.utils.listeners.RecyclerViewItemClickListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter class for audio list extends {@link RecyclerView.Adapter}
 *
 * @author Dhananjay Kumar
 */
public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.MyViewHolder> {
    private ArrayList<File> files;
    private RecyclerViewItemClickListener clickListener;

    public AudioListAdapter() {
        this.files = getListFiles(StorageUtil.getAppExternalDataDirectoryFile());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_audio_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        File file = files.get(position);
        holder.tvStart.setText(CommonUtils.formattedDate(file.lastModified()));
        holder.tvName.setText(file.getName());
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void setClickListener(RecyclerViewItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public File getItem(int position) {
        return files.get(position);
    }

    public ArrayList<File> getAllItems() {
        return files;
    }

    private ArrayList<File> getListFiles(File parentDir) {
        File[] files = parentDir.listFiles();
        ArrayList<File> inFiles = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    /**
     * View holder class for recycler view row extends {@link RecyclerView.ViewHolder} and
     * implements {@link View.OnClickListener}
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvStart, tvName;

        private MyViewHolder(View view) {
            super(view);
            tvStart = (TextView) view.findViewById(R.id.tv_start);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

}
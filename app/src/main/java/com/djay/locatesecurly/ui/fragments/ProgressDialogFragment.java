package com.djay.locatesecurly.ui.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.view.KeyEvent;

import com.djay.locatesecurly.R;


/**
 * Fragment class for showing progress bar whenever required in application extends {@link DialogFragment}
 *
 * @author Dhananjay Kumar
 */
public class ProgressDialogFragment extends DialogFragment {

    private static final String TAG = ProgressDialogFragment.class.getSimpleName();
    private static final String ARG_MESSAGE_ID = "message_id";

    /**
     * Show progress dialog
     * @param fm FragmentManager
     */
    public static void show(FragmentManager fm) {
        // We're not using show() because we may call this DialogFragment
        // in onActivityResult() method and there will be a state loss exception
        if (fm.findFragmentByTag(TAG) == null) {
            fm.beginTransaction().add(newInstance(), TAG).commitAllowingStateLoss();
        }
    }

    /**
     * Hide progress dialog
     * @param fm FragmentManager
     */
    public static void hide(FragmentManager fm) {
        DialogFragment fragment = (DialogFragment) fm.findFragmentByTag(TAG);
        if (fragment != null) {
            fragment.dismissAllowingStateLoss();
        }
    }

    public static ProgressDialogFragment newInstance() {
        return newInstance(R.string.dlg_wait_please);
    }

    public static ProgressDialogFragment newInstance(int messageId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MESSAGE_ID, messageId);

        ProgressDialogFragment dialog = new ProgressDialogFragment();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(getArguments().getInt(ARG_MESSAGE_ID)));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Disable the back button
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        };
        dialog.setOnKeyListener(keyListener);

        return dialog;
    }
}
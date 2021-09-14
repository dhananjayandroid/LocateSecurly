package com.djay.locatesecurly.utils;

import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import android.widget.Toast;

import com.djay.locatesecurly.App;

/**
 * Utility class for showing toasts in application
 *
 * @author Dhananjay Kumar
 */
@SuppressWarnings("unused")
public class ToastUtils {

    public static void shortToast(@StringRes int text) {
        shortToast(App.getInstance().getString(text));
    }

    public static void shortToast(String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    static void longToast(@StringRes int text) {
        longToast(App.getInstance().getString(text));
    }

    static void longToast(String text) {
        show(text, Toast.LENGTH_LONG);
    }

    private static Toast makeToast(String text, @ToastLength int length) {
        return Toast.makeText(App.getInstance(), text, length);
    }

    private static void show(String text, @ToastLength int length) {
        makeToast(text, length).show();
    }

    @IntDef({Toast.LENGTH_LONG, Toast.LENGTH_SHORT})
    private @interface ToastLength {

    }
}
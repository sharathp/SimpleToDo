package com.codepath.simpletodo.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * {@link Spinner}'s subclass that notifies the spinner's pop-up is open/close.
 */
public class PopupAwareSpinner extends Spinner {
    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;


    public PopupAwareSpinner(final Context context) {
        super(context);
    }

    public PopupAwareSpinner(final Context context, final int mode) {
        super(context, mode);
    }

    public PopupAwareSpinner(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupAwareSpinner(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PopupAwareSpinner(final Context context, final AttributeSet attrs, final int defStyleAttr, final int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PopupAwareSpinner(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes, final int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public PopupAwareSpinner(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes, final int mode, final Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
    }

    @Override
    public boolean performClick() {
        // register that the Spinner was opened so we have a status
        // indicator for the activity(which may lose focus for some other
        // reasons)
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened();
        }
        return super.performClick();
    }

    /**
     * Set the spinner event listener
     *
     * @param onSpinnerEventsListener - on spinner event listener
     */
    public void setSpinnerEventsListener(final OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    /**
     * Propagate the closed Spinner event to the listener from outside.
     */
    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed();
        }
    }

    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }

    @Override
    public void onWindowFocusChanged(final boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasBeenOpened() && hasWindowFocus) {
            performClosedEvent();
        }
    }

    /**
     * Callback that is notified of Spinner pop-up open & close.
     */
    public interface OnSpinnerEventsListener {
        /**
         * Notify that spinner is opened.
         */
        void onSpinnerOpened();

        /**
         * Notify that spinner is closed.
         */
        void onSpinnerClosed();
    }
}

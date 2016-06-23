package com.codepath.simpletodo.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.Priority;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PriorityPickerFragment extends DialogFragment {
    public static final String ARG_SELECTED_PRIORITY = "PriorityPickerFragment.PRIORITY";

    @BindView(R.id.tv_item_priority_label)
    TextView mPriorityLabelTextView;

    @BindView(R.id.sb_item_priority)
    SeekBar mPrioritySeekBar;

    private int mInitialPriority;

    private OnPrioritySetListener mOnPrioritySetListener;

    public static PriorityPickerFragment createInstance(final int initialPriority) {
        final Bundle args = new Bundle();
        args.putInt(ARG_SELECTED_PRIORITY, initialPriority);

        final PriorityPickerFragment fragment = new PriorityPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (! (activity instanceof OnPrioritySetListener)) {
            throw new ClassCastException("Activity must implement " + OnPrioritySetListener.class.getName());
        }
        mOnPrioritySetListener = (OnPrioritySetListener) activity;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInitialPriority = getArguments().getInt(ARG_SELECTED_PRIORITY, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final View view = View.inflate(getContext(), R.layout.dialog_priority, null);
        ButterKnife.bind(this, view);

        builder.setView(view)
                .setPositiveButton(R.string.alert_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        mOnPrioritySetListener.onPrioritySet(mPrioritySeekBar.getProgress());
                    }
                })
                .setNegativeButton(R.string.alert_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // no-op
                    }
                });

        initViews();

        return builder.create();
    }

    private void initViews() {
        mPrioritySeekBar.setProgress(mInitialPriority);
        mPrioritySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                setPrioritySeekBarData(seekBar, progress);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
                // no-op
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                // no-op
            }
        });
        setPrioritySeekBarData(mPrioritySeekBar, mInitialPriority);
    }

    private void setPrioritySeekBarData(final SeekBar seekBar, final int progress) {
        // default low
        int colorResId = Priority.LOW.getColorResourceId();
        final Priority priority = Priority.getPriorityByOrder(progress);
        if (priority != null) {
            colorResId = priority.getColorResourceId();
        }

        final int color = getResources().getColor(colorResId);
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mPriorityLabelTextView.setText(priority.name());
        ((GradientDrawable) mPriorityLabelTextView.getBackground()).setColor(getResources().getColor(priority.getColorResourceId()));
    }

    /**
     * The callback used to indicate the user is done selecting priority.
     */
    public interface OnPrioritySetListener {
        /**
         * @param priority - selected priority
         */
        void onPrioritySet(final int priority);
    }
}
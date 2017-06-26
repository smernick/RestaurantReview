package com.nucleuslife.restaurantreview.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.views.SpinnerView;

public class LoadingDialog extends DialogFragment
{
    public static final String TAG = LoadingDialog.class.getSimpleName();

    private SpinnerView spinningView;
    private Handler timeoutHandler;
    private int loadingDialogTimeout = 5000;

    private Runnable timeoutRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            timeoutHandler.removeCallbacksAndMessages(null);
            LoadingDialog.this.dismiss();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(true);

        this.timeoutHandler = new Handler();

        return dialog;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        if (this.loadingDialogTimeout > 0) {
            this.timeoutHandler.postDelayed(this.timeoutRunnable, this.loadingDialogTimeout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.loading_dialog, container, false);

        this.spinningView = (SpinnerView) view.findViewById(R.id.loading_dialog_spinner);

        this.spinningView.setSpinningView(R.mipmap.ic_launcher);


        return view;
    }

    @Override
    public void onResume()
    {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();

        this.spinningView.startAnimation();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        if (dialog != null) {
            super.onDismiss(dialog);
        }
    }
}

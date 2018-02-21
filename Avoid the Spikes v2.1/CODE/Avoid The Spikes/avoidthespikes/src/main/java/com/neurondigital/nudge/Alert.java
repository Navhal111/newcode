package com.neurondigital.nudge;

import com.neurondigital.avoidthespikes.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class Alert extends DialogFragment {
	Context mContext;
	String title, hint, text;
	Boolean exit;
	Activity activity;
	PositiveButtonListener PositiveButtonListener;
	NegativeButtonListener NegativeButtonListener;
	int type;
	final int TEXT = 0, EDITTEXT = 1;

	public void DisplayText(String title, String text, Activity activity) {
		this.title = title;
		this.text = text;
		this.activity = activity;
		type = TEXT;
	}

	public void DisplayEditText(String title, String text, String hint, Activity activity) {
		this.title = title;
		this.activity = activity;
		this.text = text;
		this.hint = hint;
		type = EDITTEXT;
	}

	//positive button interface
	public interface PositiveButtonListener {
		public void onPositiveButton(String input);
	}

	public synchronized void setPositiveButtonListener(PositiveButtonListener PositiveButtonListener) {
		this.PositiveButtonListener = PositiveButtonListener;
	}

	//negative button interface
	public interface NegativeButtonListener {
		public void onNegativeButton(String input);
	}

	public synchronized void setNegativeButtonListener(NegativeButtonListener NegativeButtonListener) {
		this.NegativeButtonListener = NegativeButtonListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(text);

		if (type == EDITTEXT) {
			final EditText edittext = new EditText(activity);
			edittext.setHint(hint);
			alertDialogBuilder.setView(edittext);

			alertDialogBuilder.setPositiveButton(activity.getString(R.string.Alert_accept), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (PositiveButtonListener != null)
						PositiveButtonListener.onPositiveButton(edittext.getText().toString());
				}
			});
			alertDialogBuilder.setNegativeButton(activity.getString(R.string.Alert_cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (NegativeButtonListener != null)
						NegativeButtonListener.onNegativeButton(edittext.getText().toString());
				}
			});
		}
		if (type == TEXT) {

			alertDialogBuilder.setPositiveButton(activity.getString(R.string.Alert_accept), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (PositiveButtonListener != null)
						PositiveButtonListener.onPositiveButton("");

				}
			});
			alertDialogBuilder.setNegativeButton(activity.getString(R.string.Alert_cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (NegativeButtonListener != null)
						NegativeButtonListener.onNegativeButton("");

				}
			});
		}

		return alertDialogBuilder.create();
	}
}
package com.shj00007.activity.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public class BaseActivity extends Activity {

	private ProgressDialog m_ProgressDialog;

	protected void showProgressDialog(String p_TitleResID, String p_MessageResID) {
		m_ProgressDialog = new ProgressDialog(this);
		m_ProgressDialog.setTitle(p_TitleResID);
		m_ProgressDialog.setMessage(p_MessageResID);
		m_ProgressDialog.show();
	}

	protected void dismissProgressDialog() {
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
		}
	}

	protected void showToast(String pText) {
		Toast.makeText(this, pText, Toast.LENGTH_SHORT).show();
	}

}

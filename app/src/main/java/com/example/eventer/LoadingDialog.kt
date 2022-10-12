package com.example.eventer

import android.app.AlertDialog
import android.os.Handler
import androidx.fragment.app.FragmentActivity

class LoadingDialog(val mActivity: FragmentActivity?) {
    private lateinit var isDialog: AlertDialog

    fun startLoading() {
        val inflater = mActivity?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.loading_item, null)
        val builder = AlertDialog.Builder(mActivity,R.style.AlertDialogCustom)

        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialog = builder.create()
        isDialog.show()
    }

    fun isDismiss() {
        Handler().postDelayed({isDialog.dismiss()}, 500)
    }
}
package com.grandtech.map.utils.commons;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by zy on 2016/11/14.
 */

public class DialogHelper {

    public static void dialog(AlertDialog.Builder builder,String positiveName,String negativeName,final ICallBack iCallBack) {

        builder.setPositiveButton(positiveName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(iCallBack!=null) {
                    iCallBack.dialogOk();
                }
            }
        });
        builder.setNegativeButton(negativeName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(iCallBack!=null) {
                    iCallBack.dialogCancel();
                }
            }
        });
        builder.create().show();
    }

    public interface ICallBack{
        public void dialogOk();
        public void dialogCancel();
    }

}

package com.grandtech.map.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grandtech.map.R;
import com.grandtech.map.entity.Account;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.utils.commons.DateUtil;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;
import com.grandtech.map.utils.enmus.EnumLoginState;
import com.grandtech.map.utils.enmus.EnumUserAccountState;
import com.grandtech.map.view.other.ClearEditText;

import java.util.Date;

/**
 * Created by zy on 2016/11/20.
 * 账号管理Activity
 */

public class AccountActivity extends Activity implements View.OnClickListener {

    //public final static String SP_ACCOUNT="account";
    private ClearEditText cetPhoneNum;
    private ClearEditText cetCollectName;
    private ClearEditText cetDateTime;
    private ClearEditText ctvAccountState;
    private ClearEditText cetLoginState;
    private Button btnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialize();
        registerEvent();
        initData();
    }


    //寻找控件
    private void initialize() {
        cetPhoneNum = (ClearEditText) findViewById(R.id.cetPhoneNum);
        cetCollectName = (ClearEditText) findViewById(R.id.cetCollectName);
        cetDateTime = (ClearEditText) findViewById(R.id.cetDateTime);
        ctvAccountState = (ClearEditText) findViewById(R.id.ctvAccountState);
        cetLoginState = (ClearEditText) findViewById(R.id.cetLoginState);
        btnSure = (Button) findViewById(R.id.btnSure);
    }

    //注册事件
    private void registerEvent() {
        btnSure.setOnClickListener(this);
    }

    private void initData() {
        MobileUser mobileUser = (MobileUser) SharedPreferencesHelper.readObject(this, LoginActivity.USER_ACCOUNT);
        if (mobileUser != null) {
            cetPhoneNum.setText(mobileUser.getPhoneNum().toString());
            cetCollectName.setText(mobileUser.getName().toString());
            cetDateTime.setText(DateUtil.dateTimeToStr(new Date()));
            ctvAccountState.setText(EnumUserAccountState.getEnumByCode(mobileUser.getState()).getDesc());
            cetLoginState.setText(EnumLoginState.getEnumByCode(mobileUser.getIsOnLine()).getDesc());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSure:
                // saveAccount2Xml();
                break;
        }
    }

    /*private void saveAccount2Xml() {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setCollectName(cetCollectName.getText().toString());
        SharedPreferencesHelper.deleteObject(this, LoginActivity.USER_ACCOUNT);
        boolean flag = SharedPreferencesHelper.saveOrUpdateObject(this, LoginActivity.USER_ACCOUNT, account);
        if (flag) {
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        }
    }*/
}

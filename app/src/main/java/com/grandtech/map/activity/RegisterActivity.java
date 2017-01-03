package com.grandtech.map.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grandtech.map.R;
import com.grandtech.map.dao.MobileUserDao;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.utils.commons.AnimationHelper;
import com.grandtech.map.utils.commons.MD5AndDESUtil;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.commons.RegexHelper;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;
import com.grandtech.map.utils.enmus.EnumLoginState;
import com.grandtech.map.utils.enmus.EnumUserAccountState;
import com.grandtech.map.utils.enmus.EnumUserRegisterCallBack;
import com.grandtech.map.utils.okhttp.OkHttpUtils;
import com.grandtech.map.utils.okhttp.callback.Callback;
import com.grandtech.map.view.other.ClearEditText;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Response;

//注册
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private ClearEditText cetPhoneNum;
    private ClearEditText cetLoginName;
    private ClearEditText cetPwd;
    private ClearEditText cetPwdAgain;
    private Button btnRegister;
    private Button btnReset;
    private TextView tvRegisterInfo;
    private String phoneNum;
    private String loginName;
    private String pwd;
    private String pwdAgain;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        registerEvent();
        initClass();
    }

    @Override
    protected void initialize() {
        cetPhoneNum = (ClearEditText) findViewById(R.id.cetPhoneNum);
        cetLoginName = (ClearEditText) findViewById(R.id.cetLoginName);
        cetPwd = (ClearEditText) findViewById(R.id.cetPwd);
        cetPwdAgain = (ClearEditText) findViewById(R.id.cetPwdAgain);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnReset = (Button) findViewById(R.id.btnReset);
        tvRegisterInfo = (TextView) findViewById(R.id.tvRegisterInfo);
    }

    @Override
    protected void registerEvent() {
        btnRegister.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }

    @Override
    protected void initClass() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                try {
                    if (isNetWork()) {
                        if (validate()) {
                            return;
                        }
                        register();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }

    private boolean validate() {
        boolean flag = false;
        phoneNum = cetPhoneNum.getText().toString();
        loginName = cetLoginName.getText().toString();
        pwd = cetPwd.getText().toString();
        pwdAgain = cetPwdAgain.getText().toString();
        if (!RegexHelper.checkMobile(phoneNum)) {
            AnimationHelper.shakeAnimation(cetPhoneNum);
            flag = true;
        }
        if (loginName.equals("")) {
            AnimationHelper.shakeAnimation(cetLoginName);
            flag = true;
        }
        if (pwd.equals("")) {
            AnimationHelper.shakeAnimation(cetPwd);
            flag = true;
        }
        if (!pwd.equals(pwdAgain) || pwdAgain.equals("")) {
            AnimationHelper.shakeAnimation(cetPwdAgain);
            flag = true;
        }
        return flag;
    }


    public void register() throws Exception {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("注册中...").show();
        Map<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNum);//手机号
        params.put("loginName", loginName);//手机号
        params.put("pwd", pwd);//密码
        OkHttpUtils.post().url(PathConfig.MOBILE_SERVICE_USER_REGISTER).params(params).build().execute(new Callback<Object>() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {//这是异步线程
                String string = response.body().string();
                return string;//返回值到ui线程
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                sweetAlertDialog.dismiss();
                tvRegisterInfo.setVisibility(View.VISIBLE);
                tvRegisterInfo.setTextColor(Color.RED);
                tvRegisterInfo.setText(e.toString());
            }

            @Override
            public void onResponse(Object response, int id) { //这里是ui线程
                sweetAlertDialog.dismiss();
                Integer res = new Integer(response.toString());
                tvRegisterInfo.setText(EnumUserRegisterCallBack.getEnumByCode(res).getDesc());
                tvRegisterInfo.setVisibility(View.VISIBLE);
                if (res == EnumUserRegisterCallBack.SUCCESS.getValue()) {
                    saveUser2Location();
                    tvRegisterInfo.setTextColor(Color.GREEN);
                    Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                } else if (res == EnumUserRegisterCallBack.ISEXIST.getValue()) {
                    tvRegisterInfo.setTextColor(Color.RED);
                } else if (res == EnumUserRegisterCallBack.SURVER_ERROR.getValue()) {
                    tvRegisterInfo.setTextColor(Color.RED);
                }
            }
        });
    }

    private void reset() {
        cetPhoneNum.setText("");
        cetLoginName.setText("");
        cetPwd.setText("");
        cetPwdAgain.setText("");
        tvRegisterInfo.setVisibility(View.GONE);
    }

    /**
     * 账号保存到本地
     */
    private void saveUser2Location() {
        MobileUserDao mobileUserDao = new MobileUserDao(RegisterActivity.this);
        MobileUser mobileUser = new MobileUser();
        mobileUser.setPhoneNum(phoneNum);
        mobileUser.setName(loginName);
        mobileUser.setPwd(MD5AndDESUtil.md5Encrypt(pwd));//MD5加密
        mobileUser.setDateTime(new Date());
        mobileUser.setState(EnumUserAccountState.NORMAL.getValue());
        mobileUserDao.save(mobileUser);
        mobileUser.setPwd(pwd);//密码设置成明文
        mobileUser.setIsOnLine(EnumLoginState.ONLINE.getValue());
        SharedPreferencesHelper.saveOrUpdateObject(this, LoginActivity.USER_ACCOUNT, mobileUser);//保存用户信息
    }
}

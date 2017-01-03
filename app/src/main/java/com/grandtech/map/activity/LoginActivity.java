package com.grandtech.map.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandtech.map.R;
import com.grandtech.map.dao.MobileUserDao;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.utils.commons.AnimationHelper;
import com.grandtech.map.utils.commons.NetHelper;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.commons.RegexHelper;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;
import com.grandtech.map.utils.enmus.EnumLoginState;
import com.grandtech.map.utils.enmus.EnumUserAccountState;
import com.grandtech.map.utils.okhttp.OkHttpUtils;
import com.grandtech.map.utils.okhttp.callback.Callback;
import com.grandtech.map.view.other.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Response;

/**
 * SharedPreferencesHelper保存的密码是明文 有待改进
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String IS_REMEMBER = "isRemember";
    public static final String USER_ACCOUNT = "userAccount";

    private Button btnReset;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvLoginInfo;
    private ClearEditText cetPhoneNum;
    private ClearEditText cetPwd;
    private String phoneNum;
    private String pwd;
    private String SP_Pwd;//当前记住的密码（密文）
    private MobileUser mobileUser;
    private CheckBox cbRemember;
    private Boolean isRemember;
    private SweetAlertDialog sweetAlertDialog;
    private MobileUserDao mobileUserDao;
    private Integer isOnLogin = EnumLoginState.OFF.getValue();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        registerEvent();
        initClass();
    }

    @Override
    protected void initialize() {
        cetPhoneNum = (ClearEditText) findViewById(R.id.cetPhoneNum);
        cetPwd = (ClearEditText) findViewById(R.id.cetPwd);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvLoginInfo = (TextView) findViewById(R.id.tvLoginInfo);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnReset = (Button) findViewById(R.id.btnReset);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        isRemember = (Boolean) SharedPreferencesHelper.readObject(this, IS_REMEMBER);
    }

    @Override
    protected void registerEvent() {
        btnLogin.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        cbRemember.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initClass() {
        mobileUserDao = new MobileUserDao(this);

        if (isRemember != null) {
            cbRemember.setChecked(isRemember);
        } else {
            isRemember = cbRemember.isChecked();
        }
        mobileUser = (MobileUser) SharedPreferencesHelper.readObject(this, USER_ACCOUNT);
        if (mobileUser != null && isRemember) {
            SP_Pwd = mobileUser.getPwd();
            cetPhoneNum.setText(mobileUser.getPhoneNum());
            cetPwd.setText(SP_Pwd);
        }
        if (mobileUser == null) {
            mobileUser = new MobileUser();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tvRegister:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                if (validate()) {
                    return;
                }
                if (!NetHelper.hasInternet(this)) {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("是否离线登录?")
                            .setContentText("当前设备未连接网络!")
                            .setCancelText("取消")
                            .setConfirmText("离线登录")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    outLineLogin();//离线登录
                                }
                            }).show();
                } else {
                    sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.setTitleText("登录中...").show();
                    onLineLogin();//在线登录
                }
                break;
            case R.id.btnReset:
                reset();
                break;

        }
    }

    @Override
    public void finish() {
        super.finish();
        SharedPreferencesHelper.saveOrUpdateObject(this, IS_REMEMBER, isRemember);
        phoneNum = cetPhoneNum.getText().toString();
        pwd = cetPwd.getText().toString();
        MobileUser mobileUserSP = new MobileUser();
        mobileUserSP.setPhoneNum(phoneNum);
        mobileUserSP.setPwd(pwd);//存入临时文件
        mobileUserSP.setIsOnLine(isOnLogin);
        mobileUserSP.setName(mobileUser != null ? mobileUser.getName() : "");
        mobileUserSP.setDateTime(mobileUser != null? mobileUser.getDateTime(): null);
        mobileUserSP.setState(mobileUser!=null?mobileUser.getState(): EnumUserAccountState.UNUSUAL.getValue());
        SharedPreferencesHelper.saveOrUpdateObject(this, USER_ACCOUNT, mobileUserSP);//保存用户信息
    }


    private boolean validate() {
        boolean flag = false;
        phoneNum = cetPhoneNum.getText().toString();
        pwd = cetPwd.getText().toString();
        if (!RegexHelper.checkMobile(phoneNum)) {
            AnimationHelper.shakeAnimation(cetPhoneNum);
            flag = true;
        }
        if (pwd.equals("")) {
            AnimationHelper.shakeAnimation(cetPwd);
            flag = true;
        }
        return flag;
    }

    /**
     * 离线登录
     */
    private void outLineLogin() {
        if(mobileUser==null){
            mobileUser = new MobileUser();
        }
        mobileUser.setPhoneNum(phoneNum);
        mobileUser.setPwd(pwd);
        mobileUser = mobileUserDao.getByPhoneAndPwd(mobileUser);
        if (mobileUser!=null) {
            isOnLogin = EnumLoginState.OUTLINE.getValue();
            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
            startActivity(intent);
            finish();
        } else {
            tvLoginInfo.setVisibility(View.VISIBLE);
            tvLoginInfo.setText("账号或密码出错！");
            tvLoginInfo.setTextColor(Color.RED);
        }
    }

    /**
     * 在线登录
     */
    private void onLineLogin() {
        Map<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNum);//手机号
        params.put("pwd", pwd);//密码
        OkHttpUtils.post().url(PathConfig.MOBILE_SERVICE_USER_LOGIN).params(params).build().execute(new Callback<Object>() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {//这是异步线程
                String string = response.body().string();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                mobileUser = gson.fromJson(string, MobileUser.class);//转json
                return mobileUser;//返回值到ui线程
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                sweetAlertDialog.dismiss();
                tvLoginInfo.setVisibility(View.VISIBLE);
                tvLoginInfo.setText(e.toString());
                tvLoginInfo.setTextColor(Color.RED);
            }

            @Override
            public void onResponse(Object response, int id) { //这里是ui线程
                sweetAlertDialog.dismiss();
                if (response != null) {
                    isOnLogin = EnumLoginState.ONLINE.getValue();
                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    startActivity(intent);
                    save2Location((MobileUser) response);
                    finish();
                } else {
                    tvLoginInfo.setVisibility(View.VISIBLE);
                    tvLoginInfo.setText("账号或密码出错！");
                    tvLoginInfo.setTextColor(Color.RED);
                }
            }
        });
    }

    /**
     * 数据保存到本地
     *
     * @param mobileUser
     */
    private void save2Location(MobileUser mobileUser) {
        mobileUserDao.saveOrUpdate(mobileUser);
    }

    /**
     * 重置
     */
    private void reset() {
        cetPhoneNum.setText("");
        cetPwd.setText("");
        tvLoginInfo.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        isRemember = b;
    }
}

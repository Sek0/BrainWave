package com.example.tanxiaokao.brainwave;

import android.content.Intent;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import shem.com.materiallogin.DefaultLoginView;
import shem.com.materiallogin.DefaultRegisterView;
import shem.com.materiallogin.MaterialLoginView;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout loginPass1;
    private TextInputLayout loginUser1;

    private TextInputLayout registerUser1;
    private TextInputLayout registerPass1;
    private TextInputLayout registerPassRep1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityManage.addActivity(this);

        final MaterialLoginView login = (MaterialLoginView) findViewById(R.id.login);

        ((DefaultLoginView)login.getLoginView()).setListener(new DefaultLoginView.DefaultLoginViewListener() {


            @Override
            public void onLogin(final TextInputLayout loginUser, TextInputLayout loginPass) {
                loginPass1 = loginPass;
                loginUser1 = loginUser;



                final String user = loginUser.getEditText().getText().toString();
                if (user.isEmpty()) {
                    loginUser1.setErrorEnabled(true);
                    loginUser1.setError("用户名不能为空");
                    return;
                }
                loginUser1.setErrorEnabled(false);
                //loginUser.setError("");

                final String pass = loginPass.getEditText().getText().toString();

                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("userName",user);
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            if (list.size() == 0){
                                loginUser1.setErrorEnabled(true);
                                loginUser1.setError("用户名不存在！");
                                return;
                            }else {
                                if (list.get(0).getPassWord().equals(pass)){
                                    //((App)getApplication()).userId = list.get(0).getObjectId();
                                    ((App)getApplication()).user.setUserName(user);
                                    ((App)getApplication()).user.setPassWord(pass);
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    loginPass1.setErrorEnabled(true);
                                    loginPass1.setError("密码错误！");
                                    return;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                });

                loginPass1.setErrorEnabled(false);
                //Snackbar.make(login, "登陆成功!", Snackbar.LENGTH_LONG).show();

            }
        });

        ((DefaultRegisterView)login.getRegisterView()).setListener(new DefaultRegisterView.DefaultRegisterViewListener() {
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                registerUser1 = registerUser;
                registerPass1 = registerPass;
                registerPassRep1 = registerPassRep;


                final String user = registerUser.getEditText().getText().toString();
                if (user.isEmpty()) {
                    registerUser.setErrorEnabled(true);
                    registerUser.setError("用户名不能为空");
                    return;
                }
                registerUser.setErrorEnabled(false);

                //registerUser.setError("");
                final String pass = registerPass.getEditText().getText().toString();
                if (pass.isEmpty()) {
                    registerPass1.setErrorEnabled(true);
                    registerPass1.setError("密码不能为空");
                    return;
                }
                registerPass.setErrorEnabled(false);
                //registerPass.setError("");

                String passRep = registerPassRep.getEditText().getText().toString();
                if (!pass.equals(passRep)) {
                    registerPassRep1.setErrorEnabled(true);
                    registerPassRep1.setError("密码不一致");
                    return;
                }

                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("userName",user);
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            if (list.size() == 0){
                                ((App)getApplication()).user.setUserName(user);
                                ((App)getApplication()).user.setPassWord(pass);
                                ((App)getApplication()).user.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                    registerUser1.setErrorEnabled(true);
                                    registerUser1.setError("用户名已经存在！");
                                    return;
                            }
                        } else {
                            return;
                        }
                    }
                });

                //registerPassRep.setError(null);
                registerPassRep.setErrorEnabled(false);
                Snackbar.make(login, "注册成功!", Snackbar.LENGTH_LONG).show();
            }
        });
    }


}

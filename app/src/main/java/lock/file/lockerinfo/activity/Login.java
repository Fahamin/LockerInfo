package lock.file.lockerinfo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lock.file.lockerinfo.R;

public class Login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private ProgressDialog mProgress;
    private FirebaseAuth auth;
    TextView btnReset;
    String email, hello = "";
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

       // Toast.makeText(this, "please check internet connection", Toast.LENGTH_LONG).show();


        initView();

        intvarible();
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

    }
    private void intvarible() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            hello = hello.concat("").concat(user.getEmail());
        } else
        {
            // hello = "";
           /* startActivity(new Intent(this, MainActivity.class));
            finish();*/
            Toast.makeText(this, "Registration First", Toast.LENGTH_SHORT).show();
        }

        inputEmail.setText(hello);

    }

    private void initView() {
        inputEmail = (EditText) findViewById(R.id.inputEmailEtdId);
        inputPassword = (EditText) findViewById(R.id.inputPassEtdId);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, PasswordReset.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter Email");
                    inputEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Enter Valid Email");
                    inputEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Required password");
                    inputPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    inputPassword.setError("password check");
                    inputPassword.requestFocus();
                    return;
                }

                mProgress.show();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mProgress.dismiss();
                                    startActivity(new Intent(Login.this, MainActivity.class));

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(Login.this, "Login Failed!! \n Try again", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public void RegisterBTN(View view) {
        startActivity(new Intent(Login.this, Registration.class));

    }
}
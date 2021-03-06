package com.example.lms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lms.database.SignUpForm;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText editFirst,editLast,editAddress,editPass,editConf,editEmail,editPhone;
    private String first,last,email,pass,confirm,mobile,address;
    private Button signup;
    ProgressBar progressBar;

    //Object of SignUpForm to take input and insert into database
    SignUpForm form=new SignUpForm();

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myef=database.getReference("User");






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editFirst=findViewById(R.id.name);
        editLast=findViewById(R.id.lname);
        editEmail=findViewById(R.id.email);
        editPass=(EditText) findViewById(R.id.password);
        editConf=(EditText) findViewById(R.id.confirm_password);
        editPhone=findViewById(R.id.phone);
        editAddress=findViewById(R.id.address);
        progressBar=findViewById(R.id.progress);

        signup=findViewById(R.id.sign);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                first=editFirst.getText().toString();
                if(first.isEmpty())
                {
                    editFirst.setError("First Name Required");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    form.setFname(first);
                }
                last=editLast.getText().toString();
                form.setLname(last);
                pass=editPass.getText().toString();
                if(pass.isEmpty())
                {
                    editPass.setError("Provide password");
                    progressBar.setVisibility(View.GONE);

                }
                /*if(pass.length()<6)
                {
                    progressBar.setVisibility(View.GONE);
                    editPass.setError("Password at least 6 char");
                }*/
                else
                {
                    form.setPassword(pass);
                }

                confirm=editConf.getText().toString();

                email=editEmail.getText().toString();
                if(email.isEmpty())
                {
                    editEmail.setError("Email Required");
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    form.setEmail(email);
                }
                address=editAddress.getText().toString();
                form.setAddress(address);
                mobile=editPhone.getText().toString();
                if(mobile.isEmpty())
                {
                    editPhone.setError("Mobile Required");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    form.setMobile(mobile);
                }

                if (email.isEmpty() || first.isEmpty() || mobile.isEmpty() ||pass.isEmpty())
                {
                    progressBar.setVisibility(View.GONE);
                }
                else
                {


                    authenticate();
                }


            }
        });
    }
    public void InsertInDataBase()
    {
        String id=myef.push().getKey();
        myef.child(id).setValue(form);

        Toast.makeText(SignUp.this,"SignUp Success",Toast.LENGTH_LONG).show();
        Intent i=new Intent(SignUp.this,MainActivity.class);
        startActivity(i);

    }
    public void authenticate()
    {

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(form.getEmail(),form.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);
                    InsertInDataBase();
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this,"Failed to Register "+form.getEmail(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

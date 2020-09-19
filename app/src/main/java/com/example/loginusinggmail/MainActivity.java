package com.example.loginusinggmail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
        private SignInButton signInButton;
        private GoogleSignInClient mGoogleSignInClient;
        private String TAG="MainActivity";
        private FirebaseAuth mAuth;
        private int RC_SIGN_IN=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton=findViewById(R.id.sign_in);
        mAuth=FirebaseAuth.getInstance();

       // Configuring the google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    //function that sign In's an account
    private void signIn(){
        //Using Intent for google sign in client
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            //Creating sign task
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
           //Function that handles the sign in
            handleSignInResult(task);
        }
    }

    //Defining the  function that handles the sign in
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
                GoogleSignInAccount acc=completedTask.getResult(ApiException.class);
            Toast.makeText(MainActivity.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){

            Toast.makeText(MainActivity.this,"Sign In failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

         //Hndling firebase google Authentication
    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential= GoogleAuthProvider.getCredential(acct.getIdToken(),null);

        //Checking authentication credential
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user =mAuth.getCurrentUser();
                  //  updateUI(user);
                }
                else{
                    Toast.makeText(MainActivity.this,"Fialed",Toast.LENGTH_SHORT).show();
                   // updateUI(null);
                }

            }
        });
    }

//    private void updateUI(FirebaseUser fuser){
//
//    }
}

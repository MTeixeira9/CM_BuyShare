package com.example.android.buyshare;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Upload;
import com.example.android.buyshare.Database.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Pattern;

public class EditPerfil extends AppCompatActivity {

    private String userTlm;
    private Query q;
    private EditText nomeET, passwordET, conf_PasswET, nTlmET, emailET;
    private TextView nomeTV, pwdTV, nTlm_TV, email_TV;
    private String nome, password, conf_Passw, nTlm, email;
    private static final int RESULT_LOAD_IMAGE = 1;

    private DatabaseReference mDatabase, mDatabaseUpload;
    private StorageReference mStorageRefUpload;

    private StorageTask mUploadTask;
    private EditText mEditTextFileName;
    
    private FirebaseAuth mAuth;

    private ProgressBar mProgressBar;

    private Uri mImageUri;




    private static final String MSG_INV_EMAIL_ERRO = "Tem que inserir um email válido";


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        getSupportActionBar().setTitle("Editar Dados");

        userTlm = getIntent().getStringExtra("userTlm");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
        }else{
            signInAnonymously();
        }
        
        
        

        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabaseUpload = FirebaseDatabase.getInstance().getReference("upload");
        mStorageRefUpload = FirebaseStorage.getInstance().getReference("upload");


        nomeET = (EditText) findViewById(R.id.nome_perfil);
        passwordET = (EditText) findViewById(R.id.pass_edit);
        conf_PasswET = (EditText) findViewById(R.id.conf_pwd_edit);
        emailET = (EditText) findViewById(R.id.email_edit);


        q = mDatabase.orderByChild("numeroTlm").equalTo(userTlm);


        Button loadImage = (Button) findViewById(R.id.button_load_image);
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //photoPickerIntent.setType("image/*");
               // startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);


            }
        });

        Button upload = (Button) findViewById(R.id.uploadFoto);
        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });


        Button guardarDados = (Button) findViewById(R.id.guardarDados);
        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nome = nomeET.getText().toString();
                password = passwordET.getText().toString();
                conf_Passw = conf_PasswET.getText().toString();
                //nTlm = nTlmET.getText().toString();
                email = emailET.getText().toString();


                if (!password.equals(conf_Passw)){

                    Toast.makeText(getApplicationContext(), "Palavra passe não coincide", Toast.LENGTH_SHORT).show();

                }else if (!nome.equals("") || !password.equals("") || !conf_Passw.equals("")||  !email.equals("")) {


                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                                if(!nome.equals("")) {
                                    mDatabase.child(userTlm).child("nome").setValue(nome);
                                }

                                if(!email.equals("")) {
                                    //if (!isValidEmail(email)) {
                                      //  emailET.setError(MSG_INV_EMAIL_ERRO);
                                   // }
                                    mDatabase.child(userTlm).child("email").setValue(email);
                                }

                                if(!password.equals("")) {
                                    mDatabase.child(userTlm).child("password").setValue(password);
                                }
                            }

                            Intent i = new Intent(EditPerfil.this, Perfil.class);
                            i.putExtra("userTlm", userTlm);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Falta inserir dados", Toast.LENGTH_LONG).show();
                }

                Intent i = new Intent(EditPerfil.this, Perfil.class);
                i.putExtra("userTlm", userTlm);
                startActivity(i);
                finish();
            }

        });
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("MainActivity", "signFailed****** ", exception);
            }
        });
    }

    public final static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imageView);
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        if (mImageUri != null) {
           final StorageReference fileReference = mStorageRefUpload.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                   // mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload("Ola",
                                    fileReference.getDownloadUrl().toString());
                            String uploadId = mDatabaseUpload.push().getKey();

                            mDatabaseUpload.child(userTlm).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            //mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}

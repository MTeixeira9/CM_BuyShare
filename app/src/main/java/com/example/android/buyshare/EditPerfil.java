package com.example.android.buyshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Pattern;

public class EditPerfil extends AppCompatActivity {

    private String userTlm;
    private DatabaseReference mDatabase;
    private Query q;
    private EditText nomeET, passwordET, conf_PasswET, nTlmET, emailET;
    private TextView nomeTV, pwdTV, nTlm_TV, email_TV;
    private String nome, password, conf_Passw, nTlm, email;
    private static int RESULT_LOAD_IMAGE = 1;





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


        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        nomeET = (EditText) findViewById(R.id.nome_perfil);
        passwordET = (EditText) findViewById(R.id.pass_edit);
        conf_PasswET = (EditText) findViewById(R.id.conf_pwd_edit);
        emailET = (EditText) findViewById(R.id.email_edit);


        q = mDatabase.orderByChild("numeroTlm").equalTo(userTlm);


        Button loadImage = (Button) findViewById(R.id.button_load_image);
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(
                        //Intent.ACTION_PICK,
                        //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //startActivityForResult(i, RESULT_LOAD_IMAGE);

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);


            }
        });


        Button guardarDados = (Button) findViewById(R.id.guardarDados);
        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = nomeET.getText().toString();
                password = passwordET.getText().toString();
                conf_Passw = conf_PasswET.getText().toString();
                nTlm = nTlmET.getText().toString();
                email = emailET.getText().toString();

                if (!password.equals(conf_Passw)){

                    Toast.makeText(getApplicationContext(), "Palavra passe não coincide", Toast.LENGTH_SHORT).show();

                }else if (!nome.equals("") || !password.equals("") || !conf_Passw.equals("")|| !nTlm.equals("") || !email.equals("")) {


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

                                if(!nTlm.equals("")) {
                                    mDatabase.child(userTlm).child("numeroTlm").setValue(nTlm);
                                }

                                if(!password.equals("")) {
                                    mDatabase.child(userTlm).child("password").setValue(password);
                                }
                            }

                            Intent i = new Intent(EditPerfil.this, Perfil.class);
                            i.putExtra("userTlm", userTlm);
                            startActivity(i);
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
            }

        });
    }


    public final static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }*/


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }








}

package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Notificacoes extends AppCompatActivity {

    private String userTlm;
    private DatabaseReference mDatabaseN;
    private ListView listNotificacoes;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        getSupportActionBar().setTitle("Notificações ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userTlm = getIntent().getStringExtra("userTlm");
        mDatabaseN = FirebaseDatabase.getInstance().getReference("notificacoes");




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Notificacoes.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }
}

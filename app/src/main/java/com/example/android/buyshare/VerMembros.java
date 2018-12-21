package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class VerMembros extends AppCompatActivity {

    private String userTlm, position, nameL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_membros);

        userTlm = getIntent().getStringExtra("userTlm");
        position = getIntent().getStringExtra("position");
        nameL = getIntent().getStringExtra("nameL");

        getSupportActionBar().setTitle("Membros de [nome lista]");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
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
        Intent i = new Intent(VerMembros.this, MostraLista.class);
        i.putExtra("userTlm", userTlm);
        i.putExtra("nameL", nameL);
        i.putExtra("position", position);
        startActivity(i);
    }
}

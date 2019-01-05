package com.example.android.buyshare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Amigos extends AppCompatActivity {

    private static final String MSG_SUCESSO = "Amigo adicionado com sucesso!";
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseUploads;
    private ArrayAdapter<String> mAdapter;
    private String userLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        getSupportActionBar().setTitle("Meus Amigos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addAmigos = findViewById(R.id.addAmigos);
        userLogado = getIntent().getStringExtra("userTlm");

        addAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //POPUP
                Intent i = new Intent(Amigos.this, PopUpAddAmigo.class);
                i.putExtra("userTlm", userLogado);
                startActivityForResult(i, 1);
            }
        });

        final ListView mListAmigos = findViewById(R.id.listAmigos);
        final List<RowItem> rowItems = new ArrayList<>();

        //mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        /**
         * opcoes amigos
         * */
        registerForContextMenu(mListAmigos);

        //adicionar logo os amigos da base de dados
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseUploads = FirebaseDatabase.getInstance().getReference("upload");

        Query q = mDatabaseUsers.orderByChild("numeroTlm").equalTo(userLogado);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    User u = singleSnapShot.getValue(User.class);
                    Map<String, String> amigos = u.getAmigos();

                    if(amigos != null) {
                        for (Map.Entry<String, String> amigo : amigos.entrySet()) {
                            String url = mDatabaseUploads.child(amigo.getKey()).child("imageUrl").toString();
                            ImageView img = new ImageView(getApplicationContext());
                            if (url == null){
                                Drawable d = getResources().getDrawable(R.drawable.user_icon);
                                img.setImageDrawable(d);
                            }
                            else {
                                new DownloadImageTask((ImageView) img).execute(url);
                            }
                            RowItem r = new RowItem(img.getId(), amigo.getValue(), amigo.getKey());
                            rowItems.add(r);
                        }
                    }

                    CustomBaseAdapter adapter = new CustomBaseAdapter(getApplicationContext(), rowItems);
                    mListAmigos.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v,
                                     ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Eliminar");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                String nome = data.getStringExtra("nomeA");
                String tlmv = data.getStringExtra("nTlm");
                String novoAmigo = nome + " " + tlmv;
                mAdapter.add(novoAmigo);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), MSG_SUCESSO, Toast.LENGTH_LONG).show();
            }
        }
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
        Intent i = new Intent(Amigos.this, MinhasListas.class);
        i.putExtra("userTlm", userLogado);
        startActivity(i);
    }
}
package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class AdicionarMembrosMostraLista extends AppCompatActivity {

    private SectionsPageAdapter mSectrionsPageAdapter;
    private ViewPager mViewPager;
    private String userTlm, position, nameL, idL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_membros_mostra_lista);

        getSupportActionBar().setTitle("Adicionar Membros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectrionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        userTlm = getIntent().getStringExtra("userTlm");
        position = getIntent().getStringExtra("position");
        nameL = getIntent().getStringExtra("nameL");
        idL = getIntent().getStringExtra("idL");

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager (ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragAddMembrosAmigos(), "Amigos");
        adapter.addFragment(new FragAddMembrosGrupos(), "Grupos");
        viewPager.setAdapter(adapter);
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
        Intent i = new Intent(AdicionarMembrosMostraLista.this, MostraLista.class);
        i.putExtra("userTlm", userTlm);
        i.putExtra("nameL", nameL);
        i.putExtra("position", position);
        i.putExtra("idL", idL);
        startActivity(i);
    }

}
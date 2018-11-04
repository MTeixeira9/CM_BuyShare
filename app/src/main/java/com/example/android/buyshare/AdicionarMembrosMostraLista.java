package com.example.android.buyshare;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class AdicionarMembrosMostraLista extends AppCompatActivity {

    private SectionsPageAdapter mSectrionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_membros_mostra_lista);

        getSupportActionBar().setTitle("Adicionar Membros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectrionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

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

}
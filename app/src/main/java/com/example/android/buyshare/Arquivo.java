package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Arquivo extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    private ListView listaArq;
    private String userTlm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arquivo);

        getSupportActionBar().setTitle("Listas Arquivadas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ir buscar quem estah autenticado
        userTlm = getIntent().getStringExtra("userTlm");

        listaArq = findViewById(R.id.listasArquivadas);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaArq.setAdapter(mAdapter);

        /*

        ...
setListAdapter(lists);
registerForContextMenu(getListView());
Then you provide code ot create the context menu (you can create it from an XML resource too, but I don't have an example of that handy):

@Override
public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.setHeaderTitle("Item Operations");
    menu.add(0, v.getId(), 0, "Edit Item");
    menu.add(0, v.getId(), 0, "Delete Item");
}
Then you provide the code to handle the options in the menu:

@Override
public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
            .getMenuInfo();
    if (item.getTitle() == "Edit Item") {
        mRowId = info.id;
        DialogFragment_Item idFragment = new DialogFragment_Item();
        idFragment.show(getFragmentManager(), "dialog");
    } else if (item.getTitle() == "Delete Item") {
        mDbHelper.deleteItem(info.id);
        return true;
    }
    return super.onContextItemSelected(item);
}

         */


        //Listas iniciais
        mAdapter.add("Jantar de Aniversário");
        mAdapter.add("Jantar da Faculdade");
        mAdapter.add("Supermercado");
        mAdapter.notifyDataSetChanged();

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
        Intent i = new Intent(Arquivo.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }

}

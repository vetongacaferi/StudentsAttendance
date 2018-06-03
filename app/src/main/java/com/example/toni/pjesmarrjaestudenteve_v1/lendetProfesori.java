package com.example.toni.pjesmarrjaestudenteve_v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class lendetProfesori extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lendet_profesori_layout);

        Intent intenti = getIntent();
        final String idProf = intenti.getStringExtra(profesori.idProfesor);

        BackgroundTask background = new BackgroundTask(lendetProfesori.this);
        background.execute(BackgroundTask.showLendet, idProf);

        ListView listview = (ListView) findViewById(R.id.lvLendet);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tagText = (TextView) view.findViewById(R.id.tvLendaProf);
                String tag = tagText.getText().toString();
                Intent intent_date = new Intent(getApplicationContext(), date_activity.class);
                intent_date.putExtra(profesori.idProfesor, idProf);
                intent_date.putExtra(date_activity.LENDA_SELECTED, tag);
                startActivity(intent_date);

            }
        });
    }


    public class lendet {

        private String lenda;

        public lendet(String _lenda) {
            this.setLenda(_lenda);
        }

        public String getLenda() {
            return lenda;
        }

        public void setLenda(String lenda) {
            this.lenda = lenda;
        }
    }


    public static class lendetHolder {
        TextView tv_lenda;
    }

    public class lendetAdapter extends ArrayAdapter {

        List lista = new ArrayList();

        public lendetAdapter(Context context, int resource) {
            super(context, resource);
        }

        public void add(lendet object) {
            lista.add(object);
            super.add(object);
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return lista.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            lendetHolder lendetholder;
            if (row == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.lendet_profesoir_row, parent, false);

                lendetholder = new lendetHolder();
                lendetholder.tv_lenda = (TextView) row.findViewById(R.id.tvLendaProf);
                row.setTag(lendetholder);

            } else {
                lendetholder = (lendetHolder) row.getTag();
            }
            lendet _lendet = (lendet) getItem(position);
            lendetholder.tv_lenda.setText(_lendet.getLenda().toString());
            return row;
        }
    }
    public class BackgroundTask extends AsyncTask<String, lendet, String> {

        public static final String showLendet = "lendet";

        Context ctx;
        Activity aktiviti;
        lendetAdapter lendet_adapter;
        ListView lista_view;
        BackgroundTask(Context ctx) {
            this.ctx = ctx;
            this.aktiviti = (Activity) ctx;
        }
        @Override
        protected String doInBackground(String... params) {

            DatabaseHelper obj = new DatabaseHelper(ctx);
            String vlera = params[0];
            if (vlera.equals(showLendet)) {
                lista_view = (ListView) aktiviti.findViewById(R.id.lvLendet);
                lendet_adapter = new lendetAdapter(ctx, R.layout.lendet_profesoir_row);
                String idProf = params[1];
                Cursor kursori = obj.getTemat(idProf);
                String lenda;
                while (kursori.moveToNext()) {
                    lenda = kursori.getString(0);
                    lendet obj_lenda = new lendet(lenda);
                    publishProgress(obj_lenda);
                }
                return "get_info";
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(lendet... values) {
            lendet_adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("get_info")) {
                lista_view.setAdapter(lendet_adapter);
            }
        }
    }
}

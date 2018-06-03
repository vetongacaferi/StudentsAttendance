package com.example.toni.pjesmarrjaestudenteve_v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShikoPjesmarrjen extends AppCompatActivity {

    Spinner spinner_lenda;
    TextView tvNrEvidenca;
    int nr_Evid;

    DatabaseHelper obj_db;

    ArrayList<String> lista_lendet;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiko_evidencen);

        spinner_lenda=(Spinner)findViewById(R.id.spinnerLendaSt);
        tvNrEvidenca=(TextView)findViewById(R.id.tvLendaStudPjes);


        obj_db=new DatabaseHelper(this);

        lista_lendet=new ArrayList<>();

        Intent intenti=getIntent();
        final String _idProfesor=intenti.getStringExtra(profesori.idProfesor);

        try{
            Cursor result=obj_db.getTemat(_idProfesor);

            while(result.moveToNext())
            {
                lista_lendet.add(result.getString(0));
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(ShikoPjesmarrjen.this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        adapter=new ArrayAdapter<String>(ShikoPjesmarrjen.this,android.R.layout.simple_spinner_dropdown_item,lista_lendet);

        spinner_lenda.setAdapter(adapter);

       spinner_lenda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                doBackgrounTask backgroundtask = new doBackgrounTask(ShikoPjesmarrjen.this);
                backgroundtask.execute("shikoDate",lista_lendet.get(position).toString(),_idProfesor);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
       });


        String lenda_zgjedhur= String.valueOf(spinner_lenda.getSelectedItem());
        doBackgrounTask backgroundtask = new doBackgrounTask(ShikoPjesmarrjen.this);
        backgroundtask.execute("shikoDate",lenda_zgjedhur,_idProfesor);


    }


    public class Student {
        String emri_mbiemri;
        String java;

        public Student(String emri_mbiemri, String java) {
            this.setEmri_mbiemri(emri_mbiemri);
            this.setJava(java);
        }

        public String getEmri_mbiemri() {
            return emri_mbiemri;
        }

        public void setEmri_mbiemri(String emri_mbiemri) {
            this.emri_mbiemri = emri_mbiemri;
        }

        public String getJava() {
            return java;
        }

        public void setJava(String java) {
            this.java = java;
        }
    }


    public static class StudentPjesHolder {
        TextView emri_mbiemi, java;
    }


    public class PjesmarrjaList extends ArrayAdapter {
        List list = new ArrayList();


        public PjesmarrjaList(Context context, int resource) {
            super(context, resource);
        }


        public void add(Student objekt) {
            list.add(objekt);
            super.add(objekt);
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            StudentPjesHolder objholder;

            if (row == null) {
                LayoutInflater layoutinflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutinflater.inflate(R.layout.student_pjesmarrja_row, parent, false);
                objholder = new StudentPjesHolder();
                objholder.emri_mbiemi = (TextView) row.findViewById(R.id.tvEmriMbimeri);
                objholder.java = (TextView) row.findViewById(R.id.tvJavetPjesmarres);
                row.setTag(objholder);
            } else {
                objholder = (StudentPjesHolder) row.getTag();
            }

            Student objlista = (Student) getItem(position);
            objholder.emri_mbiemi.setText(objlista.getEmri_mbiemri().toString());
            objholder.java.setText(objlista.getJava().toString());

            return row;
        }

    }

    public class doBackgrounTask extends AsyncTask<String, Student, String> {
        Context ctx;
        Activity activity;
        ListView listview;
        PjesmarrjaList pjesmarres_adapter;

        public doBackgrounTask(Context ctx) {
            this.ctx = ctx;
            activity = (Activity) ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            DatabaseHelper obj = new DatabaseHelper(ctx);
            String vlera_param = params[0];
            String _lenda=params[1];
            String _idProfesor=params[2];
            if (vlera_param.equals("shikoDate")) {

                //objKursori.getString(0).
                listview = (ListView) activity.findViewById(R.id.lvStudentPjes);
                pjesmarres_adapter = new PjesmarrjaList(ctx, R.layout.student_pjesmarrja_row);

                //ketu duhet te na vij lenda dhe te rifomohet query
                Cursor objKursori = obj.getData(DatabaseHelper.TABLE_NAME_STUD);
                Cursor kursoriStudentatPjes=obj.getEvidencaPjesmarres(_lenda,_idProfesor);
                String emri_mbiemri,_numri,id;

                while (objKursori.moveToNext()) {
                    emri_mbiemri = objKursori.getString(1);
                    id=objKursori.getString(0);

                    int[] nr_PjesEvid=nrPjesmarresEvidencav(id,kursoriStudentatPjes,0);
                    int nr_Pjes=nr_PjesEvid[0];
                    nr_Evid =nr_PjesEvid[1];

                    kursoriStudentatPjes.moveToFirst();
                    kursoriStudentatPjes.moveToPrevious();
                    _numri=Integer.toString(nr_Pjes);

                    Student objlista = new Student(emri_mbiemri,_numri);
                    publishProgress(objlista);
                }

                return "get_info";
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Student... values) {
            pjesmarres_adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("get_info")) {
                listview.setAdapter(pjesmarres_adapter);
                tvNrEvidenca.setText(Integer.toString(nr_Evid)+" jave");
            }
        }

    }

    public int[] nrPjesmarresEvidencav(String studenti,Cursor studentatPjesmarres,int indexi)
    {
        int nrPjesmarres=0,nrEvidencav=0;
        while(studentatPjesmarres.moveToNext())
        {
            if(studentatPjesmarres.getString(indexi).contains(studenti))
            {
                nrPjesmarres++;
            }
            nrEvidencav++;
        }
        return new int[]{nrPjesmarres, nrEvidencav};
    }
}

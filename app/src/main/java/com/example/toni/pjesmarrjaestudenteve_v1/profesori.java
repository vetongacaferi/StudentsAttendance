package com.example.toni.pjesmarrjaestudenteve_v1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class profesori extends AppCompatActivity  {

    LinearLayout lnKrijoPjesmarrje,lnGjeneroPdf,lnShikoPjesmarrje;

    public static final String idProfesor="idja";
    public static final String nameProfesor="nameProf";
    public static final String surnProfesor="surnamProf";

    DatabaseHelper obj_db;

    public String _idProfesor;
    public String _nameProfesor;
    public String _surnamProfesor;

    ArrayList<String> lista_lendet;
    ArrayAdapter<String> adapter;
    AlertDialog dialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesori);

        lista_lendet=new ArrayList<>();

        obj_db=new DatabaseHelper(this);

        lnKrijoPjesmarrje=(LinearLayout)findViewById(R.id.lnKrijoPjesmarrjen);
        lnGjeneroPdf=(LinearLayout)findViewById(R.id.lnKrijoPdf);
        lnShikoPjesmarrje=(LinearLayout)findViewById(R.id.lnShikoPjesmarrje);

        Intent intenti=getIntent();

        _idProfesor=intenti.getStringExtra(idProfesor);
        _nameProfesor=intenti.getStringExtra(nameProfesor);
        _surnamProfesor=intenti.getStringExtra(surnProfesor);

        try{
            Cursor result=obj_db.getTemat(_idProfesor);

            while(result.moveToNext())
            {
                lista_lendet.add(result.getString(0));
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(profesori.this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }


        lnKrijoPjesmarrje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(profesori.this);
                View view1 =getLayoutInflater().inflate(R.layout.dialog_evidence,null);

                final Spinner spinnerLenda=view1.findViewById(R.id.spinLenda);
                final EditText tema=view1.findViewById(R.id.etTema);
                final EditText grupi=view1.findViewById(R.id.etGrupi);
                final EditText java=view1.findViewById(R.id.etJava);
                final Button krijoEvidenc=view1.findViewById(R.id.btnkrijoEvidenc);
                final TextView kontrolloFuasht=view1.findViewById(R.id.tvMesazhiPerKlikim);

                 adapter = new ArrayAdapter<>(
                        profesori.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        lista_lendet
                );
                spinnerLenda.setAdapter(adapter);

                builder.setView(view1);
                dialog=builder.create();
                dialog.show();
                krijoEvidenc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tema.getText().toString().isEmpty() || grupi.getText().toString().isEmpty() || java.getText().toString().isEmpty()) {
                            kontrolloFuasht.setText("Ploteso te gjitha fushat.");
                        }
                        else
                        {
                            Intent mainActivity = new Intent(profesori.this, MainActivity.class);
                            mainActivity.putExtra(MainActivity.LENDA, String.valueOf(spinnerLenda.getSelectedItem()));
                            mainActivity.putExtra(MainActivity.TEMA, tema.getText().toString());
                            mainActivity.putExtra(MainActivity.GRUPI, grupi.getText().toString());
                            mainActivity.putExtra(MainActivity.JAVA, java.getText().toString());
                            mainActivity.putExtra(idProfesor, _idProfesor);
                            startActivity(mainActivity);
                            dialog.dismiss();
                        }
                    }
                });


            }
        });

        lnGjeneroPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intenti= new Intent(profesori.this,lendetProfesori.class);
                intenti.putExtra(idProfesor,_idProfesor);
                startActivity(intenti);

            }
        });

        lnShikoPjesmarrje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenti=new Intent(profesori.this,ShikoPjesmarrjen.class);
                intenti.putExtra(idProfesor,_idProfesor);
                startActivity(intenti);
            }
        });
    }

}

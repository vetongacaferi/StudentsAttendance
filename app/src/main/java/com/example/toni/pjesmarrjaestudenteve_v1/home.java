package com.example.toni.pjesmarrjaestudenteve_v1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class home extends AppCompatActivity {

    TextView _tvUsernamePassword;
    EditText _etTitulli,_etPassword;
    Button _btnStart;
    String _vleraTitullit;
    DatabaseHelper obj_db;
    String ID,Name,Surname;
    String _perdoruesi,_passwordi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        obj_db=new DatabaseHelper(this);
        try
        {
            insertDataAuto(); //Vetem heren e par# duhet te insertohen te dhenat
        }
        catch (Exception ex)
        {
           Toast.makeText(home.this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

        _etTitulli=(EditText)findViewById(R.id.etTitulli);
        _etPassword=(EditText)findViewById(R.id.etPassword);
        _tvUsernamePassword=(TextView)findViewById(R.id.tvUsernamePassword);
        _perdoruesi=_etTitulli.getText().toString();

        _btnStart=(Button)findViewById(R.id.btnStarto);
        _btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _vleraTitullit = _etTitulli.getText().toString();
                _passwordi = _etPassword.getText().toString();
                if (_vleraTitullit.isEmpty() || _passwordi.isEmpty()) {
                        _tvUsernamePassword.setText("Plotësoni fushat e zbrazta.");
                        return;
                }
                Character karakteri = _vleraTitullit.charAt(0);
                if (karakteri.equals('p')) {
                        //profesori
                        try {
                            Cursor kursori = obj_db.getData(DatabaseHelper.TABLE_NAME_PROF);
                            while (kursori.moveToNext()) {
                                if (_vleraTitullit.equals(kursori.getString(0))) {
                                    if(_passwordi.equals(kursori.getString(3))) {
                                        ID = _vleraTitullit;
                                        Name = kursori.getString(1);
                                        Surname = kursori.getString(2);
                                        Intent intent_profesori = new Intent(home.this, profesori.class);
                                        intent_profesori.putExtra(profesori.idProfesor, ID);
                                        intent_profesori.putExtra(profesori.nameProfesor, Name);
                                        intent_profesori.putExtra(profesori.surnProfesor, Surname);
                                        startActivity(intent_profesori);
                                        return;
                                    }
                                    else
                                    {
                                        _tvUsernamePassword.setText("Passwordi eshte gabim.");
                                    }
                                }
                            }
                            _tvUsernamePassword.setText("Nuk ka profesor me kete ID!");
                        } catch (Exception ex) {
                            _tvUsernamePassword.setText("Dicka ka shkuar gabim!");
                        }
                    } else if (karakteri.equals('s')) {
                        try {
                            Cursor kursori = obj_db.getData(DatabaseHelper.TABLE_NAME_STUD);
                            while (kursori.moveToNext()) {
                                if (_vleraTitullit.equals(kursori.getString(0))) {
                                    if(_passwordi.equals(kursori.getString(2))) {
                                        ID = _vleraTitullit;
                                        Name = kursori.getString(1);
                                        Intent obj_studenti = new Intent(home.this, studenti.class);
                                        obj_studenti.putExtra(studenti.idStudent, ID);
                                        obj_studenti.putExtra(studenti.emriStudent, Name);
                                        startActivity(obj_studenti);
                                        return;
                                    }
                                    else
                                    {
                                        _tvUsernamePassword.setText("Passwordi eshte gabim.");
                                    }
                                }
                            }
                            _tvUsernamePassword.setText("Nuk ka student me kete ID!");

                        } catch (Exception ex) {
                            _tvUsernamePassword.setText("Dicka ka shkuar gabim!");
                        }
                    } else {
                        _tvUsernamePassword.setText("ID nuk eshte ne rregull!");
                    }
            }
        });

    }


    void insertDataAuto ()
    {
        obj_db=new DatabaseHelper(this);
        obj_db.insertDataStudent("s100002", "Bajram Gashi","1");
        obj_db.insertDataStudent("s100001", "Fatime Krasniqi","1");
        obj_db.insertDataStudent("s100003", "Emine Berisha","1");
        obj_db.insertDataStudent("s100004", "Bekim Morina","1");
        obj_db.insertDataStudent("s100005", "Aferdita Shala","1");
        obj_db.insertDataStudent("s100006", "Valon Bytyqi","1");
        obj_db.insertDataStudent("s100007", "Egzon Hasani","1");
        obj_db.insertDataStudent("s100008", "Arben Kastrati","1");
        obj_db.insertDataStudent("s100009", "Ramadan Kryeziu","1");
        obj_db.insertDataStudent("s100010", "Muhamet Hoti","1");

        obj_db.insertDataProfesor("p100001", "Blerim", "Rexha","1");
        obj_db.insertDataProfesor("p100002", "Isak", "Shabani","1");
        obj_db.insertDataProfesor("p100003", "Arbnor", "Halili","1");

        obj_db.insertDataLendet("1","p100001","Siguria e te dhenave");
        obj_db.insertDataLendet("2","p100001","Rrjeta Kompjuterike");
        obj_db.insertDataLendet("3","p100001","Siguria ne internet");
        obj_db.insertDataLendet("4","p100001","Inxhinieri Softverike");
        obj_db.insertDataLendet("5","p100001","Programim ne paisje mobile");

        obj_db.insertDataLendet("6","p100002","Programimi i orientuar ne objekte");
        obj_db.insertDataLendet("7","p100002","Komunikim njeri kompjuter");
        obj_db.insertDataLendet("8","p100002","Sisteme operative");
        obj_db.insertDataLendet("9","p100002","Sisteme te shprendara");


       obj_db.insertDataLendet("10","p100003","Sigurija e te dhenave");
       obj_db.insertDataLendet("11","p100003","Programim ne paisje mobile");

    }
}

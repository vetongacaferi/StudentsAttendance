package com.example.toni.pjesmarrjaestudenteve_v1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class date_activity extends generatePDF {

    public static final String LENDA_SELECTED="selected_lenda";
    String lenda,profiId;
    DatabaseHelper obj_database;

    //per pdf
    private static final String TAG = "PdfCreatorActivity";
    int l=0;
    String[] studnetListaPDF;
    String _lenda,_java,_grupi,_tema,_date,listaStud;
    String date;

    public String profesoriName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_activity);

        obj_database = new DatabaseHelper(this);

        Intent intenti = getIntent();
        lenda = intenti.getStringExtra(LENDA_SELECTED);
        profiId = intenti.getStringExtra(profesori.idProfesor);

        doBackgrounTask obj_doBackgorund = new doBackgrounTask(this);
        obj_doBackgorund.execute("shikoDate", lenda, profiId);

        try
        {
            Cursor kursori_emrimbiemri=obj_database.getProfesoriName(profiId);
            while(kursori_emrimbiemri.moveToNext())
            {
                profesoriName=kursori_emrimbiemri.getString(0)+" "+kursori_emrimbiemri.getString(1);
            }
        }
        catch (Exception ex)
        {
        }


        ListView listview = (ListView)findViewById(R.id.lvDateEvid);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tagText = (TextView) view.findViewById(R.id.txtDataProf);
                date = tagText.getText().toString();
                String[] date1=date.split(" ");

                String grupi=date1[date1.length-1];

                Cursor objKursori = obj_database.getEvidencenStudent(lenda,profiId,date1[0],grupi);

                StringBuffer bafferi=new StringBuffer();

                bafferi.append(date+lenda+profiId);
                while(objKursori.moveToNext())
                {

                    _date=objKursori.getString(1);
                    _lenda=objKursori.getString(7);
                    _java=objKursori.getString(5);
                    _grupi=objKursori.getString(4);
                    _tema=objKursori.getString(3);
                    listaStud=objKursori.getString(2);
                }
                String lista=listaStud;
                String[] studentlista = listaStudentev(lista);
                studnetListaPDF=studentListaPDF(studentlista);

                try {
                    if( checkForPermission()) {
                        ArrayList<String> test=new ArrayList<String>();
                        createPdf(test,studnetListaPDF, _lenda, _java, _grupi, _tema, _date, profesoriName, R.drawable.logoup, 2);
                        if (Build.VERSION.SDK_INT >= 24) {
                            try {
                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                m.invoke(null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(date_activity.this, "nuk pati sukses", Toast.LENGTH_LONG).show();
                        }
                        previewPdf();
                    }
                } catch (FileNotFoundException ex) {
                    Toast.makeText(date_activity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                } catch (DocumentException ex) {

                    Toast.makeText(date_activity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }
                catch(Exception ex)
                {
                    Toast.makeText(date_activity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }
                finally {
                   recreate();
                }
            }
        });
    }


    public class date_listview {
        String date;
        public date_listview(String date)
        {
            this.setDate(date);
        }
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
    }


    public static class dateHolder {
        TextView txtRreshti;
    }

    public class AdapteriLista extends ArrayAdapter {

        List list=new ArrayList();

        public void add(date_listview object) {
            list.add(object);
            super.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        public AdapteriLista(Context context, int resource) {
            super(context, resource);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row=convertView;
            dateHolder objholder;

            if(row==null)
            {
                LayoutInflater layoutinflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=layoutinflater.inflate(R.layout.date_layout_row,parent,false);
                objholder=new dateHolder();
                objholder.txtRreshti=(TextView)row.findViewById(R.id.txtDataProf);
                row.setTag(objholder);
            }
            else
            {
                objholder=(dateHolder)row.getTag();
            }
            date_listview objlista=(date_listview)getItem(position);
            objholder.txtRreshti.setText(objlista.getDate().toString());

            return row;
        }

    }

    public class doBackgrounTask extends AsyncTask<String,date_listview,String>
    {
        Context ctx;
        Activity activity;
        ListView listview;
        AdapteriLista dateadapter;

        public doBackgrounTask(Context ctx)
        {
            this.ctx=ctx;
            activity=(Activity)ctx;
        }
        @Override
        protected String doInBackground(String... params) {
            DatabaseHelper obj = new DatabaseHelper(ctx);
            String vlera_param=params[0];

            if(vlera_param.equals("shikoDate")) {

                String _lenda=params[1];
                String _profiID=params[2];
                Cursor objKursori = obj.getEvidenca(_lenda,_profiID);

                listview = (ListView) activity.findViewById(R.id.lvDateEvid);
                dateadapter = new AdapteriLista(ctx, R.layout.date_layout_row);

                String date,grupi;
                while (objKursori.moveToNext()) {
                    date = objKursori.getString(0);
                    grupi=objKursori.getString(1);
                    date_listview objlista = new date_listview(date+"              gr: "+grupi);
                    publishProgress(objlista);
                }
                return "get_info";
            }
            return null;
        }



        @Override
        protected void onProgressUpdate(date_listview... values) {
            dateadapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(String  result){
            if(result.equals("get_info")) {
                listview.setAdapter(dateadapter);
            }
        }
    }


    public String[] listaStudentev(String _listaStudentev)
    {
        String[] splitLista=_listaStudentev.split(",");
        String[] listaStudentev=new String[splitLista.length];

        for(int i=0;i<splitLista.length;i++)
        {
            listaStudentev[i]=splitLista[i];
        }
        return listaStudentev;
    }
    private String[] studentListaPDF(String[] studentLista)
    {
        String[] lista=new String[studentLista.length*2];
        Cursor kursori=obj_database.getData(DatabaseHelper.TABLE_NAME_STUD);
        int i=0;
        while(kursori.moveToNext())
        {
            for(int j=0;j<studentLista.length;j++) {
                if (studentLista[j].equals(kursori.getString(0))) {
                    lista[i] = kursori.getString(1);
                    i++;
                    lista[i] = studentLista[j];
                    i++;
                }
            }
        }
        return lista;
    }

}

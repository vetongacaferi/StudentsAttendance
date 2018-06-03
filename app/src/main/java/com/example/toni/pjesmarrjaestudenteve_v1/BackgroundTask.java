package com.example.toni.pjesmarrjaestudenteve_v1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by TONI on 5/9/2018.
 */

public class BackgroundTask extends AsyncTask<String,String,String> {

    Context ctx;

    public static final String addEvent="addEvent";
    public static final String getProfesori="profesoriName";
    public static final String showLendet="lendet";
    public static final String showEvidenca="evidenca";
    public static final String getLogin="login";

    ListView  lista_view;

    Activity aktiviti;

    BackgroundTask(Context ctx)
    {
        this.ctx=ctx;
        this.aktiviti=(Activity)ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        DatabaseHelper obj=new DatabaseHelper(ctx);

        String vlera=params[0];

        if(vlera.equals(addEvent))
        {
            String data=params[1];
            String studentLista=params[2];
            String tema=params[3];
            String grupi=params[4];
            String java=params[5];
            String profesori=params[6];
            String lenda=params[7];

            boolean vlera_query=obj.insertDataEvidenca(data,studentLista,tema,grupi,java,profesori,lenda);
            if(vlera_query) {
                return "succesfull evidence inserted...";
            }else
            {
                return "can't insert those values ...";
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected void onPostExecute(String result) {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();

    }
}

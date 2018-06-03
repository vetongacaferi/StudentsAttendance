package com.example.toni.pjesmarrjaestudenteve_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TONI on 5/5/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    //constant for database

    public static final String DATABASE_NAME="student.db";

    //TabelaProfesori
    public static final String TABLE_NAME_PROF="profesori";
    public static final String PROFESORI_ID="profesoriID";
    public static final String PROFESORI_PASSWORD="profesoriPASSWORD";
    public static final String PROFESORI_NAME="profesoriNAME";
    public static final String PROFESROI_SURN="profesoriSURN";

    //TabelaStudenti
    public static final String TABLE_NAME_STUD="studenti";
    public static final String STUDENTI_ID="studentiID";
    public static final String STUDENTI_PASSWORD="studentiPASSWORD";
    public static final String STUDENT_NAME="studentiNS";

    //TabelaLendet

    public static final String TABLE_NAME_LENDET="lendet";
    public static final String LENDET_ID="idjaLendet";
    public static final String LENDET_IDprof="IDprof";
    public static final String LENDET_NAME="lendetNAME";



    //Tabela Evidenca
    public static final String TABLE_NAME_EVIDENCA="evidenca";
    public static final String EVIDENCA_ID="evidencaID";
    public static final String EVIDENCA_DATA="data";
    public static final String EVIDENCA_STUD="studentLista";
    public static final String EVIDENCA_TEMA="temaEvi";
    public static final String EVIDENCA_GRUPI="grupi";
    public static final String EVIDENCA_JAVA="java";
    public static final String EVIDENCA_PROFID="profID";
    public static final String EVIDENCA_LENDA="lenda";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME_STUD+"("+STUDENTI_ID+" TEXT PRIMARY KEY, "+STUDENT_NAME+" TEXT,"+STUDENTI_PASSWORD+" TEXT);");
         sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME_PROF+"("+PROFESORI_ID+" TEXT PRIMARY KEY,"+PROFESORI_NAME+" TEXT, "+PROFESROI_SURN+" TEXT, "+PROFESORI_PASSWORD+" TEXT);");
         sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME_LENDET+"("+LENDET_ID+" INTEGER PRIMARY KEY,"+LENDET_IDprof+" TEXT,"+LENDET_NAME+" TEXT, FOREIGN KEY ("+LENDET_IDprof+") REFERENCES "+TABLE_NAME_PROF+"("+PROFESORI_ID+") "+")");
         sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME_EVIDENCA+"("+EVIDENCA_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+EVIDENCA_DATA+" TEXT,"+EVIDENCA_STUD+" TEXT, "
                 +EVIDENCA_TEMA+" TEXT," +EVIDENCA_GRUPI+" TEXT,"+EVIDENCA_JAVA+" TEXT, "+EVIDENCA_PROFID+" TEXT, "+EVIDENCA_LENDA+" TEXT,"+
                        " FOREIGN KEY("+EVIDENCA_PROFID+") REFERENCES "+TABLE_NAME_PROF +"("+PROFESORI_ID+"),"+
                 " FOREIGN KEY("+EVIDENCA_LENDA+") REFERENCES "+TABLE_NAME_LENDET+"("+LENDET_NAME+"))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_EVIDENCA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_STUD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_PROF);
        onCreate(sqLiteDatabase);
    }

    public boolean insertDataEvidenca(String _evidencaData,String _studentLista,String _tema,String _grupi,String _java,String _profiID,String _evidencaLenda)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(EVIDENCA_DATA,_evidencaData);
        contentValues.put(EVIDENCA_STUD,_studentLista);
        contentValues.put(EVIDENCA_TEMA,_tema);
        contentValues.put(EVIDENCA_GRUPI,_grupi);
        contentValues.put(EVIDENCA_JAVA,_java);
        contentValues.put(EVIDENCA_PROFID,_profiID);
        contentValues.put(EVIDENCA_LENDA,_evidencaLenda);


        long check=db.insert(TABLE_NAME_EVIDENCA,null,contentValues);
       if(check==-1)
       {
           return false;
       }
        else
       {
           return true;
       }
    }

    public void dropTable()
    {

        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_EVIDENCA);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_STUD);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_PROF);
    }


    public boolean insertDataStudent(String _StudentiID,String _StudentiName,String _StudentPassword)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(STUDENTI_ID,_StudentiID);
        contentValues.put(STUDENT_NAME,_StudentiName);
        contentValues.put(STUDENTI_PASSWORD,_StudentPassword);
        long check=db.insert(TABLE_NAME_STUD,null,contentValues);
        if(check==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean insertDataProfesor(String _PROFESORI_ID,String _PROFESORI_NAME,String _PROFESROI_SURN,String _PROFESORI_PASSW)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(PROFESORI_ID,_PROFESORI_ID);
        contentValues.put(PROFESORI_NAME,_PROFESORI_NAME);
        contentValues.put(PROFESROI_SURN,_PROFESROI_SURN);
        contentValues.put(PROFESORI_PASSWORD,_PROFESORI_PASSW);


        long check=db.insert(TABLE_NAME_PROF,null,contentValues);
        if(check==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean insertDataLendet(String _LENDA_ID,String _PROFESORI_ID,String _Lenda)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(LENDET_ID,_LENDA_ID);
        contentValues.put(LENDET_IDprof,_PROFESORI_ID);
        contentValues.put(LENDET_NAME,_Lenda);
        long check=db.insert(TABLE_NAME_LENDET,null,contentValues);
        if(check==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getData(String _emriTabelese)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor result;
        result=db.rawQuery("select * from "+_emriTabelese,null);
        return result;
    }

    public Cursor getTemat(String _where)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor result;
        result=db.query(TABLE_NAME_LENDET,new String[]{LENDET_NAME}, LENDET_IDprof+"=?", new String[] {_where}, null, null, null);
        return result;
    }

    public Cursor getProfesoriName(String id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor result;
        result=db.query(TABLE_NAME_PROF,new String[]{PROFESORI_NAME,PROFESROI_SURN}, PROFESORI_ID+"=?", new String[] {id}, null, null, null);
        return result;
    }

    public Cursor getEvidenca(String _lenda,String idProfesor )
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor result;
        String whereClause = DatabaseHelper.EVIDENCA_LENDA + " = ? AND " + DatabaseHelper.EVIDENCA_PROFID  + " = ?";
        String[] whereArgs = new String[]{_lenda,idProfesor};
        String[] projections={DatabaseHelper.EVIDENCA_DATA,DatabaseHelper.EVIDENCA_GRUPI};
        result = db.query(TABLE_NAME_EVIDENCA,projections,whereClause,whereArgs,null,null,null);
        return result;
    }
    public Cursor getEvidencaPjesmarres(String _lenda,String idProfesor )
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor result;
        String whereClause = DatabaseHelper.EVIDENCA_LENDA + " = ? AND " + DatabaseHelper.EVIDENCA_PROFID  + " = ?";
        String[] whereArgs = new String[]{_lenda,idProfesor};
        String[] projections={DatabaseHelper.EVIDENCA_STUD};
        result = db.query(TABLE_NAME_EVIDENCA,projections,whereClause,whereArgs,null,null,null);
        return result;
    }

    public Cursor getEvidencenStudent(String _lenda,String _idProfesor,String _date,String _grupi)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor result;
        String whereClause = DatabaseHelper.EVIDENCA_LENDA + " = ? AND "+DatabaseHelper.EVIDENCA_DATA+" = ? AND "+DatabaseHelper.EVIDENCA_GRUPI+" = ? AND "  + DatabaseHelper.EVIDENCA_PROFID  + " = ?";
        String[] whereArgs = new String[]{_lenda,_date,_grupi,_idProfesor};
        String[] projections={EVIDENCA_ID,EVIDENCA_DATA ,EVIDENCA_STUD,EVIDENCA_TEMA,EVIDENCA_GRUPI,EVIDENCA_JAVA,EVIDENCA_PROFID,EVIDENCA_LENDA};
        result = db.query(TABLE_NAME_EVIDENCA,projections,whereClause,whereArgs,null,null,null);
        return result;
    }
}

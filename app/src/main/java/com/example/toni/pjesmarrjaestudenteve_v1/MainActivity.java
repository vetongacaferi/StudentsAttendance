package com.example.toni.pjesmarrjaestudenteve_v1;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Phaser;

public class MainActivity extends generatePDF{

    TextView _tvServeri,tvNumriStudentev;
    LinearLayout _lvServeri;

    String message = "";
    ServerSocket serverSocket;
    String strDate;

    ListView listaview_studentat;

    public ArrayList<String> array_listaStudentav;
    public ArrayList<String> array_listaMac;


    public int count;

    public ArrayAdapter mstudent_list;

    Button btnKrijoPdf;

    DatabaseHelper db;

    private File pdfFile;
    //final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    String[][] lista;
    String[] lista2;
    StringBuffer listaStudentav;
    int l=0;

    public static final String LENDA="_lendavalue";
    public static final String TEMA="_temavalue";
    public static final String GRUPI="_grupivalue";
    public static final String JAVA="java";

    String _lenda="";
    String _tema="";
    String _grupi="";
    String _java="";
    String _profesori="";

    String profesoriName="";

    static boolean permission_granted;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count=0;

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        strDate = dateFormat.format(date);

        Intent intenti=getIntent();

        date=new Date();
        date.getTime();

        _lenda=intenti.getStringExtra(LENDA);
        _tema=intenti.getStringExtra(TEMA);
        _grupi=intenti.getStringExtra(GRUPI);
        _java=intenti.getStringExtra(JAVA);
        _profesori=intenti.getStringExtra(profesori.idProfesor);


        lista=new String[2][100];
        lista2=new String[200];
        listaStudentav =new StringBuffer();

        array_listaStudentav = new ArrayList<>();
        array_listaMac=new ArrayList<>();

        _tvServeri=(TextView)findViewById(R.id.tvServeri);
        tvNumriStudentev=(TextView)findViewById(R.id.tvNumriStudentev);
        _lvServeri=(LinearLayout)findViewById(R.id.lnServeri);

        btnKrijoPdf=(Button)findViewById(R.id.btnKrijoPdf);


        try {
            db = new DatabaseHelper(this);
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,"Can't create database",Toast.LENGTH_LONG).show();
        }

        listaview_studentat=(ListView)findViewById(R.id.listv_studentat);
        array_listaStudentav=new ArrayList<String>();

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();

        try
        {
            Cursor kursori_emrimbiemri=db.getProfesoriName(_profesori);
            while(kursori_emrimbiemri.moveToNext())
            {
                profesoriName=kursori_emrimbiemri.getString(0)+" "+kursori_emrimbiemri.getString(1);
            }
        }
        catch (Exception ex)
        {
        }
        btnKrijoPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    //kontrollimi
                   if( checkForPermission()) {
                       Toast.makeText(MainActivity.this, "pati suksesaa", Toast.LENGTH_LONG).show();
                       createPdf(array_listaStudentav, new String[0],_lenda, _java, _grupi, _tema, strDate, profesoriName, R.drawable.logoup,1);
                       BackgroundTask background = new BackgroundTask(MainActivity.this);
                       background.doInBackground(BackgroundTask.addEvent, strDate, listaStudentav.toString(), _tema, _grupi, _java, _profesori, _lenda);
                       if (Build.VERSION.SDK_INT >= 24) {
                           try {
                               Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                               m.invoke(null);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                           previewPdf();
                       } else {
                           Toast.makeText(MainActivity.this, "nuk pati sukses", Toast.LENGTH_LONG).show();
                       }
                   }
                }
                catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                }
                catch (DocumentException ex)
                {
                    ex.printStackTrace();
                }
                catch(Exception ex)
                {
                    Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 9999;

        @Override
        public void run() {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean vlera=checkIP(getIpAddress());
                        if(vlera==true)
                        {
                            _lvServeri.setBackgroundResource(R.drawable.circle_shape_green);
                            _tvServeri.setText("Serveri hapur");
                            btnKrijoPdf.setClickable(true);
                        }
                        else
                        {
                            _tvServeri.setText("Serveri mbyllur");
                        }
                    }
                });

                while (true){
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    String messageFromClient = "";

                    messageFromClient = dataInputStream.readUTF();

                    String[] mesazhi_punuar=get_ID_MAC(messageFromClient);

                    String id=mesazhi_punuar[0];
                    String macAddresa=mesazhi_punuar[1];

                    array_listaMac.add(macAddresa);



                    //messageFromClient duhet te trajtohet
                    String[] checkedMessage=checkRecMessage(id);//kthehen 3 te dhena nese eshte ne rregull
                    if(checkedMessage[0]=="error")
                    {
                        message+="ska rezultat\n";
                    }
                    else
                    {
                        message+=checkedMessage[0]+" "+checkedMessage[1]+"\n";
                        array_listaStudentav.add(checkedMessage[0] + " " + checkedMessage[1]);
                        listaStudentav.append(checkedMessage[0]+",");
                        count++;
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mstudent_list = new listview_studentat(MainActivity.this, R.layout.listview_studentat, array_listaStudentav);
                            listaview_studentat.setAdapter(mstudent_list);
                            tvNumriStudentev.setText("Numri i studentev pjesmarres: "+Integer.toString(count));

                        }
                    });
                    //mesazhi qe i dergohet klientit
                    String msgReply ="succeeded";
                    dataOutputStream.writeUTF(msgReply);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                final String errMsg = e.toString();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     Toast.makeText(MainActivity.this,errMsg,Toast.LENGTH_LONG).show();
                    }
                });

            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip =inetAddress.getHostAddress();
                    }

                }

            }
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                 e.printStackTrace();
                 ip += "Something Wrong! " + e.toString() + "\n";
             }

        return ip;
    }

    private String[] checkRecMessage(String recievedMessage)
        {
            String[] lista=new String[2];
            Cursor kursori=db.getData(DatabaseHelper.TABLE_NAME_STUD);

            while(kursori.moveToNext())
            {
                if(recievedMessage.equals(kursori.getString(0)))
                {
                    lista[0]=recievedMessage;
                    lista[1]=kursori.getString(1);
                    return lista;

                }
                else
                {
                    lista[0]="error";
                }
            }
            return lista;
        }

    public String[] get_ID_MAC(String id_mac)
    {
        String[] ID_MAC=id_mac.split(",");
        return ID_MAC;
    }

    boolean checkIP(String ip)
    {
        boolean vlera=false;
        if(ip.startsWith("192.168.43.1"))
        {
            vlera=true;
        }
        return vlera;
    }

}

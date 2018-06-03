package com.example.toni.pjesmarrjaestudenteve_v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class studenti extends Activity {

    public static final String idStudent="abc";
    public static final String emriStudent="bcd";

    LinearLayout linlayout_Attend;
    TextView tvAttend;
    String _idStudentit;
    String _emriStudentit;

    String macAddresa;
    static boolean connecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_student);


       Intent intenti=getIntent();
        _idStudentit=intenti.getStringExtra(idStudent);
        _emriStudentit=intenti.getStringExtra(emriStudent);

        Toast.makeText(studenti.this,_idStudentit+" " +_emriStudentit,Toast.LENGTH_LONG).show();

        linlayout_Attend=(LinearLayout)findViewById(R.id.layout_attend);
        tvAttend=(TextView)findViewById(R.id.tvAttend);
        macAddresa=getMacAddress(studenti.this);
        connecting=false;
        linlayout_Attend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAttend.setText("duke tentuar");
                linlayout_Attend.setBackgroundResource(R.drawable.cilcle_shape_blue);
                final MyClientTask myClientTask = new MyClientTask("192.168.43.1",9999,_idStudentit+","+macAddresa);
                myClientTask.execute();

                new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(connecting==false)
                            {
                                Toast.makeText(studenti.this, "Deshtoj! Provo perseri", Toast.LENGTH_SHORT).show();
                                linlayout_Attend.setEnabled(true);
                                tvAttend.setText("Behu pjesëmarres");
                                linlayout_Attend.setBackgroundResource(R.drawable.circle_shape_red);
                                myClientTask.cancel(true);
                            }
                        }
                    }, 5000);
                }
        });
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }
        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if (msgToServer != null) {
                    dataOutputStream.writeUTF(msgToServer);
                }

                response = dataInputStream.readUTF();// here come answers from server(profesor)

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
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
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (response.equals("succeeded")) {
                tvAttend.setText("Je lidhur");
                linlayout_Attend.setBackgroundResource(R.drawable.circle_shape_green);
                linlayout_Attend.setClickable(false);
                connecting=true;
               // disconnect(studenti.this);
            }
        }
    }
    public String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Paisja nuk ka mac address ose wifi nuk eshte startuar";
        }
        return macAddress;
    }
    public void disconnect(Context context)
    {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wimanager.disconnect();
    }
}

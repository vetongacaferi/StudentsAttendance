package com.example.toni.pjesmarrjaestudenteve_v1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TONI on 5/29/2018.
 */

public class generatePDF extends AppCompatActivity {

    File pdfFile;
    public int l=0;


    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;


     boolean checkForPermission() throws FileNotFoundException,DocumentException{
         boolean vlera;
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return false;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return false;
        }else {
            vlera=true;
        }
         return vlera;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        checkForPermission();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL leja eshte e ndaluar", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     void createPdf(ArrayList<String> array_listaStudentav,String[] studnetListaPDF,String _lenda, String _java, String _grupi, String _tema, String strDate, String profesoriName, int _logo, int versioni) throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents/"+_lenda);
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        String[] lista2;
        String emri_pdf="java-"+_java+"_grupi-"+_grupi+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(),emri_pdf);



        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        // lista=array_listaStudentav.toArray(new String[0][0]);
        String[] array_ri=new String[100];
        if(versioni==1) {
            lista2 = array_listaStudentav.toArray(new String[0]);
            array_ri = ndajArray(lista2);
        } else if(versioni==2)
        {
             array_ri = studnetListaPDF;
        }

        //headeri

        Image image=null;
        try {
            Drawable d = getResources().getDrawable(_logo,null);
            BitmapDrawable bitDw = ((BitmapDrawable)d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(80,80);
        }
        catch (Exception ex)
        {

        }

        //headeri
        //logo universiteti
        PdfPTable tableImage = new PdfPTable(3);
        tableImage.setWidthPercentage(100);
        tableImage.setWidths(new int[]{2,1,2});
        tableImage.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableImage.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell foto=new PdfPCell(image);
        foto.setBorderColor(BaseColor.WHITE);
        tableImage.addCell("");
        tableImage.addCell(foto);
        tableImage.addCell("");
        PdfPTable textCenterTable = new PdfPTable(3);
        textCenterTable.setWidthPercentage(100);
        textCenterTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        textCenterTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        textCenterTable.setWidths(new int[]{1,2,1});
        textCenterTable.addCell("");
        textCenterTable.addCell("Universiteti i Prishtinës \"Hasan Prishtina\"");
        textCenterTable.addCell("");
        document.add(tableImage);
        document.add(textCenterTable);
        //headeri
        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(80);
        table1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.setWidths(new int[]{4,2});
        table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table1.addCell("Fakulteti i Inxhinierise  Elektrike dhe Kompjuterike");
        table1.addCell("Data: "+ strDate);
        table1.addCell("Departamenti: Inxhinieri Kompjuterike");
        table1.addCell("Lenda: "+_lenda);
        table1.addCell("Tema: "+_tema);
        table1.addCell("Grupi: "+_grupi);
        table1.addCell("");
        table1.addCell("");
        table1.addCell("");
        table1.addCell("");
        document.add(table1);
        //tabela
        int cell_length=array_ri.length*3;
        PdfPTable table=new PdfPTable(3);
        table.setWidths(new int[]{1,8,5});
        PdfPCell[] cell=new PdfPCell[cell_length];
        for(int i=0;i<array_ri.length;i++) {
            cell[l] = new PdfPCell(new Paragraph(array_ri[i]));
            l++;
        }

        PdfPCell cell_NR=new PdfPCell(new Paragraph("Nr."));
        cell_NR.setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPCell cell_EmriMbiemri=new PdfPCell(new Paragraph("Emri dhe mbiemri i studentit"));
        cell_EmriMbiemri.setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPCell cell_ID=new PdfPCell(new Paragraph("Nr.i indeksit/ID kartelës"));
        cell_ID.setBackgroundColor(BaseColor.LIGHT_GRAY);

        table.addCell(cell_NR);
        table.addCell(cell_EmriMbiemri);
        table.addCell(cell_ID);

        int nr=1;
        for (int i = 0; i < l; i++) {
            if (i%2==0)
            {
                table.addCell(new Paragraph(Integer.toString(nr)));
                nr++;
            }
            table.addCell(cell[i]);
        }

        document.add(table);

        //footer
        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(80);
        table2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.setWidths(new int[]{1,1});
        table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table2.addCell("");
        table2.addCell("");
        table2.addCell("");
        table2.addCell("");
        table2.addCell("Mësimdhënesi: "+ profesoriName);
        table2.addCell("Zyrtari/ja: per mësim: "+"......");
        document.add(table2);

        document.close();

    }

     void previewPdf() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        }else{
            Toast.makeText(this,"Shkarko nje PDF lexues te shikosh PDF-in e gjeneruar",Toast.LENGTH_SHORT).show();
        }
    }
    private String[] ndajArray(String[] array)
    {
        String[] stringu_array=new String[array.length*2];
        int k=0;
        for(int i=0;i<array.length;i++)
        {
            String[] ndarja=array[i].split(" ");
            stringu_array[k]=ndarja[1]+" "+ndarja[2];
            k++;
            stringu_array[k]=ndarja[0];
            k++;
        }
        return stringu_array;
    }
}

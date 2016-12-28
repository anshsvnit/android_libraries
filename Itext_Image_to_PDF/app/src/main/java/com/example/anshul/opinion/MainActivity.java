package com.example.anshul.opinion;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.Document;

import com.itextpdf.text.DocumentException;

import com.itextpdf.text.pdf.PdfWriter;


public class MainActivity extends AppCompatActivity{

    EditText name;

    File myFile;
    Button submit,button;
    public final int RESULT_LOAD_IMAGE = 1;
    public ArrayList<String> imagesPathList;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    com.itextpdf.text.Image img;

    ClipData.Item item;
    String imageEncoded;
    List<String> imagesEncodedList;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        button = (Button) findViewById(R.id.pdf);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);

                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    //Log.v("clip", mClipData.toString());
                    imagesEncodedList = new ArrayList<String>();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {


                        item = mClipData.getItemAt(i);
                        uri = item.getUri();
                        mArrayUri.add(uri);
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        Log.v("clip", imageEncoded);

                        imagesEncodedList.add(imageEncoded);
                        cursor.close();

                    }
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                }

            }
        }
    }


    private void createPdf() throws FileNotFoundException, DocumentException {

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfitextlib");
        Log.v("folderloc",pdfFolder.toString());

        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("folder", "Pdf Directory created");
        }

        String namepdf = name.getText().toString();
        myFile = new File(pdfFolder + namepdf + ".pdf");

        Log.v("file",myFile.toString());
        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document();

        PdfWriter.getInstance(document, output);


        document.open();
        try {
        for(int i=0;i<imagesEncodedList.size();i++) {
            img = com.itextpdf.text.Image.getInstance(imagesEncodedList.get(i));

            img.scalePercent(90f, 90f);
            img.setAbsolutePosition(0, 0);

            document.newPage();

            document.add(img);

        }

        document.close();}
        catch (Exception e) {
            Log.v("error",e.getMessage());
        }

    }


}

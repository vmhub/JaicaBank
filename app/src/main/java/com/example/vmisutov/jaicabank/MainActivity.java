package com.example.vmisutov.jaicabank;

import android.app.Activity;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private XmlPullParserFactory xmlFactoryObject;
    DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    Log.e("ecb", "test =2 ");
                    URL url = new URL(MainActivity.URL);
                    Log.e("ecb", "test =3 ");
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new InputSource(url.openStream()));
                    doc.getDocumentElement().normalize();

                    NodeList nodeList = doc.getElementsByTagName("Cube");

                    for (int i = 2; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        Element elm = (Element)node;

                    //    Log.e("ecb", "test date= " + elm.getAttribute("time"));
                        Log.e("ecb", "test curr= " + elm.getAttribute("currency"));
                        Log.e("ecb", "test rate= " + elm.getAttribute("rate"));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private void initDB() {
        dbHelper = new DbHelper(this);
        


    }


}








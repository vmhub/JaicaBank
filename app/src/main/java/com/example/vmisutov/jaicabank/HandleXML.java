package com.example.vmisutov.jaicabank;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andreyutkin on 14/05/15.
 */
public class HandleXML {

    private String urlString = null;
    public volatile boolean parsingComplete = true;
    private XmlPullParserFactory xmlFactoryObject;
    private ArrayList<String> currenciesList = new ArrayList<String>();
    private ArrayList<String> ratesList = new ArrayList<String>();

    public HandleXML(String url){
        this.urlString = url;
    }

    public ArrayList<String> getCurrenciesList(){
        return currenciesList;
    }

    public ArrayList<String> getRatesList(){
        return ratesList;
    }

    public void populateCurrencyList(XmlPullParser ecbParser) {

        try {

            int eventType = ecbParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType==XmlPullParser.START_TAG && ecbParser.getName().equals("Cube") &&
                        ecbParser.getAttributeCount()==2) {
                    currenciesList.add(ecbParser.getAttributeValue(0));
                    ratesList.add(ecbParser.getAttributeValue(1));
                }
                eventType = ecbParser.next();
            }
            parsingComplete = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)
                            url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
                            , false);
                    myparser.setInput(stream, null);
                    populateCurrencyList(myparser);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }
}

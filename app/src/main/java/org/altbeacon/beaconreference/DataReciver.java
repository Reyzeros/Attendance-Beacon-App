package org.altbeacon.beaconreference;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class DataReciver extends AsyncTask<Void,Void,Void> {
    Socket s;
    ServerSocket ss;
    BufferedReader be;
    InputStreamReader isr;
    static String message;

    @Override
    protected Void doInBackground(Void... voids) {


        try{
            ss=new ServerSocket(6000);
            s=ss.accept();
            isr=new InputStreamReader(s.getInputStream());
            be=new BufferedReader(isr);
            message=be.readLine();
            System.out.println(message+"HEHEREC");

        }catch (IOException e){
            e.printStackTrace();

        }
        return null;
    }

    public static String getMessage() {
        return message;
    }
}


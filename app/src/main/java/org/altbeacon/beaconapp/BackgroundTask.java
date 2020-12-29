package org.altbeacon.beaconapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class BackgroundTask extends AsyncTask<String,Void,Void> {
    private Socket s;
    private PrintWriter writer;

    @Override
    protected Void doInBackground(String... voids) {
        try
        {
            String message=voids[0];
            s=new Socket("192.168.1.48",6000);
            System.out.println(message+"HEHEGIVE");
            writer= new PrintWriter(s.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}

package com.stebanramos.appnodejs;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostUser extends AsyncTask<String, Void, String> {

    public static String postUserResponse = "";

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {

        String url_Api = params[0];
        String data = params[1];

        Log.i("d_funciones", "PostUser doInBackground Parametros Url_Api = " + url_Api);
        Log.i("d_funciones", "PostUser doInBackground Parametros Data = "  + data);

        try {

            URL url = new URL(url_Api);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            //httpURLConnection.setRequestProperty(Credentials.HEADER, header_value);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStream OS = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS));
            //String data = URLEncoder.encode("d", "UTF-8") + "=" + URLEncoder.encode(Data, "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();

            // Get the response code
            int statusCode = httpURLConnection.getResponseCode();
            InputStream inputStream;

            boolean error = false;

            //inputStream = httpURLConnection.getInputStream();

            if (statusCode >= 200 && statusCode < 400) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                //error from server
                inputStream = httpURLConnection.getErrorStream();
                Log.i("d_funciones","PostUser error from server statusCode " + statusCode);
                Log.i("d_funciones","PostUser error from server getRequestMethod " + httpURLConnection.getRequestMethod());
                Log.i("d_funciones","PostUser error from server getResponseMessage " + httpURLConnection.getResponseMessage());
                Log.i("d_funciones","PostUser error from server getErrorStream " + httpURLConnection.getErrorStream().toString());
                error = true;


            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            Log.i("d_funciones","PostUser Response " + response);
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            postUserResponse = response.toString();

            if (error) {
                postUserResponse = "Error de conexión Method " + httpURLConnection.getRequestMethod() + " Status " + statusCode + " " + httpURLConnection.getResponseMessage();
                //delegate.processFinish("ERROR");
                return null;
            }

            return postUserResponse;
        }catch (Exception e) {
            e.printStackTrace();
            postUserResponse = e.toString();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Log.i("d_funciones","PostUser onPostExecute response " + response);

        if (response == null){
            delegate.processFinish("ERROR");
        }else {
            delegate.processFinish("FINISHED");
        }
    }
}

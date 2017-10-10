package se.boreklev.copyherepastethere;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by matti on 2017-08-28.
 */

public class SendTask extends AsyncTask<SendTaskParameters, Object, Object> {
    private static final String TAG = "SendTask";

    @Override
    protected Object doInBackground(SendTaskParameters... param) {
        try {
            SendTaskParameters stp = param[0];
            CopyHerePasteThere.feedback(stp.getString());
            Socket s = new Socket(CopyHerePasteThere.getServer(), CopyHerePasteThere.getPort());
            // Outgoing stream redirect to socket
            OutputStream out = s.getOutputStream();
            PrintWriter output = new PrintWriter(out);
            // send type
            output.println(stp.getTypeAsString());
            output.flush();
            // Response
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader input = new BufferedReader(in);
            String answer = "";
            boolean b = true;
            while (b) {
                answer = input.readLine();
                if (answer.length() > 0)
                    b = false;
            }
            switch(stp.getType()) {
                case SendTaskParameters.TEXT:
                    output.print(stp.getString());
                    output.flush();
                    break;
                case SendTaskParameters.IMAGE:
                    byte[] bytes = stp.getBytes();
                    output.println(String.valueOf(bytes.length));
                    output.flush();
                    while (b) {
                        answer = input.readLine();
                        if (answer.length() > 0)
                            b = false;
                    }
                    DataOutputStream dos = new DataOutputStream(out);
                    dos.write(bytes);
                    dos.flush();
                    break;
            }
            // receive ok if all is ok, ignored atm
            b = true;
            while (b) {
                answer = input.readLine();
                if (answer.length() > 0)
                    b = false;
            }
            //Close connection
            out.close();
            s.close();
        } catch(final Exception e) {
            Log.e(TAG, e.toString(), e);
        }
        return null;
    }
}
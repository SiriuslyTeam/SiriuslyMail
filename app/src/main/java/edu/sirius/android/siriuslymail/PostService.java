package edu.sirius.android.siriuslymail;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostService extends Service {
    private final ExecutorService pool = Executors.newFixedThreadPool(1);

    private final IBinder mBinder = new LocalBinder();
    public PostService() {
    }

    public void getPost(String email,String pass,String host,String folder){
        ImapTask task = new ImapTask(email, pass, host);
        task.execute();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class LocalBinder extends Binder {

        PostService getService() {

            return PostService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    class ImapTask extends AsyncTask<Void,Void,ArrayList<Message>> {
        private final String email;
        private final String password;
        private final String host;

        @Override
        protected ArrayList<Message> doInBackground(Void... voids) {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            Store store= null;
            try {
                store = session.getStore();
                store.connect(host,email,password);
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            try {
                Folder inbox= store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                return new ArrayList<>(Arrays.asList(inbox.getMessages()));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Message> messages) {
            super.onPostExecute(messages);
            int a =5;
        }

        ImapTask(String email, String password, String host){
            this.email = email;
            this.password = password;
            this.host = host;
        }
    }

}

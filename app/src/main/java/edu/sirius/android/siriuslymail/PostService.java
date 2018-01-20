package edu.sirius.android.siriuslymail;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostService extends Service {
    private final ExecutorService pool = Executors.newFixedThreadPool(1);

    private String INTENT_NEW_MESSAGES = "NEW_MESSAGES";

    private final IBinder mBinder = new LocalBinder();
    public PostService() {
    }

    public void getPost(String email,String pass,String host,String folder){
        ImapTask task = new ImapTask(email, pass, host);
        task.execute();
    }

    public void sendMessage(String emailFrom,String emailTo,String password,String host,String subject,String body){
        SmtpTask task=new SmtpTask(emailFrom,password,emailTo,host,subject,body);
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


    class SmtpTask extends AsyncTask<Void,Void,Boolean>{
        SmtpTask(String emailFrom, String password, String emailTo, String host, String subject, String body){
            this.emailFrom = emailFrom;
            this.password = password;
            this.emailTo = emailTo;
            this.host = host;
            this.subject = subject;
            this.body = body;
        }

        private  final String emailFrom;
        private  final String password;
        private final String emailTo;
        private final String host;
        private final String subject;
        private final String body;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Properties props=new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props);

            try {
                // Получение объекта транспорта для передачи электронного сообщения
                Transport bus = session.getTransport("smtp");

                // Устанавливаем соединение один раз
                // Метод Transport.send() отсоединяется после каждой отправки
                //bus.connect();
                // Обычно для SMTP сервера необходимо указать логин и пароль
                bus.connect(host, emailFrom, password);

                // Создание объекта сообщения
                Message msg = new MimeMessage(session);

                // Установка атрибутов сообщения
                msg.setFrom(new InternetAddress(emailFrom));
                InternetAddress[] address = {new InternetAddress(emailTo)};
                msg.setRecipients(Message.RecipientType.TO, address);

                msg.setSubject(subject);
                msg.setSentDate(new Date());

                // Установка контента сообщения и отправка
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setContent(body, "text/plain");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textPart);
                msg.setContent(multipart);

                msg.saveChanges();
                bus.sendMessage(msg, address);

                bus.close();
                return true;

            }
            catch (MessagingException mex) {
                // Печать информации обо всех возможных возникших исключениях
                mex.printStackTrace();
                // Получение вложенного исключения
                while (mex.getNextException() != null) {
                    // Получение следующего исключения в цепочке
                    Exception ex = mex.getNextException();
                    ex.printStackTrace();
                    if (!(ex instanceof MessagingException)) break;
                    else mex = (MessagingException)ex;
                }
                return  false;
            }

        }
    }

    class ImapTask extends AsyncTask<Void,Void,Boolean> {
        private final String email;
        private final String password;
        private final String host;

        @Override
        protected Boolean doInBackground(Void... voids) {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            Store store= null;
            try {
                store = session.getStore();
                store.connect(host,email,password);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            try {
                Folder inbox= store.getFolder("INBOX"); //TODO folder
                inbox.open(Folder.READ_ONLY);

                ArrayList<edu.sirius.android.siriuslymail.Message> messages = new ArrayList<>();

                Message[] msgs = inbox.getMessages();

                for (Message msg : msgs) {
                    edu.sirius.android.siriuslymail.Message m = new edu.sirius.android.siriuslymail.Message();
                    m.from = msg.getFrom()[0].toString();
                    m.to = msg.getAllRecipients()[0].toString();
                    m.subject = msg.getSubject();
                    m.body = msg.getContent().toString(); //TODO MultipartMIME
                    m.folder = "INBOX"; // TODO folder

                    messages.add(m);
                }

                DataBaseHelper.insertMany(PostService.this, messages);

            } catch (MessagingException | IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Intent intent = new Intent(INTENT_NEW_MESSAGES);
                LocalBroadcastManager.getInstance(PostService.this).sendBroadcast(intent);
            }
        }

        ImapTask(String email, String password, String host){
            this.email = email;
            this.password = password;
            this.host = host;
        }
    }

}

package edu.sirius.android.siriuslymail;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static edu.sirius.android.siriuslymail.IntentConstants.EMAIL;
import static edu.sirius.android.siriuslymail.IntentConstants.FOLDER;
import static edu.sirius.android.siriuslymail.IntentConstants.HOST;
import static edu.sirius.android.siriuslymail.IntentConstants.PASSWORD;
import static edu.sirius.android.siriuslymail.PostServiceActions.GET_MESSAGES_ACTION;
import static edu.sirius.android.siriuslymail.PostServiceActions.LOGIN_ACTION;

public class PostService extends IntentService {

    public static final String SUCCESS_LOGIN = "IS_SUCCESS";
    public static final String SUCCESS_LOAD_MESSAGES = "SUCCESS_LOAD_MESSAGES";


    public PostService() {
        this(null);
    }

    public PostService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        switch (intent.getAction()) {
            case GET_MESSAGES_ACTION:
                loadMessages(intent);
                break;
            case LOGIN_ACTION:
                loggin(intent);
                break;
        }
    }

    private void loggin(Intent intent) {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);

        boolean isSuccess;
        try {
            Store store = session.getStore();
            store.connect(intent.getStringExtra(HOST), intent.getStringExtra(EMAIL), intent.getStringExtra(PASSWORD));
            isSuccess = true;
            User.getInstance().update(intent.getStringExtra(EMAIL), intent.getStringExtra(PASSWORD), intent.getStringExtra(HOST));
        } catch (MessagingException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        intent = new Intent(LOGIN_ACTION);
        intent.putExtra(SUCCESS_LOGIN, isSuccess);
        LocalBroadcastManager.getInstance(PostService.this).sendBroadcast(intent);
    }

    private void loadMessages(Intent intent) {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);
        Store store;
        boolean isSuccess;
        try {
            store = session.getStore();
            store.connect(User.getInstance().getHost(), User.getInstance().getEmail(), User.getInstance().getPassword());
            Folder inbox = store.getFolder(intent.getStringExtra(FOLDER)); //TODO folder
            inbox.open(Folder.READ_ONLY);

            List<edu.sirius.android.siriuslymail.Message> messages = new ArrayList<>();

            Message[] msgs = inbox.getMessages();
            int quantityAlreadyDownload = 0;


            for (int msgsIndex = 10 - 1; msgsIndex >= 0; --msgsIndex) {
                Message msg = msgs[msgsIndex];
                edu.sirius.android.siriuslymail.Message m = new edu.sirius.android.siriuslymail.Message();
                m.from = msg.getFrom()[0].toString();
                m.to = msg.getAllRecipients()[0].toString();
                m.subject = msg.getSubject();
                m.body = msg.getContent().toString(); //TODO MultipartMIME
                m.folder = intent.getStringExtra(FOLDER);

                messages.add(m);
//                    quantityAlreadyDownload++;
//                    if (quantityAlreadyDownload == quantityMessagesToDownload)
//                        break;

            }

            DataSource.getInstance().saveMessages(messages);
            isSuccess = true;
        } catch (MessagingException | IOException e) {
            User.getInstance().clear();
            e.printStackTrace();
            isSuccess = false;
        }
        intent = new Intent(GET_MESSAGES_ACTION);
        intent.putExtra(SUCCESS_LOAD_MESSAGES, isSuccess);
        LocalBroadcastManager.getInstance(PostService.this).sendBroadcast(intent);
    }

    class SmtpTask extends AsyncTask<Void, Void, Boolean> {
        SmtpTask(String emailFrom, String password, String emailTo, String host, String subject, String body) {
            this.emailFrom = emailFrom;
            this.password = password;
            this.emailTo = emailTo;
            this.host = host;
            this.subject = subject;
            this.body = body;
        }

        private final String emailFrom;
        private final String password;
        private final String emailTo;
        private final String host;
        private final String subject;
        private final String body;

        @Override
        protected Boolean doInBackground(Void... voids) {
            Properties props = new Properties();
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

            } catch (MessagingException mex) {
                // Печать информации обо всех возможных возникших исключениях
                mex.printStackTrace();
                // Получение вложенного исключения
                while (mex.getNextException() != null) {
                    // Получение следующего исключения в цепочке
                    Exception ex = mex.getNextException();
                    ex.printStackTrace();
                    if (!(ex instanceof MessagingException)) break;
                    else mex = (MessagingException) ex;
                }
                return false;
            }

        }
    }
}

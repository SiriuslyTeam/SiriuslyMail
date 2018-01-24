package edu.sirius.android.siriuslymail;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
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
import static edu.sirius.android.siriuslymail.IntentConstants.IMAP_HOST;
import static edu.sirius.android.siriuslymail.IntentConstants.PASSWORD;
import static edu.sirius.android.siriuslymail.IntentConstants.SMTP_HOST;
import static edu.sirius.android.siriuslymail.PostServiceActions.GET_FOLDERS;
import static edu.sirius.android.siriuslymail.PostServiceActions.GET_MESSAGES_ACTION;
import static edu.sirius.android.siriuslymail.PostServiceActions.LOGIN_ACTION;
import static edu.sirius.android.siriuslymail.PostServiceActions.POST_MESSAGE;

public class PostService extends IntentService {

    public static final String SUCCESS_LOGIN = "IS_SUCCESS";
    public static final String SUCCESS_LOAD_MESSAGES = "SUCCESS_LOAD_MESSAGES";
    public static final String SUCCESS_SEND = "SUCCESS_SEND";


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
                login(intent);
                break;
            case POST_MESSAGE:
                postMessage(intent);
                break;
            case GET_FOLDERS:
                getFoldersForUser();
                break;

        }
    }

    private void login(Intent intent) {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);

        boolean isSuccess;
        try {
            Store store = session.getStore();
            store.connect(intent.getStringExtra(IMAP_HOST), intent.getStringExtra(EMAIL), intent.getStringExtra(PASSWORD));
            UsersManager.getInstance().saveUser(new User(intent.getStringExtra(EMAIL), intent.getStringExtra(PASSWORD), intent.getStringExtra(IMAP_HOST), intent.getStringExtra(SMTP_HOST), (long) 0));
            UsersManager.getInstance().updateActiveUser(intent.getStringExtra(EMAIL));
            isSuccess = true;
        } catch (MessagingException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        intent = new Intent(LOGIN_ACTION);
        intent.putExtra(SUCCESS_LOGIN, isSuccess);
        LocalBroadcastManager.getInstance(PostService.this).sendBroadcast(intent);
    }

    private void getFoldersForUser() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);
        boolean isSuccess;
        try {
            Store store = session.getStore();
            store.connect(UsersManager.getInstance().getActiveUser().getImapHost(), UsersManager.getInstance().getActiveUser().getEmail(), UsersManager.getInstance().getActiveUser().getPassword());
            javax.mail.Folder[] folders1 = store.getDefaultFolder().list("*");
            for (Folder i : folders1) {
                DataBaseHelperFolder.getInstance(this).insertFolder(PostService.this, i, UsersManager.getInstance().getActiveUser().getEmail());
            }
            isSuccess = true;
        } catch (MessagingException e) {
            e.printStackTrace();
            isSuccess = false;
        }

        Intent intent = new Intent(GET_FOLDERS);
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
            User user = UsersManager.getInstance().getActiveUser();
            store.connect(user.getImapHost(), user.getEmail(), user.getPassword());
            Folder inbox = store.getFolder(intent.getStringExtra(FOLDER)); //TODO folder
            inbox.open(Folder.READ_ONLY);

            List<edu.sirius.android.siriuslymail.Message> messages = new ArrayList<>();

            Message[] msgs = inbox.getMessages();
            int quantityMessages = inbox.getMessageCount();

            for (int msgsIndex = quantityMessages - 1; msgsIndex >= Math.max(quantityMessages - 10, 0); --msgsIndex) {
                Message msg = msgs[msgsIndex];
                edu.sirius.android.siriuslymail.Message m = new edu.sirius.android.siriuslymail.Message();
                m.from = msg.getFrom()[0].toString();
                if (msg.getAllRecipients() != null) {
                    m.to = msg.getAllRecipients()[0].toString();
                }
                m.subject = msg.getSubject();
                m.body = getTextFromMessage(msg); //TODO MultipartMIME
                m.folder = intent.getStringExtra(FOLDER);

                messages.add(m);
//                    quantityAlreadyDownload++;
//                    if (quantityAlreadyDownload == quantityMessagesToDownload)
//                        break;

            }
            DataSource.getInstance().clearMessages(intent.getStringExtra(FOLDER));
            DataSource.getInstance().saveMessages(messages);
            isSuccess = true;
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        intent = new Intent(GET_MESSAGES_ACTION);
        intent.putExtra(SUCCESS_LOAD_MESSAGES, isSuccess);
        LocalBroadcastManager.getInstance(PostService.this).sendBroadcast(intent);
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        } else {
            result = message.getContent().toString();
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                result = (String) bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }


    private void postMessage(Intent intent) {
        edu.sirius.android.siriuslymail.Message message = (edu.sirius.android.siriuslymail.Message) intent.getSerializableExtra("MESSAGE");
        Properties props = new Properties();
        User user = UsersManager.getInstance().getActiveUser();
        String host = user.getSmtpHost();
        String emailTo = message.to;
        String body = message.body;
        String subject = message.subject;
        String emailFrom = user.getEmail();
        String password = user.getPassword();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);
        boolean bool;
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
            bool = true;


        } catch (MessagingException mex) {
            // Печать информации обо всех возможных возникших исключениях
            mex.printStackTrace();
            // Получение вложенного исключения
            bool = false;

        }
        Intent intent1 = new Intent(PostService.this, PostServiceActions.class)
                .setAction(POST_MESSAGE)
                .putExtra("IS_SUCCESS", bool);
        LocalBroadcastManager.getInstance(PostService.this).sendBroadcast(intent1);
    }
}

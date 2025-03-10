package me.sofiavicedomini.mboxconverter.services;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.SharedByteArrayInputStream;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RequiredArgsConstructor
public class MboxConversionService {

    private final Logger logger;


    public List<MimeMessage> readMbox(String mboxPath) throws Exception {
        List<MimeMessage> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(mboxPath))) {
            StringBuilder emailContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("From ")) {
                    if (!emailContent.isEmpty()) {
                        messages.add(parseMessage(emailContent.toString()));
                        emailContent.setLength(0);
                    }
                }
                emailContent.append(line).append("\n");
            }
            if (!emailContent.isEmpty()) {
                messages.add(parseMessage(emailContent.toString()));
            }
        }
        return messages;
    }

    private MimeMessage parseMessage(String email) throws Exception {
        InputStream is = new SharedByteArrayInputStream(email.getBytes());
        Session session = Session.getDefaultInstance(new Properties());
        var msg = new MimeMessage(session, is);
        logger.debug(String.format("Found message: %s, from: %s, ID: %s", msg.getSubject(), msg.getSender(), msg.getMessageID()));
        return msg;
    }

    void convertToMsg(MimeMessage message, String outputDir) throws Exception {
        String subject = message.getSubject().replaceAll("[^a-zA-Z0-9]", "_");
        File msgFile = new File(outputDir, "email_"
                + message.getSentDate().toInstant().atZone(ZoneId.of("GMT"))
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replaceAll("[^a-zA-Z0-9]", "_")
                + '_' + subject + ".msg");

        try (FileOutputStream fos = new FileOutputStream(msgFile)) {
            message.writeTo(fos);
        }
        logger.info("Converted: " + msgFile.getAbsolutePath());
    }

    public Task<File> convertMboxToMSGList(final String mboxFilePath, final String msgListFolder) {
        final var mboxService = this;
        return new Task<File>() {
            Long total = 0L;

            @Override
            protected File call() throws Exception {

                var mimeMessages = mboxService.readMbox(mboxFilePath);

                total = (long) mimeMessages.size();
                updateMessage(String.format("Found %d messages in MBOX.", total));
                updateProgress(0, total);

                for (int i = 0; i < mimeMessages.size(); i++) {
                    var message = mimeMessages.get(i);
                    try {
                        mboxService.convertToMsg(message, msgListFolder);
                        updateProgress(i + 1, total);
                        updateMessage(String.format("Converted: %d of %d", i + 1, total));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(String.format("Error converting message: %s", message.getSubject()));
                        updateMessage(String.format("Error converting message: %s", message));
                    }
                }

                return new File(msgListFolder);
            }
        };
    }
}

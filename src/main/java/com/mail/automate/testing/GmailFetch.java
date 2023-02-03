package com.mail.automate.testing;

import org.apache.commons.lang3.ArrayUtils;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.util.*;

public class GmailFetch {

    public static void main( String[] args ) throws Exception {

        Session session = Session.getDefaultInstance(new Properties( ));
        Store store = session.getStore("imaps");
        store.connect("imap.googlemail.com", 993, "info@conflowence.com", "womqzxzpzkskfdhw");
        Folder inbox = store.getFolder( "INBOX" );
        inbox.open( Folder.READ_WRITE);

        String saveDirectory = "C:\\Mythili";

        Message[] messages = inbox.getMessages();
        ArrayUtils.reverse(messages); //add common.lang dependency to use ArrayUtils

        //iterate over each messages in array, given n as no of messages
        for (int i = 0, n = 10; i < n; i++) {
            Message message = messages[i];
            Address[] fromAddress = message.getFrom();
            String from = fromAddress[0].toString();
            String subject = message.getSubject();
            String sentDate = message.getSentDate().toString();
            String contentType = message.getContentType();
            String messageContent = "";
            String attachFiles = "";

            if (contentType.contains("multipart") && subject.contains("TimeSheet approval Request")) {
                // content may contain attachments
                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        // this part is attachment
                        String fileName = part.getFileName();
                        attachFiles += fileName + ", ";
                        part.saveFile(saveDirectory + File.separator + fileName);
                        System.out.println("Message #" + (i + 1) + ":");
                        System.out.println("\t From: " + from);
                        System.out.println("\t Subject: " + subject);
                        System.out.println("\t Sent Date: " + sentDate);
                        System.out.println("\t Message: " + messageContent);
                        System.out.println("\t Attachments: " + attachFiles);
                    }
                }

            }
        }
        // disconnect
        inbox.close(false);
        store.close();
    }
}
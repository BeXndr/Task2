package com.example.task2;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public void mandarEmail(String address, String title, String read) {
        // Habilitar o protocolo correto globalmente (faça isso antes de qualquer outra coisa)
        // Isso força o uso do TLSv1.2 que é compatível com as versões recentes do Gmail
        System.setProperty("https.protocols", "TLSv1.2");
        
        // Credenciais
        final String username = "be.schneidr@gmail.com";
        // Cuidado: Use uma senha de app do Google, não sua senha normal!
        final String password = "zygt qtcl echc qafw"; 
        
        // Configurações do servidor SMTP do Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // Mudando para porta 587 (STARTTLS)
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        
        // Definições específicas para TLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        // Remova ou comente estas linhas que causam conflito
        // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // props.put("mail.smtp.socketFactory.port", "465");
        
        try {
            // Criar sessão com autenticação
            Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            
            // Ativar debug para ver detalhes da comunicação SMTP
            session.setDebug(true);
            
            // Criar mensagem
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
            msg.setSubject(title);
            msg.setText(read);
            
            // Enviar mensagem
            Transport.send(msg);
            
            System.out.println("E-mail enviado com sucesso!");
            
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
            
            // Mostrar todas as causas para melhor diagnóstico
            Throwable cause = e;
            while (cause != null) {
                System.err.println("Causa: " + cause.getClass().getName() + ": " + cause.getMessage());
                cause = cause.getCause();
            }
        }
    }
}

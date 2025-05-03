package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.Notification;
import com.pmu.pmudemo.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.text.MessageFormat;
import java.util.Locale;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepo;

 

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final MessageSource messageSource;

    @Value("${notification.sendinblue.api-key}")
    private String sendinblueApiKey;
    
    @Value("${notification.sendinblue.sender}")
    private String sendinblueSender;

    @Value("${notification.twilio.account-sid}")
    private String twilioSid;
    @Value("${notification.twilio.auth-token}")
    private String twilioToken;
    @Value("${notification.twilio.from}")
    private String twilioFrom;

    public NotificationService(NotificationRepository notificationRepo,  MessageSource messageSource) {
        this.notificationRepo = notificationRepo;
        this.messageSource = messageSource;
    }

    public void sendNotification(Long userId, String type, String message) {
        Notification notif = new Notification();
        notif.setUser(null); // à compléter selon besoin
        notif.setType(type);
        notif.setMessage(message);
        notif.setDateEnvoi(LocalDateTime.now());
        notif.setStatut("ENVOYEE");
        notificationRepo.save(notif);
        logger.info("Notification in-app envoyée à userId={} : {}", userId, message);
        System.out.println("Notification envoyée : " + message);
    }

 

    // Envoi d'email via Sendinblue
    public void sendEmailSendinblue(String to, String subject, String content) {
        String url = "https://api.sendinblue.com/v3/smtp/email";
        Map<String, Object> body = new HashMap<>();
        Map<String, String> sender = new HashMap<>();
        sender.put("email", sendinblueSender);
        body.put("sender", sender);
        body.put("to", new Map[]{Map.of("email", to)});
        body.put("subject", subject);
        body.put("htmlContent", content);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", sendinblueApiKey);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println("Sendinblue email status: " + response.getStatusCode());
        logger.info("Email Sendinblue envoyé à {} (statut: {})", to, response.getStatusCode());
    }

    // Envoi de SMS via Twilio
    public void sendSmsTwilio(String to, String content) {
        Twilio.init(twilioSid, twilioToken);
        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioFrom),
                content
        ).create();
        System.out.println("Twilio SMS SID: " + message.getSid());
        logger.info("SMS Twilio envoyé à {} (SID: {})", to, message.getSid());
    }

    // --- Templates personnalisés avec i18n ---
    public String buildRechargeEmail(String nom, Double montant, String operateur, String numero) {
        Locale locale = LocaleContextHolder.getLocale();
        String received = messageSource.getMessage("recharge.request.received", new Object[]{montant, operateur, numero}, locale);
        String confirm = messageSource.getMessage("recharge.request.confirm", null, locale);
        String historiqueLink = "<a href='https://chargili.com/mon-compte/recharges'>" + messageSource.getMessage("history.link", null, locale) + "</a>";
        String supportLink = "<a href='https://chargili.com/support'>" + messageSource.getMessage("support.link", null, locale) + "</a>";
        return "<h2>Bonjour " + nom + ",</h2>"
                + "<p>" + received + "</p>"
                + "<p>" + confirm + "</p>"
                + historiqueLink + " | " + supportLink;
    }

    public String buildRechargeSuccessEmail(String nom, Double montant, String operateur, String numero, String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String success = messageSource.getMessage("recharge.success", new Object[]{montant, operateur, numero}, locale);
        String codeMsg = code != null ? "<p>" + messageSource.getMessage("recharge.code", new Object[]{code}, locale) + "</p>" : "";
        String historiqueLink = "<a href='https://chargili.com/mon-compte/recharges'>" + messageSource.getMessage("history.link", null, locale) + "</a>";
        String supportLink = "<a href='https://chargili.com/support'>" + messageSource.getMessage("support.link", null, locale) + "</a>";
        return "<h2>Bonjour " + nom + ",</h2>"
                + "<p>" + success + "</p>"
                + codeMsg
                + historiqueLink + " | " + supportLink;
    }

    public String buildRechargeFailureEmail(String nom, Double montant, String operateur, String numero) {
        Locale locale = LocaleContextHolder.getLocale();
        String failure = messageSource.getMessage("recharge.failure", new Object[]{montant, operateur, numero}, locale);
        String tryAgain = messageSource.getMessage("recharge.tryagain", null, locale);
        String supportLink = "<a href='https://chargili.com/support'>" + messageSource.getMessage("support.link", null, locale) + "</a>";
        return "<h2>Bonjour " + nom + ",</h2>"
                + "<p>" + failure + "</p>"
                + "<p>" + tryAgain + "</p>"
                + supportLink;
    }

    public String buildRechargeSms(String montant, String operateur) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("recharge.sms.request", new Object[]{montant, operateur}, locale);
    }

    public String buildRechargeSuccessSms(String montant, String operateur, String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String codeMsg = code != null ? messageSource.getMessage("recharge.code", new Object[]{code}, locale) : "";
        return messageSource.getMessage("recharge.sms.success", new Object[]{montant, operateur, codeMsg}, locale);
    }

    public String buildRechargeFailureSms(String montant, String operateur) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("recharge.sms.failure", new Object[]{montant, operateur}, locale);
    }

    // Préparation pour le multilingue (exemple simplifié)
    public String getMessage(String key, String lang) {
        // À remplacer par une vraie gestion i18n (fichiers de messages, etc.)
        if ("fr".equals(lang)) {
            if ("recharge.success".equals(key)) return "Votre recharge a été validée.";
            if ("recharge.failure".equals(key)) return "Votre recharge a échoué.";
        } else if ("en".equals(lang)) {
            if ("recharge.success".equals(key)) return "Your top-up was successful.";
            if ("recharge.failure".equals(key)) return "Your top-up failed.";
        }
        return key;
    }
} 
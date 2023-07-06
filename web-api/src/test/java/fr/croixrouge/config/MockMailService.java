package fr.croixrouge.config;

import fr.croixrouge.service.MailService;
import jakarta.mail.MessagingException;

public class MockMailService extends MailService {

    public MockMailService() {
        super(null);
    }

    @Override
    public String generateToken() {
        return "token";
    }

    @Override
    public void sendEmailFromTemplate(String email, String token) throws MessagingException {

    }

}

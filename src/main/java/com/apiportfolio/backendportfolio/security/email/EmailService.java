package com.apiportfolio.backendportfolio.security.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 
     * 
     * @param toEmail
     * @param token
     */
    public void sendRecoveryEmail(String toEmail, String token) {
        try {
            String subject = "Recuperación de contraseña";
            String resetLink = "https://moneylender-chi.vercel.app/ResetPassword?token=" + token;
            String body = "Hola,\n\n"
                    + "Has solicitado restablecer tu contraseña. "
                    + "Haz clic en el siguiente enlace para continuar:\n\n"
                    + resetLink + "\n\n"
                    + "Si no solicitaste esto, puedes ignorar este correo.\n\n"
                    + "Gracias,\n"
                    + "El equipo de MoneyLender";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("sergioco2807@gmail.com");

            mailSender.send(message);
            System.out.println("Correo de recuperación enviado a " + toEmail);

        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}

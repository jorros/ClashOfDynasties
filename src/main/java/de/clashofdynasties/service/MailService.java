package de.clashofdynasties.service;

import de.clashofdynasties.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendMail(Player player, String subject, String content) {
        try {
            mailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws MessagingException {
                    if (player.getEmail() != null && !player.getEmail().isEmpty()) {
                        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        message.setFrom("info@clashofdynasties.de");
                        message.setTo(player.getEmail());
                        message.setSubject("Clash of Dynasties: " + subject);

                        String text = "<html>";
                        text += "<head>";
                        text += "</head>";
                        text += "<body>";
                        text += "<p>Hallo " + player.getName() + ".</p>";
                        text += "<p>" + content + "</p>";
                        text += "<p style=\"font-size:11px; margin-top:15px;\">Wenn du in Zukunft diese Mail nicht mehr bekommen möchtest, log dich in das Spiel ein und entferne das jeweilige H&auml;ckchen unter Profil!</p>";
                        text += "</body>";
                        text += "</html>";

                        message.setText(text, true);
                    }
                }
            });
        }
        catch(Exception ignored) {

        }
    }
}

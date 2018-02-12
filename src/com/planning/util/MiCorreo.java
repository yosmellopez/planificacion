package com.planning.util;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MiCorreo {

    private String[] to;

    private String from;

    private String personal = "";

    private String asunto;

    private String mensaje;

    private JavaMailSenderImpl sender;

    private HashMap<String, ClassPathResource> archivos = new HashMap<>();

    private HashMap<String, ClassPathResource> imagenes = new HashMap<>();

    public void sendMail() {
        sender.send((MimeMessage mimeMessage) -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(asunto);
            helper.setText(mensaje, true);
            if (personal.isEmpty()) {
                helper.setFrom(from);
            } else {
                helper.setFrom(from, personal);
            }
            for (Map.Entry<String, ClassPathResource> entry : archivos.entrySet()) {
                String key = entry.getKey();
                ClassPathResource resource = entry.getValue();
                helper.addAttachment(key, resource);
            }
            for (Map.Entry<String, ClassPathResource> entry : imagenes.entrySet()) {
                String key = entry.getKey();
                ClassPathResource resource = entry.getValue();
                helper.addInline(key, resource);
            }
        });
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public void setTo(String to) {
        this.to = new String[]{to};
    }

    public JavaMailSenderImpl getSender() {
        return sender;
    }

    public void setSender(JavaMailSenderImpl sender) {
        this.sender = sender;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public HashMap<String, ClassPathResource> getArchivos() {
        return archivos;
    }

    public void setArchivos(HashMap<String, ClassPathResource> archivos) {
        this.archivos = archivos;
    }

    public HashMap<String, ClassPathResource> getImagenes() {
        return imagenes;
    }

    public void setImagenes(HashMap<String, ClassPathResource> imagenes) {
        this.imagenes = imagenes;
    }
}

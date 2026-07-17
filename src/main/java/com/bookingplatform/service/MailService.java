package com.bookingplatform.service;

import lombok.RequiredArgsConstructor;



import org.springframework.mail
        .javamail.JavaMailSender;

import org.springframework.mail
        .SimpleMailMessage;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final
    JavaMailSender mailSender;

    public void sendResetEmail(

            String to,
            String token

    ){

        String resetUrl =

                "https://bookify-nine-virid.vercel.app/reset-password?token="
                        + token;

        SimpleMailMessage mail =
                new SimpleMailMessage();

        mail.setTo(to);

        mail.setSubject(
                "Password Reset Request"
        );

        mail.setText(

                "Click link to reset password:\n"
                        + resetUrl

        );

        mailSender.send(mail);
    }
}

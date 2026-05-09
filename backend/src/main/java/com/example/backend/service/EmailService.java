package com.example.backend.service;

import com.example.backend.entity.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendBookingConfirmationEmail(Booking booking) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("noreply@demomailtrap.co"));
        message.setRecipients(MimeMessage.RecipientType.TO, booking.getUser().getEmail());
        message.setSubject("Booking Confirmation - " + booking.getTrain().getTrainNumber());
        message.setText("""
                Dear %s,
                
                Your booking has been confirmed!
                
                Train:          %s
                From:           %s
                To:             %s
                Travel Date:    %s
                Seats Booked:   %d
                Booking Date:   %s
                
                Thank you for choosing our service!
                """
                .formatted(
                        booking.getUser().getEmail(),
                        booking.getTrain().getTrainNumber(),
                        booking.getStartStation().getName(),
                        booking.getEndStation().getName(),
                        booking.getTravelDate(),
                        booking.getSeatsBooked(),
                        booking.getBookingDate()
                ));

        mailSender.send(message);
    }

    @Async
    public void sendDelayNotificationEmail(Booking booking, int delayMinutes) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("noreply@trainticketing.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, booking.getUser().getEmail());
        message.setSubject("Delay Notice - " + booking.getTrain().getTrainNumber());
        message.setText("""
                Dear %s,
                
                We regret to inform you that train %s is delayed by %d minutes.
                
                We apologize for the inconvenience.
                """
                .formatted(
                        booking.getUser().getEmail(),
                        booking.getTrain().getTrainNumber(),
                        delayMinutes
                ));

        mailSender.send(message);
    }
}

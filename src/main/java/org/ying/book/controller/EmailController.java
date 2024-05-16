package org.ying.book.controller;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.ying.book.dto.email.EmailValidationDto;
import org.ying.book.pojo.Book;
import org.ying.book.service.EmailService;

@RestController
@RequestMapping(value = "/email")
public class EmailController {

    @Resource
    private EmailService emailService;
    @PostMapping("/validation-code")
    public void sendValidationCodeForUser(@RequestBody EmailValidationDto emailValidationDto) throws MessagingException {
        emailService.sendVerificationEmail(emailValidationDto);
    }

    @PostMapping("/invitation-code")
    public void sendInvitationCodeForBookAdmin(@RequestBody EmailValidationDto emailValidationDto) throws MessagingException {
        emailService.sendInvitationEmail(emailValidationDto);
    }
}

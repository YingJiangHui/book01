package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncodeService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encode(String password){
        return passwordEncoder.encode(password);
    }
}

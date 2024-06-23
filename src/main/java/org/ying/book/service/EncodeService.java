package org.ying.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EncodeService {
    public String encode(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}

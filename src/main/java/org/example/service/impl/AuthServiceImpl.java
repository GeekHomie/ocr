package org.example.service.impl;

import org.example.service.IAuthService;
import org.springframework.stereotype.Service;

/**
 * @author Ethan
 * @description
 * @date 8/25 10:44
 */
@Service
public class AuthServiceImpl implements IAuthService {
    @Override
    public boolean login(String username, String password) {
        if ("bosc".equals(username) && "test".equals(password)){
            return true;
        }
        return false;
    }
}

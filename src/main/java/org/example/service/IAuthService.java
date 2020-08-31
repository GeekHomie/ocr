package org.example.service;

/**
 * @author Ethan
 * @description
 * @date 8/25 10:44
 */
public interface IAuthService {
    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    boolean login(String username, String password);
}

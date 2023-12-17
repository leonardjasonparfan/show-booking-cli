package com.ljparfan.showbooking.service;

import com.ljparfan.showbooking.model.UserType;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private UserType currentUserType;
    @Override
    public UserType getCurrentUserType() {
        return currentUserType;
    }

    @Override
    public void setCurrentUserType(UserType userType) {
        this.currentUserType = userType;
    }
}

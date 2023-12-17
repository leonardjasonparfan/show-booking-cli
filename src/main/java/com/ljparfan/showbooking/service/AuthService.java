package com.ljparfan.showbooking.service;

import com.ljparfan.showbooking.model.UserType;

public interface AuthService {
    UserType getCurrentUserType();
    void setCurrentUserType(UserType userType);
}

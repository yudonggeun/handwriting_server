package com.promotion.handwriting.dto.response;

import com.promotion.handwriting.enums.UserType;
import lombok.Data;

@Data
public class LoginSuccessResponse {
    private UserType role;
}

package com.promotion.handwriting.enums;

public enum UserType {
    ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + this.name();
    }
}

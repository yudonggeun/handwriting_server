package com.promotion.handwriting.enums;

public enum UserType {
    OWNER, USER;

    @Override
    public String toString() {
        return "ROLE_" + this.name();
    }
}

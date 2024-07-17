package ru.kitayanochka.shop_inventory_system.exception.message;

public enum UserExceptionMessage {
    USER_NOT_FOUND("User with id(%d) doesn't exist!"),
    EMAIL_ALREADY_IN_USE("Email \"%s\" is already in use!"),
    PHONE_ALREADY_IN_USE("Phone \"%s\" is already in use!");

    private final String message;

    UserExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... arg) {
        return this.message.formatted(arg);
    }

}

package com.github.sarhatabaot.farmassistreboot.messages;

public final class Permissions {
    public static final String BASE_PERMISSION = "farmassist.";
    public static final String UPDATE_NOTIFY = "farmassist.notify.update";
    public static final String TILL = "farmassist.till";
    public static final String WHEAT = "farmassist.wheat";

    private Permissions() {
        throw new UnsupportedOperationException(Debug.UTIL_CLASS);
    }
}
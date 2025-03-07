package com.github.sarhatabaot.farmassistreboot.messages;

public final class Permissions {
    public static final String BASE_PERMISSION = "farmassist.";
    public static final String UPDATE_NOTIFY = "farmassist.notify.update";
    public static final String TILL = "farmassist.till";
    public static final String WHEAT = "farmassist.wheat";
    public static final String NO_SEEDS = "farmassist.noseeds";
    public static final String NO_DROPS = "farmassist.nodrops";

    private Permissions() {
        throw new UnsupportedOperationException(Debug.UTIL_CLASS);
    }
}
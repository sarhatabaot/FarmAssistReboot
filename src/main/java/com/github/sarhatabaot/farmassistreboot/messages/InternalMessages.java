package com.github.sarhatabaot.farmassistreboot.messages;

public final class InternalMessages {
    public static class Reload {
        public static final String COMMAND = "FarmAssistReboot has been reloaded.";
        public static final String DEBUG = "FarmAssistReboot Reloaded.";

        private Reload() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    private InternalMessages() {
        throw new UnsupportedOperationException(Debug.UTIL_CLASS);
    }
}
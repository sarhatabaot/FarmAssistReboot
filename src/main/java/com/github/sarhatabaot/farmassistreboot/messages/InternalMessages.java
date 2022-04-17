package com.github.sarhatabaot.farmassistreboot.messages;

public final class InternalMessages {
    public static class TogglePlayer {
        public static final String ON = "FarmAssistReboot functions are now on for you!";
        public static final String OFF = "FarmAssistReboot functions turned off for you!";
    }

    public static class ToggleGlobal {
        public static final String ON = "FarmAssistReboot functions are globally back on!";
        public static final String OFF = "FarmAssistReboot functions turned off globally!";
    }

    public static class Info {
        public static final String VERSION = "%s version: %s";
        public static final String MAINTAINERS = "Maintainers: %s";
    }

    public static class Update {
        public static final String RUNNING_LATEST_VERSION = "You are running the latest version: %s";
        public static final String NEW_UPDATE = "New update: %s Current version: %s";
        public static final String GET_NEW_UPDATE = "Get at %s";
        public static final String NEW_VERSION_FAIL = "Could not get new version.";
    }

    public static class Reload {
        public static final String COMMAND = "FarmAssistReboot has been reloaded.";
        public static final String DEBUG = "FarmAssistReboot Reloaded.";
    }
}
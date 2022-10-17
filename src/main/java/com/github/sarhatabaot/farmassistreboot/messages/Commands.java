package com.github.sarhatabaot.farmassistreboot.messages;

public final class Commands {
    public static final String BASE_COMMAND = "farmassist|fa";

    public static class TogglePlayer {
        public static final String SUB_COMMAND = "toggle";
        public static final String PERMISSION = "farmassist.toggle";
        public static final String DESCRIPTION = "Toggle usage of FarmAssist";

        private TogglePlayer() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class ToggleGlobal {
        public static final String SUB_COMMAND = "global|g";
        public static final String PERMISSION = "farmassist.toggle.global";
        public static final String DESCRIPTION = "Turn farmassist off/on globally";

        private ToggleGlobal() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class Language {
        public static final String SUB_COMMAND = "language|lang";
        public static final String PERMISSION = "farmassist.lang";
        public static final String DESCRIPTION = "Change the active plugin language.";

        private Language() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class Info {
        public static final String SUB_COMMAND = "info|about";
        public static final String PERMISSION = "farmassist.info";
        public static final String DESCRIPTION = "";

        private Info() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class Update {
        public static final String SUB_COMMAND = "update|u";
        public static final String PERMISSION = "farmassist.update";
        public static final String DESCRIPTION = "";

        private Update() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class Reload {
        public static final String SUB_COMMAND = "reload";
        public static final String PERMISSION = "farmassist.reload";
        public static final String DESCRIPTION = "Reload FarmAssistReboot";

        private Reload() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    private Commands() {
        throw new UnsupportedOperationException(Debug.UTIL_CLASS);
    }
}
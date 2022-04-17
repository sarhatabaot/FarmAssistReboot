package com.github.sarhatabaot.farmassistreboot.messages;

public final class Commands {
    public static final String BASE_COMMAND = "farmassist|fa";

    public static class TogglePlayer {
        public static final String SUB_COMMAND = "toggle";
        public static final String PERMISSION = "farmassist.toggle";
        public static final String DESCRIPTION = "Toggle usage of FarmAssist";
    }

    public static class ToggleGlobal {
        public static final String SUB_COMMAND = "global|g";
        public static final String PERMISSION = "farmassist.toggle.global";
        public static final String DESCRIPTION = "Turn farmassist off/on globally";
    }

    public static class Info {
        public static final String SUB_COMMAND = "info|about";
        public static final String PERMISSION = "farmassist.info";
        public static final String DESCRIPTION = "";
    }

    public static class Update {
        public static final String SUB_COMMAND = "update|u";
        public static final String PERMISSION = "farmassist.update";
        public static final String DESCRIPTION = "";
    }

    public static class Reload {
        public static final String SUB_COMMAND = "reload";
        public static final String PERMISSION = "farmassist.reload";
        public static final String DESCRIPTION = "Reload FarmAssistReboot";
    }
}
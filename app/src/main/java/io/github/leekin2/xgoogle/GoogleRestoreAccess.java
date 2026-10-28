package io.github.leekin2.xgoogle;

import java.util.function.BooleanSupplier;

/** Android Switch writes a secure setting before starting cloud restore. */
final class GoogleRestoreAccess {
    static final String SECURE_SETTINGS = "android.permission.WRITE_SECURE_SETTINGS";

    @FunctionalInterface
    interface Grant { void run() throws Exception; }

    static final class MissingPermission extends Exception {
        MissingPermission() { super("Android Switch secure-settings permission is still missing"); }
    }

    /** Do not open a flow that will crash, even when the shell command exits successfully. */
    static void prepare(BooleanSupplier granted, Grant grant) throws Exception {
        if (granted.getAsBoolean()) return;
        grant.run();
        if (!granted.getAsBoolean()) throw new MissingPermission();
    }

    private GoogleRestoreAccess() { }
}

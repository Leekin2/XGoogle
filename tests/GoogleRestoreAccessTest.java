package io.github.leekin2.xgoogle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class GoogleRestoreAccessTest {
    public static void main(String[] args) throws Exception {
        // An ADB/preinstalled grant must work even if root is absent or refused.
        GoogleRestoreAccess.prepare(() -> true, () -> {
            throw new AssertionError("Root requested for an already granted permission");
        });

        AtomicBoolean granted = new AtomicBoolean();
        AtomicInteger attempts = new AtomicInteger();
        GoogleRestoreAccess.prepare(granted::get, () -> {
            attempts.incrementAndGet();
            granted.set(true);
        });
        if (attempts.get() != 1) throw new AssertionError("Permission was not prepared once");

        // The old implementation ignored this and opened an activity that later crashed.
        IOException denied = new IOException("root denied");
        try {
            GoogleRestoreAccess.prepare(() -> false, () -> { throw denied; });
            throw new AssertionError("A denied grant was accepted");
        } catch (IOException failure) {
            if (failure != denied) throw new AssertionError("Grant failure was lost");
        }

        // A zero shell exit code is not proof that PackageManager granted the permission.
        try {
            GoogleRestoreAccess.prepare(() -> false, () -> { });
            throw new AssertionError("A no-op grant was accepted");
        } catch (GoogleRestoreAccess.MissingPermission expected) { }

        System.out.println("PASS: Google restore requires a verified permission grant");
    }
}

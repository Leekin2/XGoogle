package io.github.leekin2.xgoogle;

import android.content.Context;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Google location accuracy: China ROMs name Xiaomi's own provider (MetokNLP) as the only network
 * location and geocoding service. With this on, system_server instead picks the best system service
 * by its declared version, as Android does without a fixed package: Google Play services declares a
 * higher version, so it wins while it is there, and Xiaomi's provider takes over again if Play
 * services is disabled or removed. Fused location stays with the ROM.
 */
final class LocationHooks {
    private static final Set<String> ACTIONS = Set.of(
            "com.android.location.service.v3.NetworkLocationProvider",
            "com.android.location.service.GeocodeProvider");
    private static final String SUPPLIER = "com.android.server.servicewatcher.CurrentUserServiceSupplier";

    static void hookSystem(HookInstaller hooks, ClassLoader loader) {
        try {
            Class<?> supplier = Class.forName(SUPPLIER, false, loader);
            Method fromConfig = supplier.getDeclaredMethod("createFromConfig", Context.class, String.class, int.class, int.class);
            Method create = supplier.getDeclaredMethod("create", Context.class, String.class, String.class, String.class, String.class);
            // The providers are created once at boot; keep ART from inlining the config call into them.
            for (String caller : new String[]{"ProxyLocationProvider", "ProxyGeocodeProvider"}) {
                try {
                    Class<?> type = Class.forName("com.android.server.location.provider.proxy." + caller, false, loader);
                    for (java.lang.reflect.Constructor<?> constructor : type.getDeclaredConstructors()) hooks.deoptimize(constructor);
                } catch (ClassNotFoundException ignored) { }
            }
            hooks.install(fromConfig, chain -> {
                String action = (String) chain.getArg(1);
                if (!ACTIONS.contains(action) || !FeatureConfig.enabled(FeaturePolicy.LOCATION)) return chain.proceed();
                hooks.info("LOCATION: " + action + " picks the best system provider");
                return create.invoke(null, chain.getArg(0), action, null, null, null);
            });
            hooks.info("LOCATION: provider selection hooked");
        } catch (Throwable error) {
            hooks.error("LOCATION: ROM provider kept", error);
        }
    }

    private LocationHooks() {}
}

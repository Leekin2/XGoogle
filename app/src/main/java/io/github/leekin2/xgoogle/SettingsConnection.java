package io.github.leekin2.xgoogle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import io.github.libxposed.service.XposedService;
import io.github.libxposed.service.XposedServiceHelper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/** One process listener; activities are never retained by the framework service. */
final class SettingsConnection {
    private static WeakReference<MainActivity> activity = new WeakReference<>(null);
    private static XposedService service;
    private static boolean registered;

    /** Scopes added after the first release; LSPosed does not add them to existing installs. */
    private static final String[] LATER_SCOPES = {"com.miui.securitycore", "com.android.camera"};
    private static Context app;

    /** From the application: connect early so missing scopes are offered even without the UI. */
    static synchronized void start(Context context) {
        app = context.getApplicationContext();
        register();
    }

    static synchronized void connect(MainActivity value) {
        activity = new WeakReference<>(value);
        register();
        value.onFrameworkChanged(service);
    }

    private static void register() {
        if (!registered) {
            registered = true;
            XposedServiceHelper.registerListener(new XposedServiceHelper.OnServiceListener() {
                @Override public void onServiceBind(XposedService value) { update(value); }
                @Override public void onServiceDied(XposedService value) { update(null); }
            });
        }
    }

    private static synchronized void update(XposedService value) {
        service = value;
        if (value != null) {
            requestLaterScopes(value);
            dropIntegrityScopes(value, null);
        }
        MainActivity target = activity.get();
        if (target != null) target.runOnUiThread(() -> target.onFrameworkChanged(value));
    }

    static synchronized void detach(MainActivity value) {
        if (activity.get() == value) activity.clear();
    }
    /** LSPosed shows a one-tap approval notice; asked once per version so a "no" is not repeated. */
    private static void requestLaterScopes(XposedService value) {
        try {
            if (app == null) return;
            List<String> scope = value.getScope();
            List<String> missing = new ArrayList<>();
            for (String s : LATER_SCOPES) if (!scope.contains(s)) missing.add(s);
            if (missing.isEmpty()) return;
            SharedPreferences prefs = app.getSharedPreferences("scope", Context.MODE_PRIVATE);
            long version = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).getLongVersionCode();
            if (prefs.getLong("asked", 0) == version) return;
            prefs.edit().putLong("asked", version).apply();
            value.requestScope(missing, new XposedService.OnScopeEventListener() { });
        } catch (Throwable t) {
            Log.w("XGoogle", "Scope request failed", t);
        }
    }

    /**
     * Only Timeline needs Play services and GSF; an older XGoogle recommended them, and LSPosed
     * keeps them ticked. Being hooked inside Play services can cost the Play Integrity verdicts,
     * so without Timeline they are taken out of the scope. [timeline] null reads the saved switch.
     */
    static synchronized void dropIntegrityScopes(XposedService value, Boolean timeline) {
        XposedService current = value != null ? value : service;
        if (current == null) return;
        try {
            boolean on = timeline != null ? timeline : current.getRemotePreferences(FeatureConfig.GROUP)
                    .getBoolean(FeaturePolicy.TIMELINE, FeaturePolicy.defaultValue(FeaturePolicy.TIMELINE));
            if (on) return;
            List<String> scope = current.getScope();
            List<String> drop = new ArrayList<>();
            for (String name : new String[]{TimelineHooks.GMS, TimelineHooks.GSF}) if (scope.contains(name)) drop.add(name);
            if (drop.isEmpty()) return;
            current.removeScope(drop);
            Log.i("XGoogle", "Removed unused scopes " + drop);
        } catch (Throwable t) {
            Log.w("XGoogle", "Scope removal failed", t);
        }
    }

    enum ScopeResult { OK, DENIED, SYSTEM_MISSING }

    /**
     * Turning a switch on asks LSPosed for that feature's missing app scopes straight away; the
     * switch is saved either way. LSPosed cannot add the system framework from a
     * request, so a missing "system" is only reported.
     */
    static synchronized void requestScopes(Context context, String feature, java.util.function.Consumer<ScopeResult> done) {
        XposedService current = service;
        if (current == null) { Log.i("XGoogle", "Scope check for " + feature + " skipped: LSPosed not connected"); return; }
        android.os.Handler main = new android.os.Handler(android.os.Looper.getMainLooper());
        try {
            List<String> scope = current.getScope();
            boolean systemMissing = false;
            List<String> missing = new ArrayList<>();
            for (String name : FeatureScopes.required(feature)) {
                if (scope.contains(name)) continue;
                if (FeatureScopes.SYSTEM.equals(name)) { systemMissing = true; continue; }
                try { context.getPackageManager().getPackageInfo(name, 0); missing.add(name); }
                catch (android.content.pm.PackageManager.NameNotFoundException notInstalled) { }
            }
            ScopeResult after = systemMissing ? ScopeResult.SYSTEM_MISSING : ScopeResult.OK;
            Log.i("XGoogle", "Scope check for " + feature + ": missing=" + missing + " systemMissing=" + systemMissing);
            if (missing.isEmpty()) { if (systemMissing) main.post(() -> done.accept(after)); return; }
            current.requestScope(missing, new XposedService.OnScopeEventListener() {
                @Override public void onScopeRequestApproved(List<String> approved) {
                    Log.i("XGoogle", "Scope request approved: " + approved);
                    main.post(() -> done.accept(after));
                }
                @Override public void onScopeRequestFailed(String message) {
                    Log.w("XGoogle", "Scope request failed: " + message);
                    main.post(() -> done.accept(ScopeResult.DENIED));
                }
            });
        } catch (Throwable t) {
            Log.w("XGoogle", "Scope request failed", t);
        }
    }

    static synchronized List<String> currentScope() {
        return service == null ? null : service.getScope();
    }

    private SettingsConnection() { }
}

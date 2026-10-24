package io.github.leekin2.xgoogle;

import android.location.Location;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Google Maps Timeline (location history) on the China ROM. Google Play services and Maps turn
 * it off when the SIM or network country is a restricted region, so inside those apps the SIM and
 * network report a US carrier. Maps also stops shifting the map to GCJ-02 once it believes the
 * phone is in the US; there the location and the Timeline points are shifted the way the
 * mainland map needs. Hong Kong, Macao and Taiwan use plain WGS-84 and are left alone.
 * Idea from SherlockChiang/ReLocationReportEnabler (MIT); written anew here.
 */
final class TimelineHooks {
    static final String GMS = "com.google.android.gms";
    static final String GSF = "com.google.android.gsf";
    static final String MAPS = "com.google.android.apps.maps";
    /** Play services' DroidGuard process, which answers Play Integrity. */
    static final String INTEGRITY_PROCESS = "com.google.android.gms.unstable";
    private static final String ISO = "us", SUBSCRIPTION_ISO = "US", MCC_MNC = "310030", MCC = "310", MNC = "030";
    private static final ThreadLocal<boolean[]> READING = ThreadLocal.withInitial(() -> new boolean[1]);
    /** GMS and GSF can share a process; the carrier hooks go in once per process. */
    private static boolean carrierDone;

    static boolean handles(String packageName) {
        return GMS.equals(packageName) || GSF.equals(packageName) || MAPS.equals(packageName);
    }

    static void install(HookInstaller hooks, ClassLoader loader, String packageName) {
        synchronized (TimelineHooks.class) {
            if (!carrierDone) {
                carrierDone = true;
                int telephony = spoofTelephony(hooks);
                hooks.info("TIMELINE " + packageName + ": US carrier reported through " + telephony + " method(s)");
            }
        }
        if (!MAPS.equals(packageName)) return;
        try {
            hooks.install(Location.class.getMethod("getLatitude"), chain -> shifted(chain.proceed(), (Location) chain.getThisObject(), 0));
            hooks.install(Location.class.getMethod("getLongitude"), chain -> shifted(chain.proceed(), (Location) chain.getThisObject(), 1));
        } catch (Throwable error) {
            hooks.error("TIMELINE: map location left unshifted", error);
        }
        shiftTimelinePoints(hooks, loader);
    }

    /** TelephonyManager, SubscriptionInfo and the gsm.* properties, in every overload. */
    private static int spoofTelephony(HookInstaller hooks) {
        int count = 0;
        try {
            Class<?> telephony = Class.forName("android.telephony.TelephonyManager");
            count += answer(hooks, telephony, ISO, "getSimCountryIso", "getNetworkCountryIso",
                    "getSimCountryIsoForPhone", "getNetworkCountryIsoForPhone");
            count += answer(hooks, telephony, MCC_MNC, "getSimOperator", "getNetworkOperator", "getSimOperatorNumeric",
                    "getNetworkOperatorNumeric", "getSimOperatorNumericForPhone", "getNetworkOperatorForPhone");
            Class<?> subscription = Class.forName("android.telephony.SubscriptionInfo");
            count += answer(hooks, subscription, SUBSCRIPTION_ISO, "getCountryIso");
            count += answer(hooks, subscription, MCC, "getMccString");
            count += answer(hooks, subscription, MNC, "getMncString");
            count += answer(hooks, subscription, Integer.parseInt(MCC), "getMcc");
            count += answer(hooks, subscription, Integer.parseInt(MNC), "getMnc");
            Class<?> properties = Class.forName("android.os.SystemProperties");
            for (Method get : properties.getDeclaredMethods()) {
                if (!get.getName().equals("get") || get.getParameterCount() == 0
                        || get.getParameterTypes()[0] != String.class || get.getReturnType() != String.class) continue;
                hooks.install(get, chain -> {
                    String value = carrierProperty((String) chain.getArg(0));
                    return value != null ? value : chain.proceed();
                });
                count++;
            }
        } catch (Throwable error) {
            hooks.error("TIMELINE: carrier spoof incomplete", error);
        }
        return count;
    }

    private static int answer(HookInstaller hooks, Class<?> type, Object value, String... names) {
        int count = 0;
        for (String name : names) {
            for (Method method : type.getDeclaredMethods()) {
                if (!method.getName().equals(name)) continue;
                Class<?> returns = method.getReturnType();
                if (!(returns == String.class && value instanceof String) && !(returns == int.class && value instanceof Integer)) continue;
                hooks.install(method, chain -> value);
                count++;
            }
        }
        return count;
    }

    /** gsm.operator.numeric, gsm.sim.operator.iso-country and their per-slot forms (".1" or ",1"). */
    static String carrierProperty(String key) {
        if (key == null) return null;
        for (String base : new String[]{"gsm.sim.operator.numeric", "gsm.operator.numeric"})
            if (slotOf(key, base)) return MCC_MNC;
        for (String base : new String[]{"gsm.sim.operator.iso-country", "gsm.operator.iso-country"})
            if (slotOf(key, base)) return ISO;
        return null;
    }

    private static boolean slotOf(String key, String base) {
        if (!key.startsWith(base)) return false;
        String rest = key.substring(base.length());
        return rest.isEmpty() || rest.length() > 1 && (rest.charAt(0) == '.' || rest.charAt(0) == ',')
                && rest.substring(1).chars().allMatch(Character::isDigit);
    }

    /** One coordinate of the location, shifted to GCJ-02 on the mainland; the guard stops the getters recursing. */
    private static Object shifted(Object value, Location location, int index) {
        boolean[] reading = READING.get();
        if (reading[0] || !(value instanceof Double)) return value;
        reading[0] = true;
        try {
            double lat = index == 0 ? (Double) value : location.getLatitude();
            double lng = index == 1 ? (Double) value : location.getLongitude();
            return Gcj02.shift(lat, lng)[index];
        } finally {
            reading[0] = false;
        }
    }

    /** Timeline visits and trips are built from PlaceCandidate.Point(latitudeE7, longitudeE7, ...). */
    private static void shiftTimelinePoints(HookInstaller hooks, ClassLoader loader) {
        try {
            Class<?> point = Class.forName("com.google.android.gms.semanticlocation.PlaceCandidate$Point", false, loader);
            int count = 0;
            for (Constructor<?> constructor : point.getDeclaredConstructors()) {
                Class<?>[] types = constructor.getParameterTypes();
                if (types.length < 2 || types[0] != int.class || types[1] != int.class) continue;
                hooks.install(constructor, chain -> {
                    Object[] args = chain.getArgs().toArray();
                    double[] gcj = Gcj02.shift((Integer) args[0] / 1e7, (Integer) args[1] / 1e7);
                    args[0] = (int) Math.round(gcj[0] * 1e7);
                    args[1] = (int) Math.round(gcj[1] * 1e7);
                    return chain.proceed(args);
                });
                count++;
            }
            hooks.info("TIMELINE: " + count + " Timeline point constructor(s) shifted for the mainland map");
        } catch (Throwable error) {
            hooks.error("TIMELINE: Timeline points left unshifted", error);
        }
    }

    private TimelineHooks() { }
}

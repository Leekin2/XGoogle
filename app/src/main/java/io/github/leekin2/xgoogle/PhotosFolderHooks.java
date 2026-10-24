package io.github.leekin2.xgoogle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.enums.StringMatchType;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.result.MethodData;
import org.luckypray.dexkit.wrap.DexMethod;

/**
 * Google Photos always backs up the Camera and other DCIM folders. Its "is this an always-backed-up
 * folder" check (the method naming "/dcim" and "/mars_files/") answers no, so every folder follows
 * its own backup switch; new installs start with no folder backed up. Idea from NexAlloy's
 * "Enable DCIM folders backup control" (GPL-3.0, https://github.com/NexAlloy/NexAlloy), written
 * here with DexKit.
 */
final class PhotosFolderHooks {
    private static boolean done;

    static void install(HookInstaller hooks, ClassLoader loader, String processName, String apkPath, String dataDir) {
        if (!PhotosUnlimitedHooks.PHOTOS.equals(processName)) return;
        synchronized (PhotosFolderHooks.class) {
            if (done) return;
            done = true;
        }
        try {
            Method check = cached(hooks, loader, apkPath, dataDir);
            if (check == null) {
                hooks.info("PHOTOS_DCIM: camera folder check not found; backup left as it is");
                return;
            }
            hooks.install(check, chain -> Boolean.FALSE);
            hooks.info("PHOTOS_DCIM: DCIM folders follow their own backup switch");
        } catch (Throwable error) {
            hooks.error("PHOTOS_DCIM: camera folder check left as it is", error);
        }
    }

    /** One scan per Photos build; the found method is kept next to Photos' data. */
    private static Method cached(HookInstaller hooks, ClassLoader loader, String apkPath, String dataDir) throws Exception {
        File apk = new File(apkPath);
        String key = apkPath + ":" + apk.lastModified() + ":" + apk.length();
        File cache = dataDir == null ? null : new File(dataDir, "xgoogle_photos_dcim.cache");
        if (cache != null && cache.isFile()) {
            try {
                String[] stored = new String(Files.readAllBytes(cache.toPath()), StandardCharsets.UTF_8).split("\n", -1);
                if (stored.length == 2 && stored[0].equals(key)) {
                    if (stored[1].isEmpty()) return null;
                    Method method = new DexMethod(stored[1]).getMethodInstance(loader);
                    if (fits(method)) return method;
                }
            } catch (IOException | ReflectiveOperationException | RuntimeException | LinkageError stale) {
                // Scan again.
            }
        }
        Method found = find(loader, apkPath);
        if (cache != null) {
            try {
                File temporary = File.createTempFile("xgoogle_photos_dcim", ".tmp", new File(dataDir));
                Files.write(temporary.toPath(), (key + "\n" + (found == null ? "" : new DexMethod(found).serialize()))
                        .getBytes(StandardCharsets.UTF_8));
                if (!temporary.renameTo(cache)) temporary.delete();
            } catch (IOException | RuntimeException unwritable) {
                hooks.info("PHOTOS_DCIM cache not written: " + unwritable);
            }
        }
        return found;
    }

    private static Method find(ClassLoader loader, String apkPath) throws ReflectiveOperationException {
        AppDexTargets.loadNative();
        try (DexKitBridge bridge = DexKitBridge.create(apkPath)) {
            // Exact strings first; a build that adds a separator still matches by containment.
            for (StringMatchType match : new StringMatchType[]{StringMatchType.Equals, StringMatchType.Contains}) {
                List<MethodData> methods = bridge.findMethod(FindMethod.create().matcher(MethodMatcher.create()
                        .returnType("boolean").paramTypes("java.lang.String")
                        .usingStrings(List.of("/dcim", "/mars_files/"), match, true)));
                if (methods.size() == 1) {
                    Method method = methods.get(0).getMethodInstance(loader);
                    return fits(method) ? method : null;
                }
                if (methods.size() > 1) return null;
            }
        }
        return null;
    }

    private static boolean fits(Method method) {
        return method.getReturnType() == boolean.class && method.getParameterCount() == 1
                && method.getParameterTypes()[0] == String.class;
    }

    private PhotosFolderHooks() { }
}

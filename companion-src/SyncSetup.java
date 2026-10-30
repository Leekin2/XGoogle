import android.accounts.Account;
import android.os.IBinder;
import java.lang.reflect.Method;

/**
 * Turns sync on for the given Google accounts and authority where it was never set, as an
 * international ROM does when a Google account is added after its sync adapter is installed.
 * A sync the user set either way is left alone.
 *
 * Usage (as root): CLASSPATH=sync-setup.dex app_process /system/bin SyncSetup <authority> <account>...
 * Built into companion/calendar/sync-setup.dex with javac + d8 (see BUILDING.md).
 */
public final class SyncSetup {
    private static final int NOT_INITIALIZED = -1;

    public static void main(String[] args) throws Exception {
        // The sync service only takes these settings from a caller that can see the accounts.
        Class<?> process = Class.forName("android.os.Process");
        process.getMethod("setGid", int.class).invoke(null, 1000);
        process.getMethod("setUid", int.class).invoke(null, 1000);
        String authority = args[0];
        IBinder binder = (IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", String.class).invoke(null, "content");
        Object service = Class.forName("android.content.IContentService$Stub").getMethod("asInterface", IBinder.class).invoke(null, binder);
        Class<?> type = service.getClass();
        Method get = find(type, "getIsSyncableAsUser", 3);
        for (int i = 1; i < args.length; i++) {
            Account account = new Account(args[i], "com.google");
            if ((int) get.invoke(service, account, authority, 0) != NOT_INITIALIZED) continue;
            find(type, "setIsSyncableAsUser", 4).invoke(service, account, authority, 1, 0);
            find(type, "setSyncAutomaticallyAsUser", 4).invoke(service, account, authority, true, 0);
            System.out.println("sync on: " + authority);
        }
    }

    private static Method find(Class<?> type, String name, int count) throws NoSuchMethodException {
        for (Method method : type.getMethods()) if (method.getName().equals(name) && method.getParameterCount() == count) return method;
        throw new NoSuchMethodException(name);
    }

    private SyncSetup() {}
}

package io.github.leekin2.xgoogle.ui

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import io.github.leekin2.xgoogle.R
import java.util.Locale

enum class AppLanguage(val code: String, val label: String) {
    TR("tr", "Türkçe"),
    EN("en", "English"),
    RU("ru", "Русский"),
    ES("es", "Español"),
    ZH("zh", "简体中文");

    companion object {
        fun of(code: String?) = entries.firstOrNull { it.code == code } ?: TR

        /** First launch: the system language when supported, otherwise English. */
        fun forSystem(code: String) = entries.firstOrNull { it.code == code } ?: EN
    }
}

/** Every user-visible string of the app, loaded from Android XML resources. */
class Texts internal constructor(private val resources: Resources) {
    val subtitle = resources.getString(R.string.subtitle)
    val language = resources.getString(R.string.language)
    val version: (String) -> String = { resources.getString(R.string.version, it) }
    val active = resources.getString(R.string.active)
    val inactive = resources.getString(R.string.inactive)
    val connecting = resources.getString(R.string.connecting)
    val groupAssistant = resources.getString(R.string.group_assistant)
    val defaultAssistant = resources.getString(R.string.default_assistant)
    val assistantReading = resources.getString(R.string.assistant_reading)
    val assistantNone = resources.getString(R.string.assistant_none)
    val assistantTap = resources.getString(R.string.assistant_tap)
    val assistantCurrent: (String) -> String = { resources.getString(R.string.assistant_current, it) }
    val heyGoogle = resources.getString(R.string.hey_google)
    val lens = resources.getString(R.string.lens)
    val lensSummary = resources.getString(R.string.lens_summary)
    val liveTranslate = resources.getString(R.string.live_translate)
    val liveTranslateSummary = resources.getString(R.string.live_translate_summary)
    val groupSearch = resources.getString(R.string.group_search)
    val groupLock = resources.getString(R.string.group_lock)
    val groupSystem = resources.getString(R.string.group_system)
    val circle = resources.getString(R.string.circle)
    val circleSummary = resources.getString(R.string.circle_summary)
    val circleVibrate = resources.getString(R.string.circle_vibrate)
    val circleVibrateSummary = resources.getString(R.string.circle_vibrate_summary)
    val circleLongPress = resources.getString(R.string.circle_long_press)
    val circleSideHold = resources.getString(R.string.circle_side_hold)
    val circleDoubleTap = resources.getString(R.string.circle_double_tap)
    val circleLongPressOn = resources.getString(R.string.circle_long_press_on)
    val circleLongPressOff = resources.getString(R.string.circle_long_press_off)
    val circleSideHoldOn = resources.getString(R.string.circle_side_hold_on)
    val circleSideHoldOff = resources.getString(R.string.circle_side_hold_off)
    val circleDoubleTapOn = resources.getString(R.string.circle_double_tap_on)
    val circleDoubleTapOff = resources.getString(R.string.circle_double_tap_off)
    val circleOptions = resources.getString(R.string.circle_options)
    val lensOptions = resources.getString(R.string.lens_options)
    val lockOptions = resources.getString(R.string.lock_options)
    val gmsOptions = resources.getString(R.string.gms_options)
    val cameraLens = resources.getString(R.string.camera_lens)
    val cameraLensSummary = resources.getString(R.string.camera_lens_summary)
    val wallet = resources.getString(R.string.wallet)
    val walletSummary = resources.getString(R.string.wallet_summary)
    val lockShortcuts = resources.getString(R.string.lock_shortcuts)
    val lockShortcutsSummary = resources.getString(R.string.lock_shortcuts_summary)
    val gms = resources.getString(R.string.gms)
    val gmsSummary = resources.getString(R.string.gms_summary)
    val googleEntry = resources.getString(R.string.google_entry)
    val googleEntrySummary = resources.getString(R.string.google_entry_summary)
    val timeline = resources.getString(R.string.timeline)
    val timelineSummary = resources.getString(R.string.timeline_summary)
    val passkey = resources.getString(R.string.passkey)
    val passkeySummary = resources.getString(R.string.passkey_summary)
    val groupOther = resources.getString(R.string.group_other)
    val hideIcon = resources.getString(R.string.hide_icon)
    val hideIconSummary = resources.getString(R.string.hide_icon_summary)
    val hyperStyle = resources.getString(R.string.hyper_style)
    val hyperStyleSummary = resources.getString(R.string.hyper_style_summary)
    val groupLook = resources.getString(R.string.group_look)
    val theme = resources.getString(R.string.theme)
    val themeSystem = resources.getString(R.string.theme_system)
    val themeLight = resources.getString(R.string.theme_light)
    val themeDark = resources.getString(R.string.theme_dark)
    val monet = resources.getString(R.string.monet)
    val monetSummary = resources.getString(R.string.monet_summary)
    val blur = resources.getString(R.string.blur)
    val blurSummary = resources.getString(R.string.blur_summary)
    val about = resources.getString(R.string.about)
    val aboutSummary: (String) -> String = { resources.getString(R.string.about_summary, it) }
    val restartHint = resources.getString(R.string.restart_hint)
    val switchOn = resources.getString(R.string.switch_on)
    val switchOff = resources.getString(R.string.switch_off)
    val companion: (CompanionState) -> String = {
        resources.getString(
            when (it) {
                CompanionState.UNKNOWN -> R.string.companion_unknown
                CompanionState.CHECKING -> R.string.companion_checking
                CompanionState.MISSING -> R.string.companion_missing
                CompanionState.DISABLED -> R.string.companion_disabled
                CompanionState.READY -> R.string.companion_ready
                CompanionState.PENDING -> R.string.companion_pending
                CompanionState.OUTDATED -> R.string.companion_outdated
                CompanionState.INSTALLING -> R.string.companion_installing
            }
        )
    }
    val statusTitle = resources.getString(R.string.status_title)
    val statusMessage = resources.getString(R.string.status_message)
    val installTitle = resources.getString(R.string.install_title)
    val installMessage = resources.getString(R.string.install_message)
    val disabledTitle = resources.getString(R.string.disabled_title)
    val disabledMessage = resources.getString(R.string.disabled_message)
    val installedTitle = resources.getString(R.string.installed_title)
    val installedMessage = resources.getString(R.string.installed_message)
    val companionUpdateTitle = resources.getString(R.string.companion_update_title)
    val companionUpdateMessage = resources.getString(R.string.companion_update_message)
    val companionOfferTitle = resources.getString(R.string.companion_offer_title)
    val companionOfferMessage = resources.getString(R.string.companion_offer_message)
    val companionDoneMessage = resources.getString(R.string.companion_done_message)
    val update = resources.getString(R.string.update)
    val hideIconTitle = resources.getString(R.string.hide_icon_title)
    val hideIconMessage = resources.getString(R.string.hide_icon_message)
    val errorTitle = resources.getString(R.string.error_title)
    val install = resources.getString(R.string.install)
    val hide = resources.getString(R.string.hide)
    val ok = resources.getString(R.string.ok)
    val cancel = resources.getString(R.string.cancel)
    val errSave = resources.getString(R.string.err_save)
    val errSaveGeneric = resources.getString(R.string.err_save_generic)
    val errWalletPower = resources.getString(R.string.err_wallet_power)
    val errCheckCompanion = resources.getString(R.string.err_check_companion)
    val errCompanionSaved = resources.getString(R.string.err_companion_saved)
    val errCompanionInstall = resources.getString(R.string.err_companion_install)
    val errOpenAssistant = resources.getString(R.string.err_open_assistant)
    val errIcon = resources.getString(R.string.err_icon)
    val errRepository = resources.getString(R.string.err_repository)
    val errTelegram = resources.getString(R.string.err_telegram)
    val restartApps = resources.getString(R.string.restart_apps)
    val restartAppsSummary = resources.getString(R.string.restart_apps_summary)
    val restartTitle = resources.getString(R.string.restart_title)
    val restartMessage = resources.getString(R.string.restart_message)
    val restart = resources.getString(R.string.restart)
    val restartDone = resources.getString(R.string.restart_done)
    val errRestart = resources.getString(R.string.err_restart)
    val scopeDenied = resources.getString(R.string.scope_denied)
    val scopeSystemMissing = resources.getString(R.string.scope_system_missing)
    val walletDefaultSet = resources.getString(R.string.wallet_default_set)
    val errNoWallet = resources.getString(R.string.err_no_wallet)
    val errNoNfc = resources.getString(R.string.err_no_nfc)
    val errWalletPayment = resources.getString(R.string.err_wallet_payment)
    val rootDenied = resources.getString(R.string.root_denied)
    val rootMissing = resources.getString(R.string.root_missing)
    val rootTimeout = resources.getString(R.string.root_timeout)
    val rootFailed = resources.getString(R.string.root_failed)
    val tabHome = resources.getString(R.string.tab_home)
    val tabSettings = resources.getString(R.string.tab_settings)
    val statusWorking = resources.getString(R.string.status_working)
    val statusIncomplete = resources.getString(R.string.status_incomplete)
    val statusMissingIntro = resources.getString(R.string.status_missing_intro)
    val systemFramework = resources.getString(R.string.system_framework)
    val statusTap = resources.getString(R.string.status_tap)
    val groupApp = resources.getString(R.string.group_app)
    val checkUpdates = resources.getString(R.string.check_updates)
    val updateChecking = resources.getString(R.string.update_checking)
    val updateLatest = resources.getString(R.string.update_latest)
    val updateFailed = resources.getString(R.string.update_failed)
    val updateTitle = resources.getString(R.string.update_title)
    val download = resources.getString(R.string.download)
    val support = resources.getString(R.string.support)
    val supportSummary = resources.getString(R.string.support_summary)
    val errSupport = resources.getString(R.string.err_support)
    val errLsposed = resources.getString(R.string.err_lsposed)
    val restoreBackup = resources.getString(R.string.restore_backup)
    val restoreBackupSummary = resources.getString(R.string.restore_backup_summary)
    val restoreInstallTitle = resources.getString(R.string.restore_install_title)
    val restoreInstallMessage = resources.getString(R.string.restore_install_message)
    val errRestore = resources.getString(R.string.err_restore)
    val errRestorePermission = resources.getString(R.string.err_restore_permission)
    val photosUnlimited = resources.getString(R.string.photos_unlimited)
    val photosUnlimitedSummary = resources.getString(R.string.photos_unlimited_summary)
    val photosDcim = resources.getString(R.string.photos_dcim)
    val photosDcimSummary = resources.getString(R.string.photos_dcim_summary)
    val powerKey = resources.getString(R.string.power_key)
    val powerKeySummary = resources.getString(R.string.power_key_summary)
    val updateMessage: (String) -> String = { resources.getString(R.string.update_message, it) }
}

private val loaded = java.util.concurrent.ConcurrentHashMap<AppLanguage, Texts>()

/** Every screen asks on each recomposition; the strings of a language are read once. */
fun textsFor(context: Context, language: AppLanguage): Texts = loaded.getOrPut(language) {
    val app = context.applicationContext ?: context
    val configuration = Configuration(app.resources.configuration).apply {
        setLocale(Locale.forLanguageTag(language.code))
    }
    Texts(app.createConfigurationContext(configuration).resources)
}

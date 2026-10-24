// Material component conventions follow tiann/KernelSU (GPL-3.0); see NOTICE.txt.
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)

package io.github.leekin2.xgoogle.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.border
import androidx.compose.material.icons.rounded.BlurOn
import androidx.compose.ui.draw.clip
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Gesture
import androidx.compose.material.icons.rounded.PanTool
import androidx.compose.material.icons.rounded.Swipe
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.selected
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.FolderSpecial
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.DashboardCustomize
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import io.github.leekin2.xgoogle.BuildConfig
import io.github.leekin2.xgoogle.FeaturePolicy

@Composable
fun MaterialScreen(
    state: XGoogleState,
    onSetting: (String, Boolean) -> Unit,
    onAssistant: () -> Unit,
    onIconVisibility: (Boolean) -> Unit,
    onStatus: () -> Unit,
    onAbout: () -> Unit,
    onTelegram: () -> Unit,
    onLanguage: (AppLanguage) -> Unit,
    onLook: (Look) -> Unit,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
    onRestartApps: () -> Unit,
    onCheckUpdates: () -> Unit,
    onSupport: () -> Unit,
    onRestore: () -> Unit,
    /** Kept by the caller, so switching between the two looks stays on the same tab. */
    tab: Int,
    onTab: (Int) -> Unit
) {
    val t = textsFor(LocalContext.current, state.language)
    val bar = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    // Frosted bar: the list is recorded as a backdrop and blurred behind the floating bar.
    val backdrop = rememberLayerBackdrop()
    val background = MaterialTheme.colorScheme.surfaceContainer
    Scaffold(
        modifier = Modifier.nestedScroll(bar.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(if (tab == 0) "XGoogle" else t.tabSettings) },
                subtitle = { Text(if (tab == 0) t.subtitle else "XGoogle") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer),
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
                scrollBehavior = bar
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            // Switching tabs starts the new tab at its top.
            key(tab) {
                Column(Modifier.padding(padding)
                    .then(if (state.look.blur) Modifier.layerBackdrop(backdrop).background(background) else Modifier)
                    .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(12.dp))
                    if (tab == 0) HomeTab(state, t, onSetting, onAssistant, onStatus, onRestartApps, onRestore)
                    else SettingsTab(state, t, onIconVisibility, onAbout, onTelegram, onLanguage, onLook, onCheckUpdates, onSupport)
                    // Room for the floating bar.
                    Spacer(Modifier.height(112.dp))
                }
            }
            FloatingTabs(t, tab, onTab, if (state.look.blur) backdrop else null, Modifier.align(Alignment.BottomCenter))
        }
    }
    XGoogleDialog(state, t, onDialogConfirm, onDialogDismiss)
}

@Composable
private fun FloatingTabs(t: Texts, selected: Int, onSelect: (Int) -> Unit, backdrop: LayerBackdrop?,
    modifier: Modifier = Modifier) {
    val pill = MaterialTheme.colorScheme.surfaceContainerHighest
    // With a backdrop the bar is frosted glass over the list; otherwise a solid pill.
    val glass = if (backdrop == null) Modifier else Modifier.clip(CircleShape).textureBlur(backdrop, CircleShape, 25f,
        colors = BlurColors(blendColors = listOf(BlendColorEntry(pill.copy(alpha = 0.6f)))))
        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
    Surface(modifier.navigationBarsPadding().padding(bottom = 16.dp).then(glass), shape = CircleShape,
        color = if (backdrop == null) pill else Color.Transparent, shadowElevation = if (backdrop == null) 6.dp else 0.dp) {
        Row(Modifier.padding(6.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(Icons.Rounded.Home to t.tabHome, Icons.Rounded.Settings to t.tabSettings).forEachIndexed { index, (icon, label) ->
                val active = index == selected
                Surface(onClick = { onSelect(index) }, shape = CircleShape,
                    color = if (active) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                    contentColor = if (active) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.semantics { this.selected = active }) {
                    Row(Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, Modifier.size(22.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(label, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

/** KernelSU-style status: green working, yellow missing scopes (listed), red inactive. Opens LSPosed. */
@Composable
private fun StatusCard(state: XGoogleState, t: Texts, onClick: () -> Unit) {
    val dark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val scheme = MaterialTheme.colorScheme
    val waiting = state.loading && !state.connected
    val outdated = state.companion == CompanionState.OUTDATED
    val warn = state.missingScopes.isNotEmpty() || outdated
    val (container, content) = when {
        waiting -> scheme.secondaryContainer to scheme.onSecondaryContainer
        !state.connected -> scheme.errorContainer to scheme.onErrorContainer
        warn -> if (dark) Color(0xFF4A3F00) to Color(0xFFFFE08A) else Color(0xFFFFF0B3) to Color(0xFF574500)
        else -> if (dark) Color(0xFF1F4D26) to Color(0xFFB9F0C0) else Color(0xFFC9EFCB) to Color(0xFF14501E)
    }
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth(), color = container, contentColor = content,
        shape = MaterialTheme.shapes.extraLarge) {
        Row(Modifier.padding(horizontal = 24.dp, vertical = 22.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(when {
                waiting -> Icons.Rounded.HourglassTop
                !state.connected -> Icons.Rounded.HighlightOff
                warn -> Icons.Rounded.WarningAmber
                else -> Icons.Rounded.CheckCircle
            }, null, Modifier.size(36.dp))
            Spacer(Modifier.width(20.dp))
            Column(Modifier.weight(1f)) {
                Text(when {
                    waiting -> t.connecting
                    !state.connected -> t.inactive
                    state.missingScopes.isNotEmpty() -> t.statusIncomplete
                    outdated -> t.companion(CompanionState.OUTDATED)
                    else -> t.statusWorking
                }, style = MaterialTheme.typography.titleLarge)
                Text("${t.version(BuildConfig.VERSION_NAME)} · API 102", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp))
                if (state.connected && state.missingScopes.isNotEmpty()) {
                    Text(t.statusMissingIntro, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 10.dp))
                    state.missingScopes.forEach { name ->
                        Text("• " + (if (name == "system") t.systemFramework else name), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Text(if (outdated && state.missingScopes.isEmpty()) t.companionUpdateTitle else t.statusTap, style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 10.dp).alpha(0.75f))
            }
        }
    }
}

@Composable
private fun HomeTab(state: XGoogleState, t: Texts, onSetting: (String, Boolean) -> Unit, onAssistant: () -> Unit,
    onStatus: () -> Unit, onRestartApps: () -> Unit, onRestore: () -> Unit) {
    StatusCard(state, t, onStatus)
    // While connecting, show the cached switches as usable; taps wait for the connection.
    val available = state.connected || state.loading
    val assistant = when (val info = state.assistant) {
        AssistantInfo.Loading -> t.assistantReading
        AssistantInfo.None -> t.assistantNone
        AssistantInfo.Hidden -> t.assistantTap
        is AssistantInfo.App -> t.assistantCurrent(info.label)
    }
    Group(t.groupAssistant) {
        SettingRow(0, 3, Icons.Rounded.RecordVoiceOver, t.defaultAssistant, assistant, onClick = onAssistant)
        ToggleRow(t, 1, 3, Icons.Rounded.Mic, t.heyGoogle, t.companion(state.companion),
            checked = state.features[FeaturePolicy.HEY_GOOGLE] == true, enabled = available && !state.busy,
            onChange = { onSetting(FeaturePolicy.HEY_GOOGLE, it) })
        ToggleRow(t, 2, 3, Icons.Rounded.PowerSettingsNew, t.powerKey, t.powerKeySummary,
            checked = state.features[FeaturePolicy.POWER_KEY] == true, enabled = available && !state.busy,
            onChange = { onSetting(FeaturePolicy.POWER_KEY, it) })
    }
    Group(t.groupSearch) {
        // Circle to Search's own options fold under it, so the list stays short.
        var circleOpen by rememberSaveable { mutableStateOf(false) }
        var lensOpen by rememberSaveable { mutableStateOf(false) }
        val extra = if (circleOpen) 5 else 0
        val count = 2 + extra + if (lensOpen) 1 else 0
        val circleOn = state.features[FeaturePolicy.NAVIGATION] == true
        SettingRow(0, count, Icons.Rounded.TouchApp, t.circle, t.circleSummary, { circleOpen = !circleOpen },
            modifier = Modifier.semantics { stateDescription = if (circleOn) t.switchOn else t.switchOff }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val turn by animateFloatAsState(if (circleOpen) 180f else 0f, label = "circleOptions")
                Icon(Icons.Rounded.ExpandMore, t.circleOptions, Modifier.padding(end = 8.dp).rotate(turn))
                SettingSwitch(circleOn, available) { onSetting(FeaturePolicy.NAVIGATION, it) }
            }
        }
        AnimatedVisibility(circleOpen, enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(), exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                ToggleRow(t, 1, count, Icons.Rounded.Translate, t.liveTranslate, t.liveTranslateSummary,
                    state.features[FeaturePolicy.LIVE_TRANSLATE] == true, available,
                    { onSetting(FeaturePolicy.LIVE_TRANSLATE, it) })
                ToggleRow(t, 2, count, Icons.Rounded.Vibration, t.circleVibrate, t.circleVibrateSummary,
                    state.features[FeaturePolicy.CIRCLE_VIBRATE] == true, available,
                    { onSetting(FeaturePolicy.CIRCLE_VIBRATE, it) })
                // Each gesture picks Circle to Search (on) or the XiaoAi function set in the system (off).
                ToggleRow(t, 3, count, Icons.Rounded.PanTool, t.circleLongPress, if (state.features[FeaturePolicy.CIRCLE_LONG_PRESS] == true) t.circleLongPressOn else t.circleLongPressOff,
                    state.features[FeaturePolicy.CIRCLE_LONG_PRESS] == true, available && circleOn,
                    { onSetting(FeaturePolicy.CIRCLE_LONG_PRESS, it) })
                ToggleRow(t, 4, count, Icons.Rounded.Swipe, t.circleSideHold, if (state.features[FeaturePolicy.CIRCLE_SIDE_HOLD] == true) t.circleSideHoldOn else t.circleSideHoldOff,
                    state.features[FeaturePolicy.CIRCLE_SIDE_HOLD] == true, available && circleOn,
                    { onSetting(FeaturePolicy.CIRCLE_SIDE_HOLD, it) })
                ToggleRow(t, 5, count, Icons.Rounded.Gesture, t.circleDoubleTap, if (state.features[FeaturePolicy.CIRCLE_DOUBLE_TAP] == true) t.circleDoubleTapOn else t.circleDoubleTapOff,
                    state.features[FeaturePolicy.CIRCLE_DOUBLE_TAP] == true, available && circleOn,
                    { onSetting(FeaturePolicy.CIRCLE_DOUBLE_TAP, it) })
            }
        }
        // The camera's Lens folds under Lens.
        val lensOn = state.features[FeaturePolicy.LENS] == true
        SettingRow(1 + extra, count, Icons.Rounded.CenterFocusStrong, t.lens, t.lensSummary, { lensOpen = !lensOpen },
            modifier = Modifier.semantics { stateDescription = if (lensOn) t.switchOn else t.switchOff }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val turn by animateFloatAsState(if (lensOpen) 180f else 0f, label = "lensOptions")
                Icon(Icons.Rounded.ExpandMore, t.lensOptions, Modifier.padding(end = 8.dp).rotate(turn))
                SettingSwitch(lensOn, available) { onSetting(FeaturePolicy.LENS, it) }
            }
        }
        AnimatedVisibility(lensOpen, enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(), exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
            ToggleRow(t, 2 + extra, count, Icons.Rounded.PhotoCamera, t.cameraLens, t.cameraLensSummary,
                state.features[FeaturePolicy.CAMERA_LENS] == true, available, { onSetting(FeaturePolicy.CAMERA_LENS, it) })
        }
    }
    Group(t.groupLock) {
        // The power-key Wallet folds under the lock screen shortcuts.
        var lockOpen by rememberSaveable { mutableStateOf(false) }
        val lockCount = if (lockOpen) 2 else 1
        val lockOn = state.features[FeaturePolicy.QUICK_ENTRIES] == true
        SettingRow(0, lockCount, Icons.Rounded.DashboardCustomize, t.lockShortcuts, t.lockShortcutsSummary, { lockOpen = !lockOpen },
            modifier = Modifier.semantics { stateDescription = if (lockOn) t.switchOn else t.switchOff }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val turn by animateFloatAsState(if (lockOpen) 180f else 0f, label = "lockOptions")
                Icon(Icons.Rounded.ExpandMore, t.lockOptions, Modifier.padding(end = 8.dp).rotate(turn))
                SettingSwitch(lockOn, available) { onSetting(FeaturePolicy.QUICK_ENTRIES, it) }
            }
        }
        AnimatedVisibility(lockOpen, enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(), exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
            ToggleRow(t, 1, lockCount, Icons.Rounded.AccountBalanceWallet, t.wallet, t.walletSummary,
                state.features[FeaturePolicy.WALLET] == true, available, { onSetting(FeaturePolicy.WALLET, it) })
        }
    }
    Group(t.groupSystem) {
        // The Google features that need Play services fold under the GMS switch.
        var gmsOpen by rememberSaveable { mutableStateOf(false) }
        val gmsExtra = if (gmsOpen) 5 else 0
        val systemCount = 3 + gmsExtra
        val gmsOn = state.features[FeaturePolicy.GMS] == true
        SettingRow(0, systemCount, Icons.Rounded.LockOpen, t.gms, t.gmsSummary, { gmsOpen = !gmsOpen },
            modifier = Modifier.semantics { stateDescription = if (gmsOn) t.switchOn else t.switchOff }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val turn by animateFloatAsState(if (gmsOpen) 180f else 0f, label = "gmsOptions")
                Icon(Icons.Rounded.ExpandMore, t.gmsOptions, Modifier.padding(end = 8.dp).rotate(turn))
                SettingSwitch(gmsOn, available) { onSetting(FeaturePolicy.GMS, it) }
            }
        }
        AnimatedVisibility(gmsOpen, enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(), exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                ToggleRow(t, 1, systemCount, Icons.Rounded.Settings, t.googleEntry, t.googleEntrySummary,
                    state.features[FeaturePolicy.GOOGLE_ENTRY] == true, available, { onSetting(FeaturePolicy.GOOGLE_ENTRY, it) })
                ToggleRow(t, 2, systemCount, Icons.Rounded.Timeline, t.timeline, t.timelineSummary,
                    state.features[FeaturePolicy.TIMELINE] == true, available, { onSetting(FeaturePolicy.TIMELINE, it) })
                ToggleRow(t, 3, systemCount, Icons.Rounded.Key, t.passkey, t.passkeySummary,
                    state.features[FeaturePolicy.PASSKEY] == true, available, { onSetting(FeaturePolicy.PASSKEY, it) })
                ToggleRow(t, 4, systemCount, Icons.Rounded.PhotoLibrary, t.photosUnlimited, t.photosUnlimitedSummary,
                    state.features[FeaturePolicy.PHOTOS_UNLIMITED] == true, available, { onSetting(FeaturePolicy.PHOTOS_UNLIMITED, it) })
                ToggleRow(t, 5, systemCount, Icons.Rounded.FolderSpecial, t.photosDcim, t.photosDcimSummary,
                    state.features[FeaturePolicy.PHOTOS_DCIM] == true, available, { onSetting(FeaturePolicy.PHOTOS_DCIM, it) })
            }
        }
        SettingRow(1 + gmsExtra, systemCount, Icons.Rounded.SettingsBackupRestore, t.restoreBackup, t.restoreBackupSummary,
            onClick = onRestore)
        SettingRow(2 + gmsExtra, systemCount, Icons.Rounded.RestartAlt, t.restartApps, t.restartAppsSummary,
            onClick = onRestartApps, enabled = state.connected && !state.restarting)
    }
}

@Composable
private fun SettingsTab(state: XGoogleState, t: Texts, onIconVisibility: (Boolean) -> Unit, onAbout: () -> Unit,
    onTelegram: () -> Unit, onLanguage: (AppLanguage) -> Unit, onLook: (Look) -> Unit, onCheckUpdates: () -> Unit,
    onSupport: () -> Unit) {
    Group(t.groupLook) {
        LanguageRow(0, 3, state.language, t, onLanguage)
        ToggleRow(t, 1, 3, Icons.Rounded.Palette, t.hyperStyle, t.hyperStyleSummary,
            false, true, { onLook(state.look.copy(hyper = it)) })
        ToggleRow(t, 2, 3, Icons.Rounded.BlurOn, t.blur, t.blurSummary,
            state.look.blur, true, { onLook(state.look.copy(blur = it)) })
    }
    Group(t.groupApp) {
        SettingRow(0, 4, Icons.Rounded.SystemUpdate, t.checkUpdates, when (state.update) {
            UpdateStatus.IDLE -> t.version(BuildConfig.VERSION_NAME)
            UpdateStatus.CHECKING -> t.updateChecking
            UpdateStatus.LATEST -> t.version(BuildConfig.VERSION_NAME) + " · " + t.updateLatest
            UpdateStatus.AVAILABLE -> t.updateTitle + ": " + state.updateVersion
            UpdateStatus.FAILED -> t.updateFailed
        }, onClick = onCheckUpdates, enabled = state.update != UpdateStatus.CHECKING)
        SettingRow(1, 4, Icons.Rounded.Forum, t.support, t.supportSummary, onClick = onSupport)
        ToggleRow(t, 2, 4, Icons.Rounded.VisibilityOff, t.hideIcon, t.hideIconSummary,
            state.iconHidden, true, onIconVisibility)
        SettingRow(3, 4, Icons.Rounded.Info, t.about, t.aboutSummary(BuildConfig.VERSION_NAME),
            onClick = onTelegram)
    }
}

@Composable
private fun LanguageRow(index: Int, count: Int, current: AppLanguage, t: Texts, onLanguage: (AppLanguage) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Box {
        SettingRow(index, count, Icons.Rounded.Language, t.language, current.label, onClick = { open = true })
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            AppLanguage.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.label) },
                    trailingIcon = if (language == current) { { Icon(Icons.Rounded.Check, null) } } else null,
                    onClick = { open = false; onLanguage(language) }
                )
            }
        }
    }
}

@Composable
private fun Group(title: String, content: @Composable () -> Unit) {
    Column(Modifier.padding(top = 24.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp), content = { content() })
    }
}

@Composable
private fun SettingRow(index: Int, count: Int, icon: ImageVector, title: String, summary: String,
    onClick: () -> Unit, enabled: Boolean = true, modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null) {
    SegmentedListItem(
        modifier = modifier.fillMaxWidth(),
        shapes = ListItemDefaults.segmentedShapes(index, count).let {
            if (count == 1) it.copy(shape = MaterialTheme.shapes.large) else it
        },
        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceBright,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
            supportingContentColor = MaterialTheme.colorScheme.onSurfaceVariant),
        enabled = enabled,
        onClick = onClick,
        verticalAlignment = Alignment.CenterVertically,
        leadingContent = {
            Box(Modifier.size(40.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f), CircleShape),
                contentAlignment = Alignment.Center) {
                Icon(icon, null, Modifier.size(22.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        trailingContent = trailing ?: { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) },
        supportingContent = { Text(summary) },
        content = { Text(title) }
    )
}

@Composable
private fun ToggleRow(t: Texts, index: Int, count: Int, icon: ImageVector, title: String, summary: String,
    checked: Boolean, enabled: Boolean, onChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    SettingRow(index, count, icon, title, summary, { onChange(!checked) }, enabled,
        modifier.semantics { role = Role.Switch; stateDescription = if (checked) t.switchOn else t.switchOff }) {
        SettingSwitch(checked, enabled, onChange = null)
    }
}

/** With onChange null the row handles the tap. */
@Composable
private fun SettingSwitch(checked: Boolean, enabled: Boolean, onChange: ((Boolean) -> Unit)?) {
    Switch(
        checked = checked,
        onCheckedChange = onChange,
        enabled = enabled,
        thumbContent = if (checked || enabled) {
            { Icon(if (checked) Icons.Rounded.Check else Icons.Rounded.Close, null, Modifier.size(SwitchDefaults.IconSize)) }
        } else null,
        colors = SwitchDefaults.colors(
            checkedIconColor = MaterialTheme.colorScheme.primary,
            uncheckedIconColor = MaterialTheme.colorScheme.surfaceContainerHighest)
    )
}

@Composable
private fun XGoogleDialog(state: XGoogleState, t: Texts, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    if (state.dialog == DialogKind.NONE) return
    val title = when (state.dialog) {
        DialogKind.STATUS -> t.statusTitle
        DialogKind.INSTALL -> t.installTitle
        DialogKind.DISABLED -> t.disabledTitle
        DialogKind.INSTALLED -> t.installedTitle
        DialogKind.HIDE_ICON -> t.hideIconTitle
        DialogKind.RESTART -> t.restartTitle
        DialogKind.UPDATE -> t.updateTitle
        DialogKind.COMPANION_UPDATE -> t.companionUpdateTitle
        DialogKind.COMPANION_OFFER -> t.companionOfferTitle
        DialogKind.COMPANION_DONE -> t.installedTitle
        DialogKind.RESTORE_INSTALL -> t.restoreInstallTitle
        else -> t.errorTitle
    }
    val message = when (state.dialog) {
        DialogKind.STATUS -> t.statusMessage
        DialogKind.INSTALL -> t.installMessage
        DialogKind.DISABLED -> t.disabledMessage
        DialogKind.INSTALLED -> t.installedMessage
        DialogKind.HIDE_ICON -> t.hideIconMessage
        DialogKind.RESTART -> t.restartMessage
        DialogKind.UPDATE -> t.updateMessage(state.updateVersion)
        DialogKind.COMPANION_UPDATE -> t.companionUpdateMessage
        DialogKind.COMPANION_OFFER -> t.companionOfferMessage
        DialogKind.COMPANION_DONE -> t.companionDoneMessage
        DialogKind.RESTORE_INSTALL -> t.restoreInstallMessage
        else -> state.dialogText
    }
    val confirm = when (state.dialog) {
        DialogKind.INSTALL -> t.install
        DialogKind.HIDE_ICON -> t.hide
        DialogKind.RESTART -> t.restart
        DialogKind.UPDATE -> t.download
        DialogKind.COMPANION_UPDATE -> t.update
        DialogKind.COMPANION_OFFER, DialogKind.RESTORE_INSTALL -> t.install
        else -> t.ok
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.extraLarge,
        icon = { Icon(if (state.dialog in setOf(DialogKind.INSTALL, DialogKind.INSTALLED, DialogKind.DISABLED,
            DialogKind.COMPANION_UPDATE, DialogKind.COMPANION_OFFER, DialogKind.COMPANION_DONE))
            Icons.Rounded.Extension else if (state.dialog == DialogKind.HIDE_ICON) Icons.Rounded.VisibilityOff
            else if (state.dialog == DialogKind.RESTART) Icons.Rounded.RestartAlt
            else if (state.dialog == DialogKind.RESTORE_INSTALL) Icons.Rounded.SettingsBackupRestore else Icons.Rounded.Info, null) },
        title = { Text(title) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(message)
            }
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text(confirm) } },
        dismissButton = if (state.dialog in setOf(DialogKind.INSTALL, DialogKind.HIDE_ICON, DialogKind.RESTART, DialogKind.UPDATE,
        DialogKind.COMPANION_UPDATE, DialogKind.COMPANION_OFFER, DialogKind.RESTORE_INSTALL)) {
            { TextButton(onClick = onDismiss) { Text(t.cancel) } }
        } else null
    )
}

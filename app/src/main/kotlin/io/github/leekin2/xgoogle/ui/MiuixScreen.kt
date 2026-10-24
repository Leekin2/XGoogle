// HyperOS-style components from compose-miuix-ui/miuix (Apache-2.0); see NOTICE.txt.
package io.github.leekin2.xgoogle.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.border
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import androidx.compose.runtime.key
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.semantics.selected
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Gesture
import androidx.compose.material.icons.rounded.PanTool
import androidx.compose.material.icons.rounded.Swipe
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.CardDefaults
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DashboardCustomize
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.BlurOn
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.FolderSpecial
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import io.github.leekin2.xgoogle.BuildConfig
import io.github.leekin2.xgoogle.FeaturePolicy
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.window.WindowDialog
import top.yukonga.miuix.kmp.window.WindowListPopup

@Composable
fun MiuixScreen(
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
    val bar = MiuixScrollBehavior()
    // Frosted bars: the list is recorded as a backdrop and blurred behind the top and bottom bars.
    val blur = state.look.blur
    val backdrop = rememberLayerBackdrop()
    val surface = MiuixTheme.colorScheme.surface
    Scaffold(
        topBar = {
            TopAppBar(
                title = if (tab == 0) "XGoogle" else t.tabSettings,
                subtitle = if (tab == 0) t.subtitle else "XGoogle",
                modifier = if (blur) Modifier.textureBlur(backdrop, RectangleShape, 25f,
                    colors = BlurColors(blendColors = listOf(BlendColorEntry(surface.copy(alpha = 0.8f))))) else Modifier,
                color = if (blur) Color.Transparent else surface,
                scrollBehavior = bar
            )
        },
        bottomBar = {
            FloatingTabs(t, tab, onTab, if (blur) backdrop else null)
        }
    ) { padding ->
        // While connecting, show the cached switches as usable; taps wait for the connection.
        val available = state.connected || state.loading
        // Switching tabs starts the new tab at its top.
        key(tab) {
            LazyColumn(
                Modifier.fillMaxHeight()
                    .then(if (blur) Modifier.layerBackdrop(backdrop).background(surface) else Modifier)
                    .overScrollVertical().nestedScroll(bar.nestedScrollConnection),
                contentPadding = PaddingValues(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding()),
                overscrollEffect = null
            ) {
                if (tab == 1) item {
                    SettingsTab(state, t, onIconVisibility, onTelegram, onLanguage, onLook, onCheckUpdates, onSupport)
                    Spacer(Modifier.height(24.dp))
                } else item {
                    StatusCard(state, t, onStatus)
                    val assistant = when (val info = state.assistant) {
                        AssistantInfo.Loading -> t.assistantReading
                        AssistantInfo.None -> t.assistantNone
                        AssistantInfo.Hidden -> t.assistantTap
                        is AssistantInfo.App -> t.assistantCurrent(info.label)
                    }
                    Group(t.groupAssistant) {
                        ArrowPreference(title = t.defaultAssistant, summary = assistant,
                            startAction = { RowIcon(Icons.Rounded.RecordVoiceOver) }, onClick = onAssistant)
                        Toggle(Icons.Rounded.Mic, t.heyGoogle, t.companion(state.companion),
                            state.features[FeaturePolicy.HEY_GOOGLE] == true, available && !state.busy,
                        ) { onSetting(FeaturePolicy.HEY_GOOGLE, it) }
                        Toggle(Icons.Rounded.PowerSettingsNew, t.powerKey, t.powerKeySummary,
                            state.features[FeaturePolicy.POWER_KEY] == true, available && !state.busy
                        ) { onSetting(FeaturePolicy.POWER_KEY, it) }
                    }
                    Group(t.groupSearch) {
                        // Circle to Search's own options fold under it, so the list stays short.
                        var circleOpen by rememberSaveable { mutableStateOf(false) }
                        val circleOn = state.features[FeaturePolicy.NAVIGATION] == true
                        val turn by animateFloatAsState(if (circleOpen) 180f else 0f, label = "circleOptions")
                        BasicComponent(
                            modifier = Modifier.semantics { stateDescription = if (circleOn) t.switchOn else t.switchOff },
                            title = t.circle, summary = t.circleSummary,
                            startAction = { RowIcon(Icons.Rounded.TouchApp) },
                            endActions = {
                                Icon(Icons.Rounded.ExpandMore, t.circleOptions,
                                    Modifier.padding(end = 8.dp).rotate(turn),
                                    tint = MiuixTheme.colorScheme.onSurfaceVariantActions)
                                Switch(circleOn, { onSetting(FeaturePolicy.NAVIGATION, it) }, enabled = available)
                            },
                            onClick = { circleOpen = !circleOpen }
                        )
                        AnimatedVisibility(circleOpen,
                            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
                            Column {
                                Toggle(Icons.Rounded.Translate, t.liveTranslate, t.liveTranslateSummary,
                                    state.features[FeaturePolicy.LIVE_TRANSLATE] == true, available
                                ) { onSetting(FeaturePolicy.LIVE_TRANSLATE, it) }
                                Toggle(Icons.Rounded.Vibration, t.circleVibrate, t.circleVibrateSummary,
                                    state.features[FeaturePolicy.CIRCLE_VIBRATE] == true, available
                                ) { onSetting(FeaturePolicy.CIRCLE_VIBRATE, it) }
                                // Each gesture picks Circle to Search (on) or the XiaoAi function set in the system (off).
                                Toggle(Icons.Rounded.PanTool, t.circleLongPress, if (state.features[FeaturePolicy.CIRCLE_LONG_PRESS] == true) t.circleLongPressOn else t.circleLongPressOff,
                                    state.features[FeaturePolicy.CIRCLE_LONG_PRESS] == true, available && circleOn
                                ) { onSetting(FeaturePolicy.CIRCLE_LONG_PRESS, it) }
                                Toggle(Icons.Rounded.Swipe, t.circleSideHold, if (state.features[FeaturePolicy.CIRCLE_SIDE_HOLD] == true) t.circleSideHoldOn else t.circleSideHoldOff,
                                    state.features[FeaturePolicy.CIRCLE_SIDE_HOLD] == true, available && circleOn
                                ) { onSetting(FeaturePolicy.CIRCLE_SIDE_HOLD, it) }
                                Toggle(Icons.Rounded.Gesture, t.circleDoubleTap, if (state.features[FeaturePolicy.CIRCLE_DOUBLE_TAP] == true) t.circleDoubleTapOn else t.circleDoubleTapOff,
                                    state.features[FeaturePolicy.CIRCLE_DOUBLE_TAP] == true, available && circleOn
                                ) { onSetting(FeaturePolicy.CIRCLE_DOUBLE_TAP, it) }
                            }
                        }
                        // The camera's Lens folds under Lens.
                        var lensOpen by rememberSaveable { mutableStateOf(false) }
                        val lensOn = state.features[FeaturePolicy.LENS] == true
                        val lensTurn by animateFloatAsState(if (lensOpen) 180f else 0f, label = "lensOptions")
                        BasicComponent(
                            modifier = Modifier.semantics { stateDescription = if (lensOn) t.switchOn else t.switchOff },
                            title = t.lens, summary = t.lensSummary,
                            startAction = { RowIcon(Icons.Rounded.CenterFocusStrong) },
                            endActions = {
                                Icon(Icons.Rounded.ExpandMore, t.lensOptions,
                                    Modifier.padding(end = 8.dp).rotate(lensTurn),
                                    tint = MiuixTheme.colorScheme.onSurfaceVariantActions)
                                Switch(lensOn, { onSetting(FeaturePolicy.LENS, it) }, enabled = available)
                            },
                            onClick = { lensOpen = !lensOpen }
                        )
                        AnimatedVisibility(lensOpen,
                            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
                            Toggle(Icons.Rounded.PhotoCamera, t.cameraLens, t.cameraLensSummary,
                                state.features[FeaturePolicy.CAMERA_LENS] == true, available) { onSetting(FeaturePolicy.CAMERA_LENS, it) }
                        }
                    }
                    Group(t.groupLock) {
                        // The power-key Wallet folds under the lock screen shortcuts.
                        var lockOpen by rememberSaveable { mutableStateOf(false) }
                        val lockOn = state.features[FeaturePolicy.QUICK_ENTRIES] == true
                        val lockTurn by animateFloatAsState(if (lockOpen) 180f else 0f, label = "lockOptions")
                        BasicComponent(
                            modifier = Modifier.semantics { stateDescription = if (lockOn) t.switchOn else t.switchOff },
                            title = t.lockShortcuts, summary = t.lockShortcutsSummary,
                            startAction = { RowIcon(Icons.Rounded.DashboardCustomize) },
                            endActions = {
                                Icon(Icons.Rounded.ExpandMore, t.lockOptions,
                                    Modifier.padding(end = 8.dp).rotate(lockTurn),
                                    tint = MiuixTheme.colorScheme.onSurfaceVariantActions)
                                Switch(lockOn, { onSetting(FeaturePolicy.QUICK_ENTRIES, it) }, enabled = available)
                            },
                            onClick = { lockOpen = !lockOpen }
                        )
                        AnimatedVisibility(lockOpen,
                            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
                            Toggle(Icons.Rounded.AccountBalanceWallet, t.wallet, t.walletSummary,
                                state.features[FeaturePolicy.WALLET] == true, available) { onSetting(FeaturePolicy.WALLET, it) }
                        }
                    }
                    Group(t.groupSystem) {
                        // The Google features that need Play services fold under the GMS switch.
                        var gmsOpen by rememberSaveable { mutableStateOf(false) }
                        val gmsOn = state.features[FeaturePolicy.GMS] == true
                        val gmsTurn by animateFloatAsState(if (gmsOpen) 180f else 0f, label = "gmsOptions")
                        BasicComponent(
                            modifier = Modifier.semantics { stateDescription = if (gmsOn) t.switchOn else t.switchOff },
                            title = t.gms, summary = t.gmsSummary,
                            startAction = { RowIcon(Icons.Rounded.LockOpen) },
                            endActions = {
                                Icon(Icons.Rounded.ExpandMore, t.gmsOptions,
                                    Modifier.padding(end = 8.dp).rotate(gmsTurn),
                                    tint = MiuixTheme.colorScheme.onSurfaceVariantActions)
                                Switch(gmsOn, { onSetting(FeaturePolicy.GMS, it) }, enabled = available)
                            },
                            onClick = { gmsOpen = !gmsOpen }
                        )
                        AnimatedVisibility(gmsOpen,
                            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()) {
                            Column {
                                Toggle(Icons.Rounded.Settings, t.googleEntry, t.googleEntrySummary,
                                    state.features[FeaturePolicy.GOOGLE_ENTRY] == true, available) { onSetting(FeaturePolicy.GOOGLE_ENTRY, it) }
                                Toggle(Icons.Rounded.Timeline, t.timeline, t.timelineSummary,
                                    state.features[FeaturePolicy.TIMELINE] == true, available) { onSetting(FeaturePolicy.TIMELINE, it) }
                                Toggle(Icons.Rounded.Key, t.passkey, t.passkeySummary,
                                    state.features[FeaturePolicy.PASSKEY] == true, available) { onSetting(FeaturePolicy.PASSKEY, it) }
                                Toggle(Icons.Rounded.PhotoLibrary, t.photosUnlimited, t.photosUnlimitedSummary,
                                    state.features[FeaturePolicy.PHOTOS_UNLIMITED] == true, available) { onSetting(FeaturePolicy.PHOTOS_UNLIMITED, it) }
                                Toggle(Icons.Rounded.FolderSpecial, t.photosDcim, t.photosDcimSummary,
                                    state.features[FeaturePolicy.PHOTOS_DCIM] == true, available) { onSetting(FeaturePolicy.PHOTOS_DCIM, it) }
                            }
                        }
                        ArrowPreference(title = t.restoreBackup, summary = t.restoreBackupSummary,
                            startAction = { RowIcon(Icons.Rounded.SettingsBackupRestore) }, onClick = onRestore)
                        ArrowPreference(title = t.restartApps, summary = t.restartAppsSummary,
                            startAction = { RowIcon(Icons.Rounded.RestartAlt) }, onClick = onRestartApps,
                            enabled = state.connected && !state.restarting)
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
    XGoogleDialog(state, t, onDialogConfirm, onDialogDismiss)
}

/** Floating Home/Settings bar; Miuix's own floating bar shows icons only, so labels are drawn here. */
@Composable
private fun FloatingTabs(t: Texts, selected: Int, onSelect: (Int) -> Unit, backdrop: LayerBackdrop?) {
    val colors = MiuixTheme.colorScheme
    // With a backdrop the bar is frosted glass over the list; otherwise a solid pill.
    val surface = if (backdrop == null) Modifier.background(colors.surfaceContainerHigh)
        else Modifier.textureBlur(backdrop, CircleShape, 25f,
            colors = BlurColors(blendColors = listOf(BlendColorEntry(colors.surfaceContainerHigh.copy(alpha = 0.6f)))))
            .border(1.dp, colors.onSurface.copy(alpha = 0.1f), CircleShape)
    Box(Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 16.dp), contentAlignment = Alignment.Center) {
        Row(Modifier.shadow(8.dp, CircleShape).clip(CircleShape).then(surface).padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(Icons.Rounded.Home to t.tabHome, Icons.Rounded.Settings to t.tabSettings).forEachIndexed { index, (icon, label) ->
                val active = index == selected
                val tint = if (active) colors.onPrimary else colors.onSurfaceVariantSummary
                Row(Modifier.clip(CircleShape).background(if (active) colors.primary else Color.Transparent)
                    .clickable { onSelect(index) }.semantics { this.selected = active }
                    .padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, Modifier.size(22.dp), tint = tint)
                    Spacer(Modifier.width(8.dp))
                    Text(label, color = tint, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

/** KernelSU-style status: green working, yellow missing scopes (listed), red inactive. Opens LSPosed. */
@Composable
private fun StatusCard(state: XGoogleState, t: Texts, onStatus: () -> Unit) {
    val colors = MiuixTheme.colorScheme
    val dark = colors.surface.luminance() < 0.5f
    val waiting = state.loading && !state.connected
    val outdated = state.companion == CompanionState.OUTDATED
    val warn = state.missingScopes.isNotEmpty() || outdated
    val (container, content) = when {
        waiting -> colors.secondaryContainer to colors.onSecondaryContainer
        !state.connected -> (if (dark) Color(0xFF5C1A1A) else Color(0xFFFFDAD6)) to (if (dark) Color(0xFFFFDAD6) else Color(0xFF6B0F0F))
        warn -> if (dark) Color(0xFF4A3F00) to Color(0xFFFFE08A) else Color(0xFFFFF0B3) to Color(0xFF574500)
        else -> if (dark) Color(0xFF1F4D26) to Color(0xFFB9F0C0) else Color(0xFFC9EFCB) to Color(0xFF14501E)
    }
    Card(Modifier.padding(horizontal = 12.dp).padding(top = 12.dp), colors = CardDefaults.defaultColors(container, content),
        onClick = onStatus, showIndication = true) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 22.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(when {
                waiting -> Icons.Rounded.HourglassTop
                !state.connected -> Icons.Rounded.HighlightOff
                warn -> Icons.Rounded.WarningAmber
                else -> Icons.Rounded.CheckCircle
            }, null, Modifier.size(36.dp), tint = content)
            Spacer(Modifier.width(18.dp))
            Column(Modifier.weight(1f)) {
                Text(when {
                    waiting -> t.connecting
                    !state.connected -> t.inactive
                    state.missingScopes.isNotEmpty() -> t.statusIncomplete
                    outdated -> t.companion(CompanionState.OUTDATED)
                    else -> t.statusWorking
                }, color = content, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text(t.version(BuildConfig.VERSION_NAME) + " · API 102", color = content, fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp))
                if (state.connected && state.missingScopes.isNotEmpty()) {
                    Text(t.statusMissingIntro, color = content, fontSize = 14.sp, modifier = Modifier.padding(top = 10.dp))
                    state.missingScopes.forEach { name ->
                        Text("• " + (if (name == "system") t.systemFramework else name), color = content, fontSize = 14.sp)
                    }
                }
                Text(if (outdated && state.missingScopes.isEmpty()) t.companionUpdateTitle else t.statusTap, color = content.copy(alpha = 0.75f), fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
            }
        }
    }
}

@Composable
private fun SettingsTab(state: XGoogleState, t: Texts, onIconVisibility: (Boolean) -> Unit, onTelegram: () -> Unit,
    onLanguage: (AppLanguage) -> Unit, onLook: (Look) -> Unit, onCheckUpdates: () -> Unit, onSupport: () -> Unit) {
    Spacer(Modifier.height(6.dp))
    Group(t.groupLook) {
        LanguageRow(state.language, t, onLanguage)
        ThemeRow(state.look, t, onLook)
        Toggle(Icons.Rounded.Palette, t.hyperStyle, t.hyperStyleSummary, true, true) {
            onLook(state.look.copy(hyper = it)) }
        Toggle(Icons.Rounded.Wallpaper, t.monet, t.monetSummary, state.look.monet, true) {
            onLook(state.look.copy(monet = it)) }
        Toggle(Icons.Rounded.BlurOn, t.blur, t.blurSummary, state.look.blur, true) {
            onLook(state.look.copy(blur = it)) }
    }
    Group(t.groupApp) {
        ArrowPreference(title = t.checkUpdates, summary = when (state.update) {
            UpdateStatus.IDLE -> t.version(BuildConfig.VERSION_NAME)
            UpdateStatus.CHECKING -> t.updateChecking
            UpdateStatus.LATEST -> t.version(BuildConfig.VERSION_NAME) + " · " + t.updateLatest
            UpdateStatus.AVAILABLE -> t.updateTitle + ": " + state.updateVersion
            UpdateStatus.FAILED -> t.updateFailed
        }, startAction = { RowIcon(Icons.Rounded.SystemUpdate) }, onClick = onCheckUpdates,
            enabled = state.update != UpdateStatus.CHECKING)
        ArrowPreference(title = t.support, summary = t.supportSummary,
            startAction = { RowIcon(Icons.Rounded.Forum) }, onClick = onSupport)
        Toggle(Icons.Rounded.VisibilityOff, t.hideIcon, t.hideIconSummary,
            state.iconHidden, true, onIconVisibility)
        ArrowPreference(title = t.about, summary = t.aboutSummary(BuildConfig.VERSION_NAME),
            startAction = { RowIcon(Icons.Rounded.Info) }, onClick = onTelegram)
    }
}

/** Each tap moves the theme on, system → light → dark. */
@Composable
private fun ThemeRow(look: Look, t: Texts, onLook: (Look) -> Unit) {
    val (icon, label) = when (look.theme) {
        1 -> Icons.Rounded.LightMode to t.themeLight
        2 -> Icons.Rounded.DarkMode to t.themeDark
        else -> Icons.Rounded.BrightnessAuto to t.themeSystem
    }
    ArrowPreference(title = t.theme, summary = label, startAction = { RowIcon(icon) },
        onClick = { onLook(look.copy(theme = (look.theme + 1) % 3)) })
}

@Composable
private fun LanguageRow(current: AppLanguage, t: Texts, onLanguage: (AppLanguage) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Box {
        ArrowPreference(title = t.language, summary = current.label,
            startAction = { RowIcon(Icons.Rounded.Language) }, onClick = { open = true })
        WindowListPopup(show = open, onDismissRequest = { open = false },
            alignment = top.yukonga.miuix.kmp.basic.PopupPositionProvider.Align.End) {
            ListPopupColumn {
                val languages = AppLanguage.entries
                languages.forEachIndexed { index, language ->
                    DropdownImpl(text = language.label, optionSize = languages.size,
                        isSelected = language == current, index = index,
                        onSelectedIndexChange = { open = false; onLanguage(languages[it]) })
                }
            }
        }
    }
}

@Composable
private fun Group(title: String, content: @Composable () -> Unit) {
    SmallTitle(title, Modifier.padding(top = 6.dp))
    Card(Modifier.padding(horizontal = 12.dp).padding(bottom = 6.dp)) { content() }
}

@Composable
private fun RowIcon(icon: ImageVector) {
    Icon(icon, null, Modifier.padding(end = 16.dp).size(24.dp), tint = MiuixTheme.colorScheme.onSurfaceVariantSummary)
}

@Composable
private fun Toggle(icon: ImageVector, title: String, summary: String, checked: Boolean, enabled: Boolean,
    onChange: (Boolean) -> Unit) {
    SwitchPreference(
        checked = checked, onCheckedChange = onChange, title = title, summary = summary,
        startAction = { RowIcon(icon) }, enabled = enabled,
        modifier = Modifier.semantics { role = Role.Switch }
    )
}

@Composable
private fun XGoogleDialog(state: XGoogleState, t: Texts, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val show = state.dialog != DialogKind.NONE
    // Keep the last dialog's text while it animates out.
    var last by remember { mutableStateOf(state.dialog) }
    var lastText by remember { mutableStateOf(state.dialogText) }
    if (show) { last = state.dialog; lastText = state.dialogText }
    val title = when (last) {
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
    val message = when (last) {
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
        else -> lastText
    }
    val confirm = when (last) {
        DialogKind.INSTALL -> t.install
        DialogKind.HIDE_ICON -> t.hide
        DialogKind.RESTART -> t.restart
        DialogKind.UPDATE -> t.download
        DialogKind.COMPANION_UPDATE -> t.update
        DialogKind.COMPANION_OFFER, DialogKind.RESTORE_INSTALL -> t.install
        else -> t.ok
    }
    val cancellable = last in setOf(DialogKind.INSTALL, DialogKind.HIDE_ICON, DialogKind.RESTART, DialogKind.UPDATE,
        DialogKind.COMPANION_UPDATE, DialogKind.COMPANION_OFFER, DialogKind.RESTORE_INSTALL)
    WindowDialog(show = show, title = title, onDismissRequest = onDismiss) {
        Column {
            Text(message, Modifier.fillMaxWidth().heightIn(max = 420.dp)
                .verticalScroll(rememberScrollState()), color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
            Spacer(Modifier.height(24.dp))
            Row {
                if (cancellable) {
                    TextButton(t.cancel, onDismiss, Modifier.weight(1f))
                    Spacer(Modifier.size(16.dp))
                }
                TextButton(confirm, onConfirm, Modifier.weight(1f), colors = ButtonDefaults.textButtonColorsPrimary())
            }
        }
    }
}

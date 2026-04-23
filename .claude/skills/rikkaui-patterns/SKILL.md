---
name: rikkaui-patterns
description: >
  RikkaUI component patterns for this Compose Multiplatform project. RikkaUI is the
  UI library used for ALL screens — it replaces Material3 entirely and is built on
  compose.foundation. Invoke this skill for ANY UI work in composeApp/: building screens,
  adding buttons, inputs, dialogs, bottom sheets, navigation bars, tab switchers, avatars,
  icons, loading spinners, toggles, cards, or scaffolds. Also invoke it when the user asks
  about spacing or color tokens (e.g. hardcoded 16.dp → RikkaTheme.spacing), wants to know
  which component to use for a layout problem, or is editing any file under
  composeApp/src/commonMain/kotlin/org/tcl/app/*/presentation/. When in doubt about whether
  a question is about UI in this app, invoke this skill — it covers Button, Sheet,
  AlertDialog, Tabs, NavigationBar, Card, Input, Text, Avatar, Icon, Scaffold, TopAppBar,
  Fab, Spinner, Toggle, Textarea, Label, and IconButton.
---

# RikkaUI Patterns

## Core rules

- **Never hardcode colors or spacing.** Always use `RikkaTheme.colors.*` and `RikkaTheme.spacing.*`.
- **No Material3 imports** in UI code — RikkaUI replaces Material entirely.
- **MVVM shape:** every screen composable takes `(state: XState, onAction: (XAction) -> Unit)` — no ViewModel references inside composables.
- Prefer RikkaUI components over standard Compose equivalents (e.g. use `Text` from RikkaUI, not `androidx.compose.material3.Text`).

---

## Theme setup

```kotlin
// App.kt — wrap the entire app once
RikkaTheme(palette = RikkaPalette.Zinc.resolve(isDark = false)) {
    // content
}
```

### Token access

```kotlin
RikkaTheme.colors.background
RikkaTheme.colors.primary
RikkaTheme.colors.destructive
RikkaTheme.colors.success
RikkaTheme.colors.onPrimary
RikkaTheme.colors.onBackground
RikkaTheme.colors.onMuted

RikkaTheme.spacing.sm
RikkaTheme.spacing.md
RikkaTheme.spacing.lg
RikkaTheme.spacing.xl
```

---

## Components

### Button

```kotlin
Button(
    text = "Label",
    onClick = { },
    variant = ButtonVariant.Default,   // Default | Secondary | Ghost | Outline | Destructive
    leadingIcon = RikkaIcons.Plus,     // optional
    enabled = true,
    modifier = Modifier
)
```

### IconButton

```kotlin
IconButton(
    icon = RikkaIcons.ArrowLeft,
    onClick = { },
    contentDescription = "Back",
    modifier = Modifier
)
```

### Input

```kotlin
Input(
    value = state.email,
    onValueChange = { onAction(Action.EmailChanged(it)) },
    placeholder = "email@example.com",
    label = "Email"
)
```

### Textarea

```kotlin
Textarea(
    value = state.body,
    onValueChange = { onAction(Action.BodyChanged(it)) },
    label = "Message"
)
```

### Label

```kotlin
Label(text = "Field Name", required = true)
```

### Text

```kotlin
Text(
    text = "Hello",
    variant = TextVariant.H1,            // H1 | Lead | Large | Small
    color = RikkaTheme.colors.onBackground,
    textAlign = TextAlign.Center
)
```

### Avatar

```kotlin
Avatar(
    fallback = "AB",
    size = AvatarSize.Default            // Default | Lg
)
```

### Icon

```kotlin
Icon(
    imageVector = RikkaIcons.Star,
    contentDescription = null,           // null for decorative icons
    tint = RikkaTheme.colors.primary
)
```

### Card + CardContent

```kotlin
Card(modifier = Modifier.fillMaxWidth()) {
    CardContent {
        // children
    }
}
```

### Scaffold + TopAppBar

```kotlin
Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(text = "Title") },
            navigationIcon = {
                IconButton(icon = RikkaIcons.ArrowLeft, onClick = onBack, contentDescription = "Back")
            },
            actions = {
                IconButton(icon = RikkaIcons.Check, onClick = onSave, contentDescription = "Save")
            }
        )
    },
    bottomBar = { /* NavigationBar */ },
    floatingActionButton = {
        Fab(icon = RikkaIcons.Plus, label = "New", onClick = { })
    }
) { padding ->
    Column(modifier = Modifier.padding(padding)) { }
}
```

### Sheet (bottom sheet / modal)

```kotlin
Sheet(
    open = state.showSheet,
    onDismiss = { onAction(Action.DismissSheet) },
    side = SheetSide.Bottom,
    animation = SheetAnimation.Slide
) {
    SheetHeader { Text(text = "Title") }
    SheetContent {
        // content
    }
}
```

### AlertDialog

```kotlin
AlertDialog(
    open = state.showDeleteDialog,
    onDismiss = { onAction(Action.DismissDialog) },
    animation = AlertDialogAnimation.FadeScale
) {
    AlertDialogHeader { Text(text = "Are you sure?") }
    AlertDialogFooter {
        AlertDialogCancel(onClick = { onAction(Action.DismissDialog) })
        AlertDialogAction(
            onClick = { onAction(Action.ConfirmDelete) },
            variant = AlertDialogActionVariant.Destructive
        )
    }
}
```

### NavigationBar + NavigationBarItem

```kotlin
NavigationBar {
    items.forEach { item ->
        NavigationBarItem(
            selected = currentRoute == item.route,
            onClick = { navigate(item.route) },
            icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
            label = { Text(text = item.label) },
            animation = NavigationBarAnimation.Tween
        )
    }
}
```

### Tabs

```kotlin
Column {
    TabList(
        selected = state.tabIndex,
        onSelect = { onAction(Action.TabSelected(it)) },
        animation = TabAnimation.Spring
    ) {
        Tab(text = "Tab One")
        Tab(text = "Tab Two")
    }
    TabContent(selected = state.tabIndex) {
        // index-0 content
        // index-1 content
    }
}
```

### Spinner

```kotlin
if (state.isLoading) Spinner()
```

### Toggle

```kotlin
Toggle(
    checked = state.isEnabled,
    onCheckedChange = { onAction(Action.ToggleEnabled(it)) },
    label = "Enable feature"
)
```

### Fab

```kotlin
Fab(
    icon = RikkaIcons.Plus,
    label = "Book Court",
    onClick = { onAction(Action.StartBooking) }
)
```

---

## Icon system

Built-in icons (from `RikkaIcons`):
`ArrowLeft`, `ArrowRight`, `Check`, `Plus`, `Copy`, `Star`, `User`, `Mail`, `Send`, `Trash`

```kotlin
// Lucide pack — register once in App.kt before RikkaTheme {}
LucidePack.register()
```

For app/decorative icons that need no accessibility description:
```kotlin
DecorativeAppIcon(...)
```

---

## Import reference

```kotlin
// Theme
import zed.rainxch.rikkaui.foundation.theme.RikkaTheme
import zed.rainxch.rikkaui.foundation.theme.palette.RikkaPalette

// Components (all from components artifact)
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.input.Textarea
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.text.TextVariant
import zed.rainxch.rikkaui.components.ui.avatar.Avatar
import zed.rainxch.rikkaui.components.ui.avatar.AvatarSize
import zed.rainxch.rikkaui.components.ui.icon.Icon
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.card.Card
import zed.rainxch.rikkaui.components.ui.card.CardContent
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.components.ui.fab.Fab
import zed.rainxch.rikkaui.components.ui.sheet.Sheet
import zed.rainxch.rikkaui.components.ui.sheet.SheetHeader
import zed.rainxch.rikkaui.components.ui.sheet.SheetContent
import zed.rainxch.rikkaui.components.ui.sheet.SheetSide
import zed.rainxch.rikkaui.components.ui.sheet.SheetAnimation
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialog
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogHeader
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogFooter
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogCancel
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogAction
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogActionVariant
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogAnimation
import zed.rainxch.rikkaui.components.ui.navigation.NavigationBar
import zed.rainxch.rikkaui.components.ui.navigation.NavigationBarItem
import zed.rainxch.rikkaui.components.ui.navigation.NavigationBarAnimation
import zed.rainxch.rikkaui.components.ui.tabs.Tab
import zed.rainxch.rikkaui.components.ui.tabs.TabList
import zed.rainxch.rikkaui.components.ui.tabs.TabContent
import zed.rainxch.rikkaui.components.ui.tabs.TabAnimation
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.toggle.Toggle

// Icons
import zed.rainxch.rikkaicons.pack.lucide.LucidePack
import zed.rainxch.rikkaicons.core.DecorativeAppIcon
```

---

## Full screen example — MVVM pattern

```kotlin
@Composable
fun UserEditorScreen(
    state: UserEditorState,
    onAction: (UserEditorAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Profile") },
                navigationIcon = {
                    IconButton(
                        icon = RikkaIcons.ArrowLeft,
                        onClick = { onAction(UserEditorAction.NavigateBack) },
                        contentDescription = "Back"
                    )
                },
                actions = {
                    IconButton(
                        icon = RikkaIcons.Check,
                        onClick = { onAction(UserEditorAction.Save) },
                        contentDescription = "Save",
                        enabled = !state.isLoading
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(RikkaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.md)
        ) {
            if (state.isLoading) Spinner()

            Input(
                value = state.name,
                onValueChange = { onAction(UserEditorAction.NameChanged(it)) },
                label = "Name",
                placeholder = "Full name"
            )

            Button(
                text = "Delete Account",
                onClick = { onAction(UserEditorAction.ShowDeleteDialog) },
                variant = ButtonVariant.Destructive,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    AlertDialog(
        open = state.showDeleteDialog,
        onDismiss = { onAction(UserEditorAction.DismissDialog) },
        animation = AlertDialogAnimation.FadeScale
    ) {
        AlertDialogHeader { Text(text = "Delete account?") }
        AlertDialogFooter {
            AlertDialogCancel(onClick = { onAction(UserEditorAction.DismissDialog) })
            AlertDialogAction(
                onClick = { onAction(UserEditorAction.ConfirmDelete) },
                variant = AlertDialogActionVariant.Destructive
            )
        }
    }
}
```

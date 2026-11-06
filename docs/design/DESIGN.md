---
name: ごほうびスタンプ
colors:
  background: '#FFF8EF'
  on-background: '#5A3925'
  surface: '#FFF8EF'
  on-surface: '#5A3925'
  surface-container: '#FFFCF8'
  surface-container-high: '#FFFCF8'
  surface-container-highest: '#FFFCF8'
  surface-container-low: '#FFF8EF'
  surface-container-lowest: '#FFF8EF'
  surface-variant: '#FFE9E5'
  on-surface-variant: '#5A3925'
  primary: '#F85A5A'
  on-primary: '#FFFFFF'
  primary-container: '#FFE9E5'
  on-primary-container: '#F85A5A'
  secondary: '#F85A5A'
  on-secondary: '#FFFFFF'
  secondary-container: '#FFE9E5'
  on-secondary-container: '#5A3925'
  tertiary: '#F85A5A'
  on-tertiary: '#FFFFFF'
  tertiary-container: '#FFE9E5'
  on-tertiary-container: '#5A3925'
  outline: '#F6CFC5'
  outline-variant: '#F2D8CF'
  surface-tint: '#F85A5A'
  stamp-ink: '#C53A32'
  goal-outer: '#F6CFC5'
  goal-inner: '#E8B8AC'
  section: '#A94A40'
typography:
  display:
    fontFamily: Noto Sans JP
    fontSize: 45px
    fontWeight: '400'
    lineHeight: 52px
  title-app-bar:
    fontFamily: Noto Sans JP
    fontSize: 22px
    fontWeight: '400'
    lineHeight: 28px
  title-section:
    fontFamily: Zen Maru Gothic
    fontSize: 16px
    fontWeight: '900'
    lineHeight: 24px
  body-large:
    fontFamily: Noto Sans JP
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-medium:
    fontFamily: Noto Sans JP
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-nav:
    fontFamily: Zen Maru Gothic
    fontSize: 12px
    fontWeight: '900'
    lineHeight: 16px
  label-badge:
    fontFamily: Noto Sans JP
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
rounded:
  card: 12px
  dialog: 28px
  stamp-button: 32px
  full: 9999px
spacing:
  xs: 4px
  sm: 8px
  md: 12px
  lg: 16px
  xl: 20px
---

## Brand & Style

ごほうびスタンプ is an Android app that parents and young children use together. A child presses a cute stamp onto a sheet each time they finish a task (brushing teeth, tidying up), and a full sheet can be exchanged for a reward that the parent set up.

- **Atmosphere:** warm, soft and encouraging, like a paper stamp card. Never cold or corporate.
- **Audience:** young children look at the sheets and press stamps; parents create sheets, exchange rewards and change settings. Child-facing copy is mostly hiragana (「なにを がんばる？」); parent-facing copy uses normal Japanese.
- **Platform:** Android, Jetpack Compose, Material 3, light theme only. Phone portrait.

## Colors

All values come from `app/src/main/java/com/cheesecomer/rewardstamp/ui/theme/Color.kt`. The palette has a single accent; secondary and tertiary roles reuse it.

| Role | Value | Use |
| --- | --- | --- |
| Background | `#FFF8EF` | Screen background, bottom navigation |
| Card | `#FFFCF8` | Cards and grouped sections |
| Primary | `#F85A5A` | Buttons, switches, active progress, icons, section title icons |
| Primary container | `#FFE9E5` | Selected tab bubble, soft highlighted areas, ± buttons |
| Text | `#5A3925` | All text. Never pure black |
| Section | `#A94A40` | Small labels inside cards, such as 「つぎのごほうび」 on the rewards screen (halfway between text and primary) |
| Border | `#F6CFC5` | 1dp card borders, text field borders, stamp cell rings |
| Divider | `#F2D8CF` | Row dividers, the bottom navigation top divider, inactive board path |
| Stamp ink | `#C53A32` | Pressed stamps on the board and the stamp date |
| Goal outer / inner | `#F6CFC5` / `#E8B8AC` | Double ring around the goal cell |
| White | `#FFFFFF` | Stamp cell wells, text field fill, text on primary |

## Typography

The app uses two fonts:

- **にくまる (Nikumaru):** a heavy, rounded Japanese font bundled with the app. Used for section titles (「何回がんばる？」), bottom navigation labels and the stamp button. In web mockups, use **Zen Maru Gothic (900)** as a stand-in.
- **System font (Noto Sans JP on Android):** everything else, including the top app bar title, which is a thin, regular weight.

Large counters such as 「6 / 10 回」 show the current count large in primary color and the goal smaller in text color, aligned on the baseline.

## Layout & Spacing

- Screens are a top app bar with a centered title, scrollable content and a bottom navigation bar.
- Content uses 16dp outer padding. Cards are stacked with 12–16dp gaps.
- Rows inside cards are separated by 1dp `#F2D8CF` dividers.

## Elevation & Shapes

- Depth comes from tonal layering (background → card → white wells) and 1dp borders, not shadows. Cards have no elevation.
- The only shadowed element is the stamp button (6dp elevation).
- Cards use 12dp corners; dialogs use the Material 3 default (28dp). Buttons, badges and the selected tab bubble are fully rounded. Stamp cells are circles.

## Components

### Bottom navigation
Four tabs: シート / ごほうび / れきし / せってい. Outlined icons with Nikumaru labels on the background color, with a 1dp divider above. The selected tab has a circular `#FFE9E5` bubble behind the icon and label.

### Section title
A 28dp primary-colored cherry blossom icon (`ic_cherry_blossom`) followed by the title in Nikumaru, used at the top of cards and form sections.

### Sheet card
A card with a 1dp border. The left column has a 「ゴール N」 badge (filled primary, white bold 12sp text) above an 80dp ring (3dp primary stroke at 75% opacity) showing the last pressed stamp. The right column has the sheet name, the counter and rows of cherry blossom icons: earned ones in primary, the rest at 35% opacity.

### Stamp board
- Cells are white circles with a 2dp border, connected by a path with round caps: primary up to the latest stamp, `#F2D8CF` after it.
- Pressed stamps are drawn in stamp ink at 90% opacity, rotated randomly between -10° and +10°, with faint white specks and the date (`M/d`) under the stamp.
- The goal cell has a double ring (8dp `#F6CFC5` outside, 4dp `#E8B8AC` inside) and shows a gray crown until it is stamped.

### Buttons
- **Stamp button:** filled primary with 32dp corners, white Nikumaru text, a right chevron and a 6dp shadow.
- **Dialog buttons:** two buttons of equal width. Cancel is a text button in primary; confirm is a filled primary pill.

### Text fields
Outlined, white fill, 1dp `#F6CFC5` border in both focused and unfocused states, primary cursor.

## Screen designs

Stitch exports for individual screens live in `screens/<name>/` (`screen.png` and `code.html`). They are references: when they disagree with this document or the existing components, follow this document and the components.

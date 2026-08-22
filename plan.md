# FarmAssistReboot — Code Review & Test Plan

> Generated from a full code + logic review of the project. Items are grouped by priority. Each item is referenced by its code (C1, H4, M9…) and includes a status (✅ done, 🟡 in progress, ⏳ pending).

---

## 1. Project Snapshot

- **Type:** Spigot / Paper / Folia Minecraft plugin
- **Language / target:** Java 8
- **Build:** Gradle (Kotlin DSL) with `plugin-yml-bukkit`, `shadow`, JUnit 5
- **Purpose:** Auto-replant crops on break; per-crop / per-world / per-player toggles, PlaceholderAPI, update checker, ACF commands.
- **Source files (main):** 14 classes. **Tests:** 1 (the existing `SimpleUpdateCheckerTaskTest`).

---

## 2. Architecture

```
FarmAssistReboot (JavaPlugin)
├── onEnable() → saveDefaultConfig, FarmAssistConfig, LanguageManager,
│                PaperCommandManager + FarmAssistCommand, registerListeners,
│                Util.init, SimpleUpdateCheckerTask, bStats, PAPI
├── Crop (enum) — WHEAT..CACTUS, PITCHER_PLANT, TORCHFLOWER
├── Util (static) — seed-slot lookup, replant scheduler, messaging
├── FarmAssistPlaceholderExpansion — PAPI placeholders
├── FarmAssistCommand (ACF) — toggle | global | info | update | reload | lang
├── FarmAssistConfig (BoostedYAML) — per-crop + per-world config
├── LanguageFile / LanguageManager (Bukkit YAML) — bundled translations
├── listeners/
│   ├── BlockBreakListener — core auto-replant
│   ├── PlayerInteractionListener — auto-till + plant wheat
│   └── JoinListener — update notification
├── tasks/
│   ├── ReplantTask — delayed setBlock / setCocoaOrDropSeed
│   └── SimpleUpdateCheckerTask — GitHub releases JSON check
└── messages/ — Commands, Debug, InternalMessages, Permissions (string constants)
```

---

## 3. Findings (full review)

### 🔴 Critical bugs

### 🟠 High

| ID | Description | Status |
|---|---|---|
| **H1** | `Util` is static with global plugin state — makes unit testing hard. | ⏳ pending — decision needed: static reset helper vs Mockito + MockBukkit vs full refactor |
| **H2** | `FarmAssistConfig` and `LanguageManager` both do redundant I/O on the same files. | ⏳ pending — document & improve |
| **H3** | `Util#sendMessage` can NPE if `getActiveLanguage()` is null. | ⏳ pending — add guard |
| **H4** | `disabledPlayerList` is `ArrayList<UUID>` — not thread-safe, but plugin advertises Folia support. | ⏳ pending — switch to `ConcurrentHashMap.newKeySet()` |
| **H5** | `BlockBreakListener#dropItemsNaturally` is `public` but should be `private`. | ⏳ pending |
| **H6** | `FarmAssistConfig#noSeeds()` uses `Route.from("no-seeds", false)` — the second `false` is `caseSensitive`, not a default value, so `no-seeds: true` is ignored. **Real bug**, was mis-tagged as maintainability. | ⏳ pending |
| **H7** | `SimpleUpdateCheckerTask` catches `Exception` too broadly. | ⏳ pending — split into `IOException` / `ParseException` |
| **H8** | `FarmAssistReboot` doesn't initialize `needsUpdate` / `newVersion` defensively. | ⏳ pending |
| **H9** | `BlockBreakListener#dropItemsNaturally` should be private. | ⏳ pending (same as H5) |
| **H10** | `LanguageFile#initValues` doesn't guard against missing keys → null. | ⏳ pending |
| **H11** | (Mirrors C3.) | ⏳ pending |
| **H12** | `build.gradle.kts` permissions block is missing `farmassist.info`, `farmassist.toggle`, `farmassist.toggle.global`, `farmassist.till`, `farmassist.noseeds`, `farmassist.nodrops`. | ⏳ pending |

### 🟡 Medium

| ID | Description | Status |
|---|---|---|
| **M1** | `debug()` overloads shadow each other across classes. | ⏳ pending |
| **M2** | `Crop.TORCHFLOWER` uses `TORCHFLOWER_SEEDS` as seed — verify. | ⏳ pending |
| **M3** | `Crop.PITCHER_PLANT` uses `PITCHER_CROP` / `PITCHER_POD` — verify in XSeries 13.7.1. | ⏳ pending |
| **M4** | `*.java~` backup files in repo. | **✅ done** (`.gitignore` updated; files will be deleted) |
| **M5** | CI runs only on `master`. | ⏳ pending — extend to `pull_request` |
| **M6** | `fr` language file is untranslated. | ⏳ pending |
| **M7** | `pt_BR` and `zh-Hans` ARE translated. | (no action) |
| **M8** | `internal-messages/` is a build artifact. | (no action) |
| **M9** | PAPI `getVersion() = "1.0.0"` is intentional (expansion version ≠ plugin version). | (no action) |
| **M10** | "Switched language to X" message uses new language. | ⏳ pending — keep, add comment |
| **M11** | `getDisabledPlayerList` exposes mutable list. | ⏳ pending — wrap with `unmodifiableSet` |
| **M12** | `setActiveLanguage` write order in `onLanguages` is correct on inspection; add regression test. | ⏳ pending |
| **M13** | `Util.debug("Spot:" + spot)` is inconsistent. | ⏳ pending |

### 🟢 Low

| ID | Description | Status |
|---|---|---|

---

## 4. Test Coverage Map

### Tier 1 — Pure unit, no Bukkit needed (easiest)

| # | Target | Class |
|---|---|---|
| 1.1 | `Crop.getCropList()` | `CropTest` |
| 1.2 | `Crop.getPlantedOn/getPlanted/getSeed` | `CropTest` |
| 1.3 | `SimpleUpdateCheckerTask.needsUpdate` (extend existing) | `SimpleUpdateCheckerTaskTest` |
| 1.4 | `Permissions` constants | `PermissionsTest` |
| 1.5 | `Commands` constants | `CommandsTest` |
| 1.6 | `InternalMessages` constants | `InternalMessagesTest` |
| 1.7 | `Debug` constants | `DebugTest` |
| 1.8 | `Util.color` | `UtilTest` (if H1 is option 1) |

### Tier 2 — Needs `@TempDir` + a `JavaPlugin` mock

| # | Target | Class |
|---|---|---|
| 2.1 | `ConfigFile` round-trip | `ConfigFileTest` |

### Tier 3 — Needs `Mockito` + `MockBukkit`

| # | Target | Class |
|---|---|---|
| 3.1 | `Util.inventoryContainsSeeds` | `UtilTest` |
| 3.2 | `Util.isWorldDisabled` | `UtilTest` |
| 3.3 | `Util.checkNoSeeds` / `checkNoDrops` | `UtilTest` |
| 3.4 | `Util.checkSeedsOrNoSeedsInInventory` (both overloads) | `UtilTest` |
| 3.5 | `Util.removeOrSubtractItem` | `UtilTest` |
| 3.6 | `Util.replant(player, block, slot)` | `UtilTest` |
| 3.7 | `FarmAssistPlaceholderExpansion.onRequest` (all 4 placeholders) | `FarmAssistPlaceholderExpansionTest` |
| 3.8 | `FarmAssistPlaceholderExpansion.isUuid` heuristic | `FarmAssistPlaceholderExpansionTest` |
| 3.9 | `BlockBreakListener.onBlockBreak` gate logic | `BlockBreakListenerTest` |
| 3.10 | `BlockBreakListener.applyReplant` success path | `BlockBreakListenerTest` |
| 3.11 | `BlockBreakListener.isRipe` cast safety | `BlockBreakListenerTest` |
| 3.12 | **`PlayerInteractionListener.doesNotHaveWheatAndTillPermissions`** | **`PlayerInteractionListenerTest`** |
| 3.13 | `PlayerInteractionListener.isHoe / isGrassOrDirt / isTopBlockAir` | `PlayerInteractionListenerTest` |
| 3.14 | `JoinListener.onPlayerJoin` (C3 fix) | `JoinListenerTest` |
| 3.15 | `FarmAssistReboot` state getters | `FarmAssistRebootTest` |
| 3.16 | `FarmAssistReboot` update state | `FarmAssistRebootTest` |
| 3.17 | `ReplantTask.run` per material (C6 fix) | `ReplantTaskTest` |
| 3.18 | `ReplantTask.matchedRelativeType / isOnAnyOf` (C7 fix) | `ReplantTaskTest` |

### Tier 4 — Integration / real server

| # | What | Why |
|---|---|---|
| 4.1 | Full plugin on Paper / Folia | end-to-end |
| 4.2 | `SimpleUpdateCheckerTask.run` with a stubbed `URLConnection` | error paths |
| 4.3 | `FarmAssistCommand` through ACF | command routing |

| 2.2 | `LanguageFile` initValues + missing keys | `LanguageFileTest` |
| 2.3 | `LanguageManager.getFolderNamesFromJar` | `LanguageManagerTest` |
| 2.4 | `FarmAssistConfig` boolean reads (incl. H6 fix) | `FarmAssistConfigTest` |
| 2.5 | `FarmAssistConfig.setActiveLanguage` round-trip | `FarmAssistConfigTest` |

---

## 5. Execution Order (incremental — per your request)

The user requested a strict, gated, one-step-at-a-time flow:

1. ✅ Write this `plan.md`
2. 🟡 **C1** — Tests for `PlayerInteractionListener#doesNotHaveWheatAndTillPermissions` ← **current step**
3. ⏳ C2 — `LanguageManager` ordering + `LanguageFile` / `LanguageManager` tests
4. ⏳ C3 — `JoinListener` / `FarmAssistCommand.onUpdate` rewrite + tests
5. ⏳ C5 — `Util#replant(material)` overload — use `inventoryContainsSeeds`
6. ⏳ C6 — Remove dead `case FARMLAND` from `ReplantTask`
7. ⏳ C7 — Refactor `isBottomBlock` → `isOnAnyOf`
8. ⏳ C8 / C9 — `instanceof` guard for `Ageable`; null guard for `cocoa`
9. ⏳ H1 + H4 + H11 — `ConcurrentHashMap.newKeySet()` + `Util.reset()` + unmodifiable
10. ⏳ H3 — `LanguageManager.getActiveLanguageOrFallback()`
11. ⏳ H6 — Fix `noSeeds()` route + bug-reproducing test
12. ⏳ H7 + H8 — Split exceptions; defensive `onEnable` defaults
13. ⏳ H9 — `dropItemsNaturally` → `private`
14. ⏳ H12 — Register missing permissions in `build.gradle.kts`
15. ⏳ M5 — CI on PR
16. ⏳ M13 — `Util.debug` style fix
17. ⏳ L1, L4, L5, L6 — formatting / naming / unused imports cleanup

Each step **must** be reviewed and approved before moving on.

---

## 6. Open Decisions

- **H1 (Util test infrastructure):** three options on the table:
  1. `Util.reset()` + `@BeforeEach`/`@AfterEach` (no new deps).
  2. Add `Mockito` + `MockBukkit` to test deps.
  3. Full refactor of `Util` to an instance class.
  *Recommendation:* Option 2. **Pending user decision before H1 is implemented.**

---

## 7. Notes & Acknowledgments

- **M4** is partially done (gitignore added). The `*.java~` files in the working tree will be removed in the C1 step.
- **C4, C5 baseline behavior** for sugar-cane / cactus is correct in design; only the seed-slot filter logic needs unifying.
- **M9** confirmed: the PAPI expansion version is intentionally separate from the plugin version.
- **M8** confirmed: `internal-messages/` is a build artifact (auto-generated by a prior code-gen step).

| 2.6 | `LanguageManager.switchLanguages`, `isSupported` | `LanguageManagerTest` |

| **L1** | Mixed tabs/spaces in `BlockBreakListener`. | ⏳ pending — reformat |
| **L2** | `Crop` is in the root package. | (no action — keep) |
| **L3** | No Lombok (intentional). | (no action) |
| **L4** | `checkSeedsOrNoSeedsInInventory` is poorly named. | ⏳ pending — rename to `isMissingRequiredSeeds` |
| **L5** | `inventoryContainsSeeds` returns `-1` sentinel. | ⏳ pending — wrap in `OptionalInt` |
| **L6** | Unused imports. | ⏳ pending — cleanup pass |


| ID | Description | Status |
|---|---|---|
| **C1** | `PlayerInteractionListener#doesNotHaveWheatAndTillPermissions` — `&&` / `\|\|` precedence bug. Original code returned `(!usePermissions && !wheat) \|\| !till`, meaning a missing `farmassist.till` perm blocked auto-till even when `use-permissions: false`. | **🟡 in progress — code fix applied by user; tests being added now** |
| **C2** | `LanguageManager` constructor order: `saveLanguagesDirectories` runs *before* `switchLanguages`, so the first `Util.sendMessage` after construction can NPE if a translation key is missing. | ⏳ pending |
| **C3** | `JoinListener` broken fallthrough: "running latest" branch returns correctly, but the else-branch always sends "new update available" — every OP player on join is told there's a new version regardless. Same broken symmetry in `FarmAssistCommand.onUpdate` (the command version works, the listener doesn't). | ⏳ pending |
| **C5** | `Util#replant(player, block, Material)` for sugar-cane/cactus uses `player.getInventory().first(seed)` instead of `inventoryContainsSeeds(...)`, so the `ignoreRenamed` / `ignoreNbt` filters don't apply to those crops. Also, `CACTUS` self-referential seed decrements the player's cactus stack. | ⏳ pending |
| **C6** | `ReplantTask#run` has a `case FARMLAND:` branch that is dead code (`BlockBreakEvent` never fires for `FARMLAND`). | ⏳ pending |
| **C7** | `ReplantTask#isBottomBlock` has confusing overloads (array vs single Material). | ⏳ pending |
| **C8** | `BlockBreakListener#isRipe` does an unguarded `(Ageable) block.getBlockData()` cast. | ⏳ pending |
| **C9** | `ReplantTask#setCocoaOrDropSeed` can NPE if `cocoa` is null (e.g. when the constructor is bypassed). | ⏳ pending |

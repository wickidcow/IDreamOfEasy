<div align="center">

# 🌙 I Dream of Easy — Slimefun Legacy

### Community-inspired Slimefun utilities, machines, tools, transport, and quality-of-life ideas — preserved for modern servers.

[![Build IDreamOfEasy 26.2 / Slimefun Legacy](https://github.com/wickidcow/IDreamOfEasy/actions/workflows/maven.yml/badge.svg)](https://github.com/wickidcow/IDreamOfEasy/actions/workflows/maven.yml)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](LICENSE)
![Paper](https://img.shields.io/badge/Paper-26.2-2ea44f)
![Purpur](https://img.shields.io/badge/Purpur-26.2-7b4bb7)
![Folia](https://img.shields.io/badge/Folia-secondary-yellow)
![Java](https://img.shields.io/badge/Java-21%2B-orange)
![Slimefun](https://img.shields.io/badge/Slimefun-Legacy-purple)

**Maintained compatibility fork for Slimefun Legacy and modern Paper-family servers.**

**Maintained for [AlbionMC.com](https://albionmc.com) and shared publicly so the wider Slimefun community can benefit from the preservation work.**

</div>

---

## ✨ What is I Dream of Easy?

**I Dream of Easy (IDOE)** is a Slimefun addon originally created by **[Bunnky](https://github.com/Bunnky)** to turn practical community suggestions into real gameplay features. The addon grew around quality-of-life tools, machines, utility items, mob-control devices, transport ideas, and other things players wished existed in Slimefun.

This repository continues that work as a **compatibility and preservation fork**. The goal is not to replace or erase the original project, but to keep its functionality usable on current servers while preserving the identity, design intent, item IDs, recipes, community credits, and project history wherever practical.

> [!IMPORTANT]
> This fork targets **Paper 26.2 / Minecraft 1.21.11+** with **Slimefun Legacy** as its primary Slimefun runtime.
>
> **GuizhanLibPlugin is not required.** The Gugu-specific updater, version helper, hard dependency, and localized runtime requirement have been removed.

---

## 🧭 Slimefun Legacy fork goals

- Preserve the original IDreamOfEasy gameplay and Slimefun item IDs.
- Maintain compatibility with **Paper 26.2** and current **Slimefun Legacy** APIs.
- Support **Purpur** as a Paper-compatible runtime target.
- Keep **Folia** as a secondary target by using Paper/Folia region, global-region, and entity schedulers where addon code schedules work.
- Keep the addon **English-first** for the Slimefun Legacy ecosystem.
- Remove unnecessary hard dependencies, including **GuizhanLibPlugin**.
- Carry forward useful fixes from maintained forks when they improve correctness without changing the addon's identity.
- Keep builds reproducible through GitHub Actions.
- Publish a **direct, uncompressed server JAR** instead of requiring users to extract a `.zip` artifact.
- Preserve attribution to the original author, community contributors, and later maintainers.

---

## 📦 Download / build output

Open the **Releases** page for the normal server download, or open **Actions** and select:

**Build IDreamOfEasy 26.2 / Slimefun Legacy**

The current release line produces the direct server file:

```text
SF_IDreamOfEasy_Legacy_v1.0.2.jar
```

The workflow validates the compiled plugin, uploads the JAR directly as the Actions artifact, and attaches the same raw JAR to the matching GitHub Release.

### Requirements

| Component | Target |
|---|---|
| Minecraft | 1.21.11+ / Paper 26.2 line |
| Paper | **Primary target** |
| Purpur | Paper-compatible target |
| Folia | **Secondary / experimental target** |
| Slimefun | **Slimefun Legacy** |
| Java bytecode | Java 21 |
| CI runtime | Java 25 |
| GuizhanLibPlugin | **Not required** |

> [!NOTE]
> Folia-sensitive scheduling inside this addon has been migrated to Paper's region/entity scheduler APIs and the plugin declares `folia-supported: true`. Paper remains the primary validation target, so Folia should still be treated as secondary rather than as a promise that every upstream Slimefun behavior is region-thread safe.

---

## 🛠️ Installation

1. Run a compatible **Paper 26.2** server, or a compatible Paper-family fork such as Purpur.
2. Install **Slimefun Legacy**.
3. Download `SF_IDreamOfEasy_Legacy_v1.0.2.jar` from Releases or the successful Actions build.
4. Place the JAR in your server's `plugins/` directory.
5. Start the server normally.

No GuizhanLibPlugin installation is needed for this fork.

---

## 🚀 What the addon adds

IDreamOfEasy is a collection of practical Slimefun additions rather than one single progression branch. Highlights include:

- ⛏️ **Terrabores** — increasingly powerful area-mining multiblocks.
- 📦 **Player Hopper / Supply Hopper** — move items between players and hoppers.
- 🧲 **Magnetoid** — powered nearby-item attraction from the offhand.
- ☢️ **Radiation Absorbers** — powered local radiation-protection machines.
- 🐑 **Electric Shearer** — automated powered sheep shearing.
- ⚡ **Electric Cable** — powered damaging cable blocks.
- 🚤 **Lava Boat** — special transport for traversing lava.
- 🧭 **Biome Compass** — locate selected biomes.
- ⏰ **Alarm Clock** — configurable player timers and repeating alarms.
- 🧰 **Chisel, Jawn, Wister Shears, Trim Vault, Slime Meal, Idols**, and other utility items.
- 🔥 **Electric Smoker / Electric Blast Furnace** and other convenience machines.
- 🚫 **Mob Repellers** for several hostile mob types.

The original community-driven variety is intentionally preserved rather than narrowing the addon into a single machine pack.

---

## 🔧 Compatibility work in this fork

### Paper 26.2 / Slimefun Legacy

The Maven build compiles against the Paper 26.2 API and validates against the latest released Slimefun Legacy JAR. CI also checks the finished JAR's metadata, Folia declaration, Slimefun dependency, bStats relocation, Guizhan absence, and direct-JAR packaging.

### GuizhanLib removal

The Gugu-derived version required GuizhanLibPlugin at startup and used Guizhan-specific classes for version checks and self-updating. This fork removes those hooks and relies on GitHub Actions / Releases for updates instead.

### English runtime restoration

The Gugu branch carried Chinese item names and lore in the runtime item registry. This fork restores the English item names and lore from the English maintenance lineage so the guide matches the rest of the Slimefun Legacy ecosystem.

### Modern Slimefun APIs

The maintenance pass replaces legacy `BlockStorage`, old block-ticker `Config`, deprecated integer energy helpers, legacy recipe-input access, old research-cost access, Bukkit metadata markers, and other compatibility shims with their current Slimefun Legacy / Paper counterparts.

### Paper / Folia scheduler hardening

Addon-owned delayed and repeating work has been moved away from Bukkit's global scheduler where practical:

- Alarm Clock uses the player entity scheduler.
- Magnetoid uses the global-region scheduler for discovery and entity schedulers for player/item work.
- Lava Boat delayed entity work follows the entity scheduler.
- Stack Dispenser uses the owning block region scheduler.
- Radiation Absorber uses region-owned protection sessions and player entity scheduling for UI updates.

CI rejects known legacy scheduler patterns so future upstream merges cannot silently reintroduce them.

### Correctness fixes

The modernized line also fixes several bugs found during the API migration:

- Player Hopper partial transfers can no longer duplicate items.
- Supply Hopper accounts for partial transfers correctly and only charges power when items move.
- Electric Shearer now actually consumes its configured power when it shears sheep.
- Electric Cable no longer effectively requires a second energy payment before damaging targets.
- Stack Dispenser no longer risks consuming a remaining stack without actually dispensing it.
- Magnetoid no longer treats an offline player as a valid running task merely because the player was not a spectator.
- Radiation Absorber uses independent per-player protection sessions instead of shared countdown/task state.
- Lava Boat combustion protection only applies to actual Lava Boats, their dropped item, and their riders.

---

## ❤️ A tribute to the original project

This fork exists because **IDreamOfEasy was worth preserving**.

The original addon was created by **[Bunnky](https://github.com/Bunnky)** around a simple but excellent idea: listen to the Slimefun community and turn useful suggestions into working content. A large part of IDOE's character comes from those community ideas, so this fork intentionally keeps that history visible rather than presenting the project as something newly invented.

If you appreciate the project, please also visit **Bunnky's GitHub profile** and the historical upstream/community work that made this maintenance fork possible.

Additional thanks go to the **SlimefunGuguProject** maintainers for keeping IDreamOfEasy building and usable through later Minecraft/Slimefun changes, even though this Legacy fork removes the Gugu-specific runtime dependency and restores English runtime text.

Thanks also to maintainers of other IDreamOfEasy forks whose fixes help keep the addon healthy. The Lava Boat combustion correction, for example, was adapted from later **UltrapixelBulgaria/IDreamOfEasy** maintenance work. Where fixes are incorporated, their original authorship and provenance should remain visible in Git history whenever practical.

---

<details>
<summary><strong>💡 Original community suggestions and credits</strong></summary>

These credits were part of the project's history and are intentionally preserved here.

- **inaxtrawetrust** — "A machine that stops Radiation damage in an area" — *Radiation Absorber*
- **林alguém林 有人** — biome-finding compass suggestion — *Biome Compass*
- **Marvi444[Ger]** — player-directed hopper suggestion — *Player Hopper / Supply Hopper*
- **JustAHuman** — slime/mob spawn prevention totem idea — *Mob Repellers*
- **AverageUnusualUser** — always-on infused magnet concept — *Magnetoid*
- **deserdoo** — craftable armor-trim / upgrade support — *Trim Vault*
- **oah** — multi-talisman storage idea — *Idols*
- **elitemastereric** — full-area mining Industrial Miner variant — *Terrabore*
- **hellex7769** — automated wood stripping — *Electric Log Stripper*
- **energized36** — electric Smoker and Blast Furnace — *Electric Smoker / Electric Blast Furnace*
- **nyctophilio01** — obsidian/lava-travel boat concept — *Lava Boat*
- **cromecloridethecheese** — powered damaging cable idea — *Electric Cable*
- **TheBusyBiscuit** — Electric Shearer, Electric Explosive tools, Electric Poison Extractor, and other suggestions
- **kohlth** — portable block-form cycling tool — *Chisel*
- **bird** — Bad Omen potion concept
- **BurningBrimstone** — creative/infinite-style energy testing devices
- **Monster_Engineer** — slime growth meal concept — *Slime Meal*

The wording above is summarized from the original project's preserved suggestion acknowledgements; credit remains with the respective community members.

</details>

---

## ⚖️ License, attribution, and project status

IDreamOfEasy is distributed under the **GNU General Public License v3.0 (GPL-3.0)**. The complete license text is included in [`LICENSE`](LICENSE).

This repository is a **derivative maintenance fork**. Original authors and contributors retain rights to their respective contributions. Maintenance changes in this repository are distributed under the same GPL-3.0 terms. No claim is made that the Slimefun Legacy maintainer authored upstream work that predates this fork, and no upstream author is represented as endorsing this fork unless they explicitly do so.

**Original project:** IDreamOfEasy by [Bunnky](https://github.com/Bunnky)  
**Later maintenance lineage:** SlimefunGuguProject and other community forks  
**Slimefun Legacy maintenance:** wickidcow  
**Primary server use:** [AlbionMC.com](https://albionmc.com)

**Trademark / affiliation notice:** This project is unofficial and is not affiliated with, sponsored by, approved by, or endorsed by Mojang Studios, Microsoft, PaperMC, Purpur, the original Slimefun project, or their respective owners/contributors. Minecraft, Mojang, Microsoft, Paper, Purpur, Slimefun, and other names or marks belong to their respective owners where applicable.

The GPL license governs copying, modification, and redistribution of the covered source code. This README provides attribution and project-status context; it is not a substitute for the license text and is not legal advice.

---

## 🐛 Issues

For bugs specifically affecting this Slimefun Legacy maintenance fork, open an issue in this repository and include:

- Paper / Purpur / Folia version
- Java version
- Slimefun Legacy version
- IDreamOfEasy Legacy version
- Relevant startup/error log
- Steps to reproduce

That makes it much easier to distinguish an IDOE compatibility issue from a Slimefun or server-runtime problem.

---

<div align="center">

### Preserve the idea. Modernize the runtime. Keep the credit. 🌙

</div>

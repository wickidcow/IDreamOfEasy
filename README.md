<div align="center">

# 🌙 I Dream of Easy — Legacy

### Community-inspired Slimefun utilities, machines, tools, and quality-of-life ideas — preserved for modern servers.

[![Build IDreamOfEasy 26.2 / Slimefun Legacy](https://github.com/wickidcow/IDreamOfEasy/actions/workflows/maven.yml/badge.svg)](https://github.com/wickidcow/IDreamOfEasy/actions/workflows/maven.yml)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](LICENSE)
![Paper](https://img.shields.io/badge/Paper-26.2-2ea44f)
![Java](https://img.shields.io/badge/Java-21%2B-orange)
![Slimefun](https://img.shields.io/badge/Slimefun-Legacy-purple)

**Maintained compatibility fork for Slimefun Legacy and modern Paper servers.**

</div>

---

## ✨ What is I Dream of Easy?

**I Dream of Easy (IDOE)** is a Slimefun addon originally created by **Bunnky** to turn practical community suggestions into real gameplay features. The addon grew around quality-of-life tools, machines, utility items, mob-control devices, transport ideas, and other things players wished existed in Slimefun.

This repository continues that work as a **compatibility and preservation fork**. The goal is not to replace or erase the original project, but to keep its functionality usable on current servers while preserving the identity, design intent, item IDs, recipes, and community history wherever practical.

> [!IMPORTANT]
> This fork targets **Paper 26.2** with **Slimefun Legacy** as its primary Slimefun runtime.
>
> **GuizhanLibPlugin is not required.** The Gugu-specific updater, version helper, hard dependency, and localized runtime requirement have been removed.

---

## 🧭 Legacy fork goals

- Preserve the original IDreamOfEasy gameplay and Slimefun item IDs.
- Maintain compatibility with **Paper 26.2** and current Slimefun Legacy APIs.
- Keep the addon **English-first** for the Slimefun Legacy ecosystem.
- Remove unnecessary hard dependencies, including **GuizhanLibPlugin**.
- Carry forward useful fixes from maintained forks when they improve correctness without changing the addon's identity.
- Keep builds reproducible through GitHub Actions.
- Publish a **direct, uncompressed server JAR** instead of requiring users to extract a `.zip` artifact.
- Preserve attribution to the original author, community contributors, and later maintainers.

---

## 📦 Download / build output

Open the **Actions** tab and run or select:

**Build IDreamOfEasy 26.2 / Slimefun Legacy**

Successful builds produce the direct server file:

```text
SF_IDreamOfEasy_Legacy_v1.0.0.jar
```

The workflow also publishes the same direct JAR as a GitHub Release asset when the compatibility branch is merged to `master`.

### Requirements

| Component | Target |
|---|---|
| Server | Paper 26.2 |
| Slimefun | Slimefun Legacy |
| Java runtime | Java 21+; CI validates with Java 25 |
| GuizhanLibPlugin | **Not required** |

---

## 🛠️ Installation

1. Install a compatible **Paper 26.2** server.
2. Install **Slimefun Legacy**.
3. Download `SF_IDreamOfEasy_Legacy_v1.0.0.jar` from Actions or Releases.
4. Place the JAR in your server's `plugins/` directory.
5. Start the server normally.

No GuizhanLibPlugin installation is needed for this fork.

---

## 🔧 Compatibility work in this fork

### Paper 26.2 / Slimefun Legacy

The Maven build now compiles directly against the current Paper 26.2 API and the locally published Slimefun Legacy API used by the rest of the Legacy addon family.

### GuizhanLib removal

The Gugu-derived version required GuizhanLibPlugin at startup and used Guizhan-specific classes for version checks and self-updating. This fork removes those hooks and relies on GitHub Actions / Releases for updates instead.

### English runtime restoration

The Gugu branch carried Chinese item names and lore in the runtime item registry. This fork restores the English item names and lore from the English maintenance lineage so the guide matches the rest of the Slimefun Legacy ecosystem.

### Lava Boat combustion fix

The Lava Boat combustion handler has been corrected so it only protects actual Lava Boat items, Lava Boat entities, and their riders. It no longer cancels unrelated entity combustion events. This fix was adapted from the later **UltrapixelBulgaria/IDreamOfEasy** maintenance work.

---

## ❤️ A tribute to the original project

This fork exists because **IDreamOfEasy was worth preserving**.

The original addon was created by **Bunnky** around a simple but excellent idea: listen to the Slimefun community and turn useful suggestions into working content. A large part of IDOE's character comes from those community ideas, so this fork intentionally keeps that history visible rather than presenting the project as something newly invented.

Additional thanks go to the **SlimefunGuguProject** maintainers for keeping IDreamOfEasy building and usable through later Minecraft/Slimefun changes, even though this Legacy fork removes the Gugu-specific runtime dependency and restores English runtime text.

Thanks also to maintainers of other IDreamOfEasy forks whose fixes help keep the addon healthy. Where fixes are incorporated, their original authorship remains part of the Git history and should be preserved whenever practical.

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

This repository is a **derivative maintenance fork**. Original authors and contributors retain rights to their respective contributions. Maintenance changes in this repository are distributed under the same GPL-3.0 terms. No claim is made that the Legacy maintainer authored the upstream work that predates this fork.

**Original project credit:** Bunnky / IDreamOfEasy  
**Later maintenance lineage:** SlimefunGuguProject and other community forks  
**Slimefun Legacy maintenance:** wickidcow

This project is unofficial and is not endorsed by Mojang Studios, Microsoft, PaperMC, or the original Slimefun project. Names and trademarks belong to their respective owners.

---

## 🐛 Issues

For bugs specifically affecting this Legacy fork, open an issue in this repository and include:

- Paper version
- Java version
- Slimefun Legacy version
- IDreamOfEasy Legacy version
- Relevant startup/error log
- Steps to reproduce

That makes it much easier to distinguish an IDOE compatibility issue from a Slimefun or server-runtime problem.

---

<div align="center">

### Preserve the idea. Modernize the runtime. Keep the credit.

</div>

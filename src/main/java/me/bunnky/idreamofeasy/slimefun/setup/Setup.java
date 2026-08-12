package me.bunnky.idreamofeasy.slimefun.setup;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.GPSTransmitter;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import lombok.experimental.UtilityClass;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.slimefun.items.AlarmClock;
import me.bunnky.idreamofeasy.slimefun.items.BiomeCompass;
import me.bunnky.idreamofeasy.slimefun.items.Chisel;
import me.bunnky.idreamofeasy.slimefun.items.ElectricExplosivePickaxe;
import me.bunnky.idreamofeasy.slimefun.items.ElectricExplosiveShovel;
import me.bunnky.idreamofeasy.slimefun.items.TomeOfEnlightenment;
import me.bunnky.idreamofeasy.slimefun.items.SlimeMeal;
import me.bunnky.idreamofeasy.slimefun.items.idols.TerranIdol;
import me.bunnky.idreamofeasy.slimefun.items.idols.FlameheartIdol;
import me.bunnky.idreamofeasy.slimefun.items.Jawn;
import me.bunnky.idreamofeasy.slimefun.items.LavaBoat;
import me.bunnky.idreamofeasy.slimefun.items.Magnetoid;
import me.bunnky.idreamofeasy.slimefun.items.idols.DivineIdol;
import me.bunnky.idreamofeasy.slimefun.items.idols.TorrentIdol;
import me.bunnky.idreamofeasy.slimefun.machines.ElectricCable;
import me.bunnky.idreamofeasy.slimefun.machines.ElectricPoisonExtractor;
import me.bunnky.idreamofeasy.slimefun.machines.ElectricShearer;
import me.bunnky.idreamofeasy.slimefun.machines.PlayerHopper;
import me.bunnky.idreamofeasy.slimefun.machines.StackDispenser;
import me.bunnky.idreamofeasy.slimefun.machines.SupplyHopper;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.BatRepeller;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.CreeperRepeller;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.EndermanRepeller;
import me.bunnky.idreamofeasy.slimefun.items.TrimVault;
import me.bunnky.idreamofeasy.slimefun.items.WisterShears;
import me.bunnky.idreamofeasy.slimefun.machines.multiblock.AdvancedTerrabore;
import me.bunnky.idreamofeasy.slimefun.machines.ElectricBlastFurnace;
import me.bunnky.idreamofeasy.slimefun.machines.ElectricLogStripper;
import me.bunnky.idreamofeasy.slimefun.machines.ElectricSmoker;
import me.bunnky.idreamofeasy.slimefun.machines.multiblock.EliteTerrabore;
import me.bunnky.idreamofeasy.slimefun.machines.RadiationAbsorber;
import me.bunnky.idreamofeasy.slimefun.machines.multiblock.Terrabore;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.SkeletonRepeller;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.SlimeRepeller;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.SpiderRepeller;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.WitchRepeller;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.ZombieRepeller;
import me.bunnky.idreamofeasy.slimefun.machines.repellers.ZombieVillagerRepeller;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class Setup {
    private static final String TRIM_VAULT = "9d6f99f3c5d982ebde8da657d0652aa08064bf6d6b495fda23c6e47123c093e7";
    private static final String REPELLER = "10892dff2ad75e47b08bf637d8dd2d10ed767f80b8144d5eda5db4d2e18614d8";
    private static final String CREATIVE_TRANSMITTER = "93e2f779147bc025487b27a37b7fc516d7489c589df97016eb8dc0f54d3bf29e";
    private static final String CREATIVE_CAPACITOR = "621197e53c1ab112e68079d3c83fa4714c77a83d0621a29d762e99e9feaae4d5";
    private static final String CREATIVE_GENERATOR = "56a7d2195ff7674bbb12e2f7578a2a63c54a980e64744450ac6656e05a790499";

    public static void setup() {

        IDreamOfEasy plugin = IDreamOfEasy.getInstance();

        ItemStack groupItem = CustomItemStack.create(Material.CALIBRATED_SCULK_SENSOR, "&2I Dream of Easy", "", "I wish this worked!");
        NamespacedKey groupId = new NamespacedKey(IDreamOfEasy.getInstance(), "i_dream_of_easy");
        ItemGroup group = new ItemGroup(groupId, groupItem);

        //########################
        //  ITEMS
        //########################

        //////////////////////////////////////////////
        //////////////// MULTIBLOCK //////////////////
        //////////////////////////////////////////////

        SlimefunItemStack terrabore = new SlimefunItemStack(
            "IDOE_TERRABORE",
            Material.NETHERITE_SHOVEL,
            "&eTerrabore",
            "",
            "&fThis Multiblock will mine",
            "&ceverything &fin a 7x7 area",
            "&funderneath it.",
            "&fPlace coal or similar in its",
            "&fchest to fuel this machine.",
            ""
        );

        SlimefunItemStack terraboreAdvanced = new SlimefunItemStack(
            "IDOE_TERRABORE_ADVANCED",
            Material.NETHERITE_PICKAXE,
            "&eAdvanced Terrabore",
            "",
            "&fThis Multiblock will mine",
            "&ceverything except ores &fin",
            "&fa 11x11 area underneath it.",
            "&fPlace a bucket of fuel or lava in",
            "&fits chest to fuel this machine.",
            "",
            "&a+ Silk Touch"
        );

        SlimefunItemStack terraboreElite = new SlimefunItemStack(
            "IDOE_TERRABORE_ELITE",
            Material.NETHERITE_BLOCK,
            "&eAdvanced Terrabore",
            "",
            "&fThis Multiblock will mine",
            "&ceverything &fin a 21x21 area",
            "&funderneath it.",
            "&fPlace &auranium &fin its chest",
            "&fto fuel this machine.",
            "",
            "&a+ Silk Touch"
        );

        //////////////////////////////////////////////
        ///////////////// MACHINES ///////////////////
        //////////////////////////////////////////////
        SlimefunItemStack supplyHopper = new SlimefunItemStack(
            "IDOE_HOPPER_SUPPLY",
            Material.HOPPER,
            "&aSupply Hopper",
            "",
            "&fStand under this to automatically",
            "&fpush items to your inventory",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(250)
        );

        SlimefunItemStack playerHopper = new SlimefunItemStack(
            "IDOE_HOPPER_PLAYER",
            Material.HOPPER,
            "&aPlayer Hopper",
            "",
            "&fStand on this to automatically",
            "&fpull items out of your inventory",
            "",
            "&8⇨ &4Ignores equipped items!",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(250)
        );

        SlimefunItemStack stackDispenser = new SlimefunItemStack(
            "IDOE_DISPENSER_STACK",
            Material.DISPENSER,
            "&aStack Dispenser",
            "",
            "&fTries to dispense whole",
            "&fstacks",
            ""
        );

        SlimefunItemStack electricCable = new SlimefunItemStack(
            "IDOE_ELECTRIC_CABLE",
            Material.CHAIN,
            "&aElectric Cable",
            "",
            "&fDamages anything that touches it",
            "",
            "&8⇨ &aIgnores owner!",
            "",
            LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(100),
            "",
            "&eDamage: &72"
        );

        SlimefunItemStack electricCable2 = new SlimefunItemStack(
            "IDOE_ELECTRIC_CABLE_2",
            Material.YELLOW_STAINED_GLASS_PANE,
            "&aElectric Cable &7(&eII&7)",
            "",
            "&fDamages anything that touches it",
            "",
            "&8⇨ &aIgnores owner!",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(150),
            "",
            "&eDamage: &74"
        );

        SlimefunItemStack electricCable3 = new SlimefunItemStack(
            "IDOE_ELECTRIC_CABLE_3",
            Material.IRON_BARS,
            "&aElectric Cable &7(&eIII&7)",
            "",
            "&fDamages anything that touches it",
            "",
            "&8⇨ &aIgnores owner!",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(200),
            "",
            "&eDamage: &76"
        );

        SlimefunItemStack radiationAbsorber = new SlimefunItemStack(
            "IDOE_RADIATION_ABSORBER",
            Material.GREEN_CONCRETE,
            "&aRadiation Absorber",
            "",
            LoreBuilder.range(2),
            "",
            LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(250)
        );

        SlimefunItemStack radiationAbsorber2 = new SlimefunItemStack(
            "IDOE_RADIATION_ABSORBER_2",
            Material.LIME_CONCRETE,
            "&aRadiation Absorber &7(&eII&7)",
            "",
            LoreBuilder.range(4),
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(750)
        );

        SlimefunItemStack radiationAbsorber3 = new SlimefunItemStack(
            "IDOE_RADIATION_ABSORBER_3",
            Material.SLIME_BLOCK,
            "&aRadiation Absorber &7(&eIII&7)",
            "",
            LoreBuilder.range(7),
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(1800)
        );

        SlimefunItemStack electricBlastFurnace = new SlimefunItemStack(
            "IDOE_ELECTRIC_BLASTFURNACE",
            Material.BLAST_FURNACE,
            "&aElectric Blast Furnace",
            "",
            LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE),
            LoreBuilder.speed(2),
            LoreBuilder.powerPerSecond(48)
        );

        SlimefunItemStack electricBlastFurnace2 = new SlimefunItemStack(
            "IDOE_ELECTRIC_BLASTFURNACE_2",
            Material.BLAST_FURNACE,
            "&aElectric Blast Furnace &7(&eII&7)",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.speed(4),
            LoreBuilder.powerPerSecond(80)
        );

        SlimefunItemStack electricBlastFurnace3 = new SlimefunItemStack(
            "IDOE_ELECTRIC_BLASTFURNACE_3",
            Material.BLAST_FURNACE,
            "&aElectric Blast Furnace &7(&eIII&7)",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.speed(8),
            LoreBuilder.powerPerSecond(120)
        );

        SlimefunItemStack electricSmoker = new SlimefunItemStack(
            "IDOE_ELECTRIC_SMOKER",
            Material.SMOKER,
            "&aElectric Smoker",
            "",
            LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE),
            LoreBuilder.speed(2),
            LoreBuilder.powerPerSecond(48)
        );

        SlimefunItemStack electricSmoker2 = new SlimefunItemStack(
            "IDOE_ELECTRIC_SMOKER_2",
            Material.SMOKER,
            "&aElectric Smoker &7(&eII&7)",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.speed(4),
            LoreBuilder.powerPerSecond(80)
        );

        SlimefunItemStack electricSmoker3 = new SlimefunItemStack(
            "IDOE_ELECTRIC_SMOKER_3",
            Material.SMOKER,
            "&aElectric Smoker &7(&eIII&7)",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.speed(8),
            LoreBuilder.powerPerSecond(120)
        );

        SlimefunItemStack electricLogStripper = new SlimefunItemStack(
            "IDOE_ELECTRIC_LOG_STRIPPER",
            Material.STONECUTTER,
            "&aElectric Log Stripper",
            "",
            LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE),
            LoreBuilder.speed(2),
            LoreBuilder.powerPerSecond(48)
        );

        SlimefunItemStack electricLogStripper2 = new SlimefunItemStack(
            "IDOE_ELECTRIC_LOG_STRIPPER_2",
            Material.STONECUTTER,
            "&aElectric Log Stripper &7(&eII&7)",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.speed(4),
            LoreBuilder.powerPerSecond(80)
        );

        SlimefunItemStack electricLogStripper3 = new SlimefunItemStack(
            "IDOE_ELECTRIC_LOG_STRIPPER_3",
            Material.STONECUTTER,
            "&aElectric Log Stripper &7(&eIII&7)",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.speed(8),
            LoreBuilder.powerPerSecond(120)
        );

        SlimefunItemStack electricShearer = new SlimefunItemStack(
            "IDOE_ELECTRIC_SHEARER",
            Material.LOOM,
            "&aElectric Shearer",
            "",
            LoreBuilder.range(4),
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(120)
        );

        SlimefunItemStack electricPoisonExtractor = new SlimefunItemStack(
            "IDOE_ELECTRIC_POISON_EXTRACTOR",
            Material.GREEN_GLAZED_TERRACOTTA,
            "&aElectric Poison Extractor",
            "",
            "&fCreates poison potions using",
            "&fingredients and a bottle",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.speed(4),
            LoreBuilder.powerPerSecond(80)
        );

        SlimefunItemStack zombieRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_ZOMBIE",
            REPELLER,
            "&aZombie Repeller",
            "",
            "&fPlace this to repel all",
            "&fZombies in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(125)
        );

        SlimefunItemStack spiderRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_SPIDER",
            REPELLER,
            "&aSpider Repeller",
            "",
            "&fPlace this to repel all",
            "&fSpiders in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(125)
        );

        SlimefunItemStack skeletonRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_SKELETON",
            REPELLER,
            "&aSkeleton Repeller",
            "",
            "&fPlace this to repel all",
            "&fSkeletons in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(125)
        );

        SlimefunItemStack creeperRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_CREEPER",
            REPELLER,
            "&aCreeper Repeller",
            "",
            "&fPlace this to repel all",
            "&fCreepers in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(125)
        );

        SlimefunItemStack slimeRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_SLIME",
            REPELLER,
            "&aSlime Repeller",
            "",
            "&fPlace this to repel all",
            "&fSlimes in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(175)
        );

        SlimefunItemStack zombieVillagerRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_ZOMBIE_VILLAGER",
            REPELLER,
            "&aZombie Villager Repeller",
            "",
            "&fPlace this to repel all",
            "&fZombie Villagers in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(125)
        );

        SlimefunItemStack batRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_BAT",
            REPELLER,
            "&aBat Repeller",
            "",
            "&fPlace this to repel all",
            "&fBats in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(75)
        );

        SlimefunItemStack endermanRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_ENDERMAN",
            REPELLER,
            "&aEnderman Repeller",
            "",
            "&fPlace this to repel all",
            "&fEnderman in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(125)
        );

        SlimefunItemStack witchRepeller = new SlimefunItemStack(
            "IDOE_REPELLER_WITCH",
            REPELLER,
            "&aWitch Repeller",
            "",
            "&fPlace this to repel all",
            "&fWitches in a chunk",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(125)
        );

        //////////////////////////////////////////////
        /////////////////// TOOLS ////////////////////
        //////////////////////////////////////////////

        SlimefunItemStack magnetoid = new SlimefunItemStack(
            "IDOE_MAGNETOID",
            Material.ECHO_SHARD,
            "&aMagnetoid",
            "",
            "&fMagnetoids attract nearby items",
            "",
            LoreBuilder.powerCharged(0, 50),
            LoreBuilder.range(8),
            "",
            "&eOffhand &7to use"
        );

        SlimefunItemStack jawn = new SlimefunItemStack(
            "IDOE_JAWN",
            Material.BONE,
            "&aJawn",
            "",
            "&fInstantly break any head",
            "",
            LoreBuilder.RIGHT_CLICK_TO_USE
        );

        SlimefunItemStack lavaBoat = new SlimefunItemStack(
            "IDOE_LAVABOAT",
            Material.DARK_OAK_BOAT,
            "&aLava Boat",
            "",
            "&fPlace a fire proof boat",
            "&fat your feet to traverse",
            "&fover lava",
            "",
            "&8⇨ &4Not buoyant in water!",
            "&8⇨ &4Hot!",
            "",
            LoreBuilder.RIGHT_CLICK_TO_USE
        );

        SlimefunItemStack trimVault = new SlimefunItemStack(
            "IDOE_TRIM_VAULT",
            TRIM_VAULT,
            "&aTrim Vault",
            "",
            "&fBreak to receive a random",
            "&fSmithing Template",
            ""
        );

        SlimefunItemStack wisterShears = new SlimefunItemStack(
            "IDOE_WISTER_SHEARS",
            Material.SHEARS,
            "&aWister Shears",
            "",
            "&fShear all the things",
            "",
            "&8⇨ &aIndestructible!",
            "",
            "&eLeft Click&7 to use on leaves/grass",
            "&eRight Click&7 to use on mobs"
        );

        ItemMeta wistermeta = wisterShears.getItemMeta();
        wistermeta.setUnbreakable(true);
        wistermeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        wisterShears.setItemMeta(wistermeta);

        SlimefunItemStack chisel = new SlimefunItemStack(
            "IDOE_CHISEL",
            Material.POINTED_DRIPSTONE,
            "&aChisel",
            "",
            "&fChanges some blocks to",
            "&ftheir chiseled form",
            "",
            LoreBuilder.powerCharged(0, 250),
            "",
            LoreBuilder.RIGHT_CLICK_TO_USE
        );

        SlimefunItemStack electricPick = new SlimefunItemStack(
            "IDOE_ELECTRIC_EXPLOSIVE_PICKAXE",
            Material.DIAMOND_PICKAXE,
            "&aElectric Explosive Pickaxe",
            "",
            "&fAn electric explosive pickaxe",
            "",
            LoreBuilder.powerCharged(0, 700)
        );
        ItemMeta pickMeta = electricPick.getItemMeta();
        pickMeta.setUnbreakable(true);
        pickMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        electricPick.setItemMeta(pickMeta);

        SlimefunItemStack electricShovel = new SlimefunItemStack(
            "IDOE_ELECTRIC_EXPLOSIVE_SHOVEL",
            Material.DIAMOND_SHOVEL,
            "&aElectric Explosive Shovel",
            "",
            "&fAn electric explosive shovel",
            "",
            LoreBuilder.powerCharged(0, 900)
        );

        ItemMeta shovelMeta = electricShovel.getItemMeta();
        shovelMeta.setUnbreakable(true);
        shovelMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        electricShovel.setItemMeta(shovelMeta);

        SlimefunItemStack biomeCompass = new SlimefunItemStack(
            "IDOE_BIOMECOMPASS",
            Material.COMPASS,
            "&aBiome Compass",
            "",
            "&fFind a biome within " + BiomeCompass.getRange(),
            "&fblocks. Prioritizes new areas.",
            "",
            "&8⇨ &aRemembers locations!",
            "",
            "&eSneak + R-Click AIR&7 Next Biome",
            "&eSneak + R-Click BLOCK&7 Prev. Biome",
            "&eR-Click &7to search for Biome"
        );

        SlimefunItemStack slimeMeal = new SlimefunItemStack(
            "IDOE_SLIMEMEAL",
            Material.SLIME_BALL,
            "&aSlime Meal",
            "",
            "&fUse on a slime to",
            "&fincrease its size",
            "",
            "&8⇨ &aGrow super Slimes!",
            "",
            LoreBuilder.RIGHT_CLICK_TO_USE
        );

        SlimefunItemStack alarmClock = new SlimefunItemStack(
            "IDOE_ALARM_CLOCK",
            Material.CLOCK,
            "&aAlarm Clock",
            "",
            "&eR-Click &7to set timer",
            "&eSneak + R-Click &7toggle alarm mode"
        );

        SlimefunItemStack tomeOfEnlightenment = new SlimefunItemStack(
            "IDOE_TOME_OF_ENLIGHTENMENT",
            Material.ENCHANTED_BOOK,
            "&aTome of Enlightenment",
            "",
            "&fInjects &nrandom&r &fknowledge",
            "&fdirectly into your mind, skipping",
            "&ftedious research steps",
            "",
            "&8⇨ &aInstant research!",
            "",
            "&eR-Click &7to use"
        );

        SlimefunItemStack flameheartIdol = new SlimefunItemStack(
            "IDOE_IDOL_FLAMEHEART",
            Material.BLAZE_POWDER,
            "&c&lFlameheart Idol",
            "&eNever consumed!",
            "",
            "&8⇨ &aLava Walker: &720%",
            "&8⇨ &aFirefighter: &720%",
            "&8⇨ &aWarrior: &720%",
            "&8⇨ &aKnight: &730%",
            "",
            "&eRight Click &7to toggle messages",
            "&cFireproof"
        );

        SlimefunItemStack torrentIdol = new SlimefunItemStack(
            "IDOE_IDOL_TORRENT",
            Material.HEART_OF_THE_SEA,
            "&b&lTorrent Idol",
            "&eNever consumed!",
            "",
            "&8⇨ &aWater: &720%",
            "&8⇨ &aAngel: &775%",
            "&8⇨ &aWhirlwind: &760%",
            "&8⇨ &aTraveller: &760%",
            "",
            "&eRight Click &7to toggle messages",
            "&cFireproof"
        );

        SlimefunItemStack terranIdol = new SlimefunItemStack(
            "IDOE_IDOL_TERRAN",
            Material.HONEYCOMB,
            "&a&lTerran Idol",
            "&eNever consumed!",
            "",
            "&8⇨ &aCaveman: &750%",
            "&8⇨ &aMiner: &720%",
            "&8⇨ &aFarmer: &720%",
            "&8⇨ &aHunter: &720%",
            "",
            "&eRight Click &7to toggle messages",
            "&cFireproof"
        );

        SlimefunItemStack divineIdol = new SlimefunItemStack(
            "IDOE_IDOL_DIVINE",
            Material.AMETHYST_SHARD,
            "&d&lDivine Idol",
            "&eNever consumed!",
            "",
            "&8⇨ &aMagician: &780%",
            "&8⇨ &aWizard: &720%",
            "&8⇨ &aWise: &720%",
            "&8⇨ &aAnvil: &720%",
            "",
            "&eRight Click &7to toggle messages",
            "&cFireproof"
        );

        SlimefunItemStack badOmenPotion = new SlimefunItemStack(
            "IDOE_POTION_BAD_OMEN",
            Color.BLACK,
            new PotionEffect(PotionEffectType.BAD_OMEN, 6000, 0),
            "&aPotion of Bad Omen"
        );
        IDOEUtility.setGlow(badOmenPotion);

        SlimefunItemStack creativeTransmitter = new SlimefunItemStack(
            "IDOE_CREATIVE_TRANSMITTER",
            CREATIVE_TRANSMITTER,
            "&aCreative GPS Transmitter",
            "",
            "&fA GPS Transmitter that provides",
            "&falmost infinite GPS complexity",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(2),
            "",
            "&e/sf cheat &7to receive"
        );
        IDOEUtility.setGlow(creativeTransmitter);

        SlimefunItemStack creativeCapacitor = new SlimefunItemStack(
            "IDOE_CREATIVE_CAPACITOR",
            CREATIVE_CAPACITOR,
            "&aCreative Capacitor",
            LoreBuilder.range(6),
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.CAPACITOR),
            "&8⇨ &e⚡ &7 " + Integer.MAX_VALUE + " Capacity",
            "",
            "&e/sf cheat &7to receive"
        );
        IDOEUtility.setGlow(creativeCapacitor);

        SlimefunItemStack creativeGenerator = new SlimefunItemStack(
            "IDOE_CREATIVE_GENERATOR",
            CREATIVE_GENERATOR,
            "&aCreative Generator",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            LoreBuilder.powerPerSecond(Integer.MAX_VALUE),
            "",
            "&e/sf cheat &7to receive"
        );
        IDOEUtility.setGlow(creativeGenerator);

        //########################
        //  RECIPES
        //########################

        //////////////////////////////////////////////
        ///////////////// MACHINES ///////////////////
        //////////////////////////////////////////////
        ItemStack[] playerHopperRecipe = {
            SlimefunItems.ENERGY_CONNECTOR.item(), SlimefunItems.EARTH_RUNE.item(), SlimefunItems.INFUSED_MAGNET.item(),
            SlimefunItems.ENDER_RUNE.item(), SlimefunItems.INFUSED_HOPPER.item(), SlimefunItems.ENDER_RUNE.item(),
            SlimefunItems.INFUSED_MAGNET.item(), SlimefunItems.EARTH_RUNE.item(), SlimefunItems.ENERGY_CONNECTOR.item()
        };

        ItemStack[] supplyHopperRecipe = {
            SlimefunItems.ENERGY_CONNECTOR.item(), SlimefunItems.EARTH_RUNE.item(), SlimefunItems.INFUSED_MAGNET.item(),
            SlimefunItems.ENDER_RUNE.item(), new ItemStack(Material.DROPPER), SlimefunItems.ENDER_RUNE.item(),
            SlimefunItems.INFUSED_MAGNET.item(), SlimefunItems.EARTH_RUNE.item(), SlimefunItems.ENERGY_CONNECTOR.item()
        };

        ItemStack[] stackDispenserRecipe = {
            SlimefunItems.STEEL_PLATE.item(), SlimefunItems.BATTERY.item(), SlimefunItems.STEEL_PLATE.item(),
            SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.BLOCK_PLACER.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.STEEL_PLATE.item(), SlimefunItems.BATTERY.item(), SlimefunItems.STEEL_PLATE.item()
        };

        ItemStack[] electricCableRecipe = {
            new ItemStack(Material.CHAIN), new ItemStack(Material.CHAIN), new ItemStack(Material.CHAIN),
            new ItemStack(Material.CHAIN), SlimefunItems.ENERGY_CONNECTOR.item(), new ItemStack(Material.CHAIN),
            new ItemStack(Material.CHAIN), new ItemStack(Material.CHAIN), new ItemStack(Material.CHAIN)
        };

        ItemStack[] electricCable2Recipe = {
            electricCable.item(), electricCable.item(), electricCable.item(),
            electricCable.item(), SlimefunItems.ENERGY_CONNECTOR.item(), electricCable.item(),
            electricCable.item(), electricCable.item(), electricCable.item()
        };

        ItemStack[] electricCable3Recipe = {
            electricCable2.item(), electricCable2.item(), electricCable2.item(),
            electricCable2.item(), SlimefunItems.ENERGY_CONNECTOR.item(), electricCable2.item(),
            electricCable2.item(), electricCable2.item(), electricCable2.item()
        };

        ItemStack[] radiationAbsorberRecipe = {
            SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.NETHER_ICE_COOLANT_CELL.item(), SlimefunItems.REINFORCED_PLATE.item(),
            SlimefunItems.HEATING_COIL.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.HEATING_COIL.item(),
            SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.NETHER_ICE_COOLANT_CELL.item(), SlimefunItems.REINFORCED_PLATE.item()
        };

        ItemStack[] radiationAbsorber2Recipe = {
            SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.NETHER_ICE_COOLANT_CELL.item(), SlimefunItems.REINFORCED_PLATE.item(),
            SlimefunItems.HEATING_COIL.item(), radiationAbsorber.item(), SlimefunItems.HEATING_COIL.item(),
            SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.NETHER_ICE_COOLANT_CELL.item(), SlimefunItems.REINFORCED_PLATE.item()
        };

        ItemStack[] radiationAbsorber3Recipe = {
            SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.NETHER_ICE_COOLANT_CELL.item(), SlimefunItems.REINFORCED_PLATE.item(),
            SlimefunItems.HEATING_COIL.item(), radiationAbsorber2.item(), SlimefunItems.HEATING_COIL.item(),
            SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.NETHER_ICE_COOLANT_CELL.item(), SlimefunItems.REINFORCED_PLATE.item()
        };

        ItemStack[] electricBlastFurnaceRecipe = {
            null, SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.BLAST_FURNACE), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.HEATING_COIL.item()
        };

        ItemStack[] electricBlastFurnaceRecipe2 = {
            null, SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), electricBlastFurnace.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.HEATING_COIL.item()
        };

        ItemStack[] electricBlastFurnaceRecipe3 = {
            null, SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), electricBlastFurnace2.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.HEATING_COIL.item()
        };

        ItemStack[] electricSmokerRecipe = {
            new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.SMOKER), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.IGNITION_CHAMBER.item()
        };

        ItemStack[] electricSmokerRecipe2 = {
            new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), electricSmoker.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.IGNITION_CHAMBER.item()
        };

        ItemStack[] electricSmokerRecipe3 = {
            new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), electricSmoker2.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.HEATING_COIL.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.IGNITION_CHAMBER.item()
        };

        ItemStack[] electricLogStripperRecipe = {
            SlimefunItems.LUMBER_AXE.item(), SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.STONECUTTER), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.COPPER_WIRE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.COPPER_WIRE.item()
        };

        ItemStack[] electricLogStripperRecipe2 = {
            SlimefunItems.LUMBER_AXE.item(), SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), electricLogStripper.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.COPPER_WIRE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.COPPER_WIRE.item()
        };

        ItemStack[] electricLogStripperRecipe3 = {
            SlimefunItems.LUMBER_AXE.item(), SlimefunItems.BATTERY.item(), null,
            SlimefunItems.ELECTRIC_MOTOR.item(), electricLogStripper2.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            SlimefunItems.COPPER_WIRE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.COPPER_WIRE.item()
        };

        ItemStack[] electricShererRecipe = {
            SlimefunItems.SOLAR_PANEL.item(), SlimefunItems.BATTERY.item(), SlimefunItems.SOLAR_PANEL.item(),
            SlimefunItems.GOLD_22K.item(), new ItemStack(Material.SHEARS), SlimefunItems.GOLD_22K.item(),
            SlimefunItems.GOLD_22K.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.GOLD_22K.item()
        };

        ItemStack[] electricPoisonExtractorRecipe = {
            null, SlimefunItems.HARDENED_GLASS.item(), null,
            SlimefunItems.CARBONADO.item(), SlimefunItems.AUTO_BREWER.item(), SlimefunItems.CARBONADO.item(),
            SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item(), SlimefunItems.CORINTHIAN_BRONZE_INGOT.item()
        };

        ItemStack[] zombieRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.ZOMBIE_HEAD), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] spiderRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.SPIDER_EYE), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] skeletonRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.SKELETON_SKULL), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] creeperRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.CREEPER_HEAD), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] slimeRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.SLIME_BLOCK), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] zombieVillagerRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.ROTTEN_FLESH), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] batRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.APPLE), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] endermanRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.ENDER_EYE), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        ItemStack[] witchRepellerRecipe = {
            new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.GLOWSTONE_DUST), SlimefunItems.GRAPPLING_HOOK.item(),
            SlimefunItems.MAGICAL_GLASS.item(), SlimefunItems.NECROTIC_SKULL.item(), SlimefunItems.MAGICAL_GLASS.item(),
            SlimefunItems.TRASH_CAN.item(), SlimefunItems.LAVA_CRYSTAL.item(), SlimefunItems.NEPTUNIUM.item()
        };

        //////////////////////////////////////////////
        /////////////////// TOOLS ////////////////////
        //////////////////////////////////////////////
        ItemStack[] magnetoidRecipe = {
            null, SlimefunItems.GRAPPLING_HOOK.item(), null,
            SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.INFUSED_MAGNET.item(), SlimefunItems.REDSTONE_ALLOY.item(),
            null, SlimefunItems.ELECTRIC_MOTOR.item(), null
        };

        ItemStack[] jawnRecipe = {
            SlimefunItems.ENCHANTMENT_RUNE.item(), SlimefunItems.ENCHANTMENT_RUNE.item(), SlimefunItems.ENCHANTMENT_RUNE.item(),
            SlimefunItems.ENCHANTMENT_RUNE.item(), new ItemStack(Material.BONE), SlimefunItems.ENCHANTMENT_RUNE.item(),
            SlimefunItems.ENCHANTMENT_RUNE.item(), SlimefunItems.ENCHANTMENT_RUNE.item(), SlimefunItems.ENCHANTMENT_RUNE.item()
        };

        ItemStack[] lavaBoatRecipe = {
            null, null, null,
            new ItemStack(Material.OBSIDIAN), SlimefunItems.ELECTRIC_MOTOR.item(), new ItemStack(Material.OBSIDIAN),
            new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN)
        };

        ItemStack[] trimVaultRecipe = {
            SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.SALT.item(), SlimefunItems.PLASTIC_SHEET.item(),
            SlimefunItems.STEEL_PLATE.item(), new ItemStack(Material.NETHERITE_INGOT), SlimefunItems.STEEL_PLATE.item(),
            SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.OUTPUT_CHEST.item(), SlimefunItems.PLASTIC_SHEET.item()
        };

        ItemStack[] wisterShearsRecipe = {
            SlimefunItems.EARTH_RUNE.item(), new ItemStack(Material.SHORT_GRASS), SlimefunItems.EARTH_RUNE.item(),
            SlimefunItems.EARTH_RUNE.item(), new ItemStack(Material.SHEARS), SlimefunItems.EARTH_RUNE.item(),
            SlimefunItems.EARTH_RUNE.item(), new ItemStack(Material.OAK_LEAVES), SlimefunItems.EARTH_RUNE.item()
        };

        ItemStack[] biomeCompassRecipe = {
            new ItemStack(Material.ICE), new ItemStack(Material.GRASS_BLOCK), new ItemStack(Material.MAGMA_BLOCK),
            new ItemStack(Material.PHANTOM_MEMBRANE), new ItemStack(Material.COMPASS), new ItemStack(Material.SPYGLASS),
            new ItemStack(Material.CRIMSON_FUNGUS), new ItemStack(Material.SCULK), new ItemStack(Material.END_STONE)
        };

        ItemStack[] chiselRecipe = {
            SlimefunItems.ALUMINUM_BRASS_INGOT.item(), null, SlimefunItems.ALUMINUM_BRASS_INGOT.item(),
            SlimefunItems.ALUMINUM_BRASS_INGOT.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.ALUMINUM_BRASS_INGOT.item(),
            null, SlimefunItems.ALUMINUM_BRASS_INGOT.item(), null,
        };

        ItemStack[] electricPickRecipe = {
            SlimefunItems.SYNTHETIC_DIAMOND.item(), SlimefunItems.EXPLOSIVE_PICKAXE.item(), SlimefunItems.SYNTHETIC_DIAMOND.item(),
            null, SlimefunItems.BIG_CAPACITOR.item(), null,
            null, SlimefunItems.SYNTHETIC_DIAMOND.item(), null,
        };

        ItemStack[] electricShovelRecipe = {
            null, SlimefunItems.EXPLOSIVE_SHOVEL.item(), null,
            null, SlimefunItems.BIG_CAPACITOR.item(), null,
            null, SlimefunItems.SYNTHETIC_DIAMOND.item(), null,
        };

        ItemStack[] slimeMealRecipe = {
            new ItemStack(Material.BONE_MEAL), new ItemStack(Material.BONE_MEAL), new ItemStack(Material.BONE_MEAL),
            new ItemStack(Material.BONE_MEAL), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.BONE_MEAL),
            new ItemStack(Material.BONE_MEAL), new ItemStack(Material.BONE_MEAL), new ItemStack(Material.BONE_MEAL)
        };

        ItemStack[] alarmClockRecipe = {
            null, SlimefunItems.SOLAR_PANEL.item(), null,
            SlimefunItems.REDSTONE_ALLOY.item(), new ItemStack(Material.CLOCK), SlimefunItems.REDSTONE_ALLOY.item(),
            null, SlimefunItems.SMALL_CAPACITOR.item(), null
        };

        ItemStack[] tomeOfEnlightenmentRecipe = {
            null, new ItemStack(Material.FEATHER), null,
            new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.WRITABLE_BOOK), new ItemStack(Material.LAPIS_LAZULI),
            null, new ItemStack(Material.BLACK_DYE), null
        };

        //////////////////////////////////////////////
        /////////////////// IDOLS ////////////////////
        //////////////////////////////////////////////

        ItemStack[] flamheartIdolRecipe = {
            SlimefunItem.getById("ENDER_LAVA_TALISMAN").getItem(), SlimefunItems.TALISMAN_LAVA.item(), SlimefunItem.getById("ENDER_WARRIOR_TALISMAN").getItem(),
            SlimefunItems.TALISMAN_WARRIOR.item(), SlimefunItems.BOOSTED_URANIUM.item(), SlimefunItems.TALISMAN_KNIGHT.item(),
            SlimefunItem.getById("ENDER_KNIGHT_TALISMAN").getItem(), SlimefunItems.TALISMAN_FIRE.item(), SlimefunItem.getById("ENDER_FIRE_TALISMAN").getItem()
        };

        ItemStack[] torrentIdolRecipe = {
            SlimefunItem.getById("ENDER_WATER_TALISMAN").getItem(), SlimefunItems.TALISMAN_WATER.item(), SlimefunItem.getById("ENDER_ANGEL_TALISMAN").getItem(),
            SlimefunItems.TALISMAN_ANGEL.item(), SlimefunItems.BOOSTED_URANIUM.item(), SlimefunItems.TALISMAN_WHIRLWIND.item(),
            SlimefunItem.getById("ENDER_WHIRLWIND_TALISMAN").getItem(), SlimefunItems.TALISMAN_TRAVELLER.item(), SlimefunItem.getById("ENDER_TRAVELLER_TALISMAN").getItem()
        };

        ItemStack[] terranIdolRecipe = {
            SlimefunItem.getById("ENDER_CAVEMAN_TALISMAN").getItem(), SlimefunItems.TALISMAN_CAVEMAN.item(), SlimefunItem.getById("ENDER_MINER_TALISMAN").getItem(),
            SlimefunItems.TALISMAN_MINER.item(), SlimefunItems.BOOSTED_URANIUM.item(), SlimefunItems.TALISMAN_FARMER.item(),
            SlimefunItem.getById("ENDER_FARMER_TALISMAN").getItem(), SlimefunItems.TALISMAN_HUNTER.item(), SlimefunItem.getById("ENDER_HUNTER_TALISMAN").getItem()
        };

        ItemStack[] divineIdolRecipe = {
            SlimefunItem.getById("ENDER_MAGICIAN_TALISMAN").getItem(), SlimefunItems.TALISMAN_MAGICIAN.item(), SlimefunItem.getById("ENDER_WIZARD_TALISMAN").getItem(),
            SlimefunItems.TALISMAN_WIZARD.item(), SlimefunItems.BOOSTED_URANIUM.item(), SlimefunItems.TALISMAN_WISE.item(),
            SlimefunItem.getById("ENDER_WISE_TALISMAN").getItem(), SlimefunItems.TALISMAN_ANVIL.item(), SlimefunItem.getById("ENDER_ANVIL_TALISMAN").getItem()
        };

        ItemStack[] badOmenPotionRecipe = {
            null, new ItemStack(Material.FERMENTED_SPIDER_EYE), null,
            new ItemStack(Material.POISONOUS_POTATO), SlimefunItems.MEDICINE.item(), new ItemStack(Material.GHAST_TEAR),
            null, new ItemStack(Material.NETHER_WART), null,
        };

        //########################
        //  INSTANTIATE
        //########################

        SlimefunItem playerHopperItem = new PlayerHopper(group, playerHopper, RecipeType.MAGIC_WORKBENCH, playerHopperRecipe, 500, 5000);
        SlimefunItem supplyHopperItem = new SupplyHopper(group, supplyHopper, RecipeType.MAGIC_WORKBENCH, supplyHopperRecipe, 500, 5000);
        SlimefunItem stackDispenserItem = new StackDispenser(group, stackDispenser, RecipeType.MAGIC_WORKBENCH, stackDispenserRecipe);
        SlimefunItem electricCableItem = new ElectricCable(group, electricCable, RecipeType.MAGIC_WORKBENCH, electricCableRecipe, IDOEUtility.output(electricCable.item(), 8), 200, 2000, 2.0, 0.5);
        SlimefunItem electricCableItem2 = new ElectricCable(group, electricCable2, RecipeType.MAGIC_WORKBENCH, electricCable2Recipe, IDOEUtility.output(electricCable2.item(), 4), 300, 3000, 4.0, 1);
        SlimefunItem electricCableItem3 = new ElectricCable(group, electricCable3, RecipeType.MAGIC_WORKBENCH, electricCable3Recipe, IDOEUtility.output(electricCable3.item(), 2), 400, 4000, 6.0, 2);

        SlimefunItem radiationabsorber = new RadiationAbsorber(group, radiationAbsorber, RecipeType.ENHANCED_CRAFTING_TABLE, radiationAbsorberRecipe, 250, 1600, 2);
        SlimefunItem radiationabsorber2 = new RadiationAbsorber(group, radiationAbsorber2, RecipeType.ENHANCED_CRAFTING_TABLE, radiationAbsorber2Recipe, 750, 3800, 4);
        SlimefunItem radiationabsorber3 = new RadiationAbsorber(group, radiationAbsorber3, RecipeType.ENHANCED_CRAFTING_TABLE, radiationAbsorber3Recipe, 1800, 12300, 7);

        SlimefunItem electricBlastFurnaceItem = new ElectricBlastFurnace(group, electricBlastFurnace, RecipeType.ENHANCED_CRAFTING_TABLE, electricBlastFurnaceRecipe).setCapacity(1024).setEnergyConsumption(24).setProcessingSpeed(2);
        SlimefunItem electricBlastFurnaceItem2 = new ElectricBlastFurnace(group, electricBlastFurnace2, RecipeType.ENHANCED_CRAFTING_TABLE, electricBlastFurnaceRecipe2).setCapacity(1024).setEnergyConsumption(48).setProcessingSpeed(4);
        SlimefunItem electricBlastFurnaceItem3 = new ElectricBlastFurnace(group, electricBlastFurnace3, RecipeType.ENHANCED_CRAFTING_TABLE, electricBlastFurnaceRecipe3).setCapacity(1024).setEnergyConsumption(60).setProcessingSpeed(8);

        SlimefunItem electricSmokerItem = new ElectricSmoker(group, electricSmoker, RecipeType.ENHANCED_CRAFTING_TABLE, electricSmokerRecipe).setCapacity(1024).setEnergyConsumption(24).setProcessingSpeed(2);
        SlimefunItem electricSmokerItem2 = new ElectricSmoker(group, electricSmoker2, RecipeType.ENHANCED_CRAFTING_TABLE, electricSmokerRecipe2).setCapacity(1024).setEnergyConsumption(48).setProcessingSpeed(4);
        SlimefunItem electricSmokerItem3 = new ElectricSmoker(group, electricSmoker3, RecipeType.ENHANCED_CRAFTING_TABLE, electricSmokerRecipe3).setCapacity(1024).setEnergyConsumption(60).setProcessingSpeed(8);

        SlimefunItem electricLogStripperItem = new ElectricLogStripper(group, electricLogStripper, RecipeType.ENHANCED_CRAFTING_TABLE, electricLogStripperRecipe).setCapacity(1024).setEnergyConsumption(24).setProcessingSpeed(2);
        SlimefunItem electricLogStripperItem2 = new ElectricLogStripper(group, electricLogStripper2, RecipeType.ENHANCED_CRAFTING_TABLE, electricLogStripperRecipe2).setCapacity(1024).setEnergyConsumption(48).setProcessingSpeed(4);
        SlimefunItem electricLogStripperItem3 = new ElectricLogStripper(group, electricLogStripper3, RecipeType.ENHANCED_CRAFTING_TABLE, electricLogStripperRecipe3).setCapacity(1024).setEnergyConsumption(60).setProcessingSpeed(8);

        SlimefunItem electricShearerItem = new ElectricShearer(group, electricShearer, RecipeType.ENHANCED_CRAFTING_TABLE, electricShererRecipe, 120, 1024, 4);
        SlimefunItem electricPoisonExtractorItem = new ElectricPoisonExtractor(group, electricPoisonExtractor, RecipeType.ENHANCED_CRAFTING_TABLE, electricPoisonExtractorRecipe).setCapacity(1024).setEnergyConsumption(48).setProcessingSpeed(4);

        SlimefunItem zombieRepellerItem = new ZombieRepeller(group, zombieRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, zombieRepellerRecipe, 250, 1200);
        SlimefunItem spiderRepellerItem = new SpiderRepeller(group, spiderRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, spiderRepellerRecipe, 250, 1200);
        SlimefunItem skeletonRepellerItem = new SkeletonRepeller(group, skeletonRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, skeletonRepellerRecipe, 250, 1200);
        SlimefunItem creeperRepellerItem = new CreeperRepeller(group, creeperRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, creeperRepellerRecipe, 250, 1200);
        SlimefunItem slimeRepellerItem = new SlimeRepeller(group, slimeRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, slimeRepellerRecipe, 350, 1200);
        SlimefunItem zombieVillagerRepellerItem = new ZombieVillagerRepeller(group, zombieVillagerRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, zombieVillagerRepellerRecipe, 350, 1200);
        SlimefunItem batRepellerItem = new BatRepeller(group, batRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, batRepellerRecipe, 150, 1200);
        SlimefunItem endermanRepellerItem = new EndermanRepeller(group, endermanRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, endermanRepellerRecipe, 250, 1200);
        SlimefunItem witchRepellerItem = new WitchRepeller(group, witchRepeller, RecipeType.ENHANCED_CRAFTING_TABLE, witchRepellerRecipe, 250, 1200);

        SlimefunItem magnetoidItem = new Magnetoid(group, magnetoid, RecipeType.MAGIC_WORKBENCH, magnetoidRecipe, 50);
        SlimefunItem jawnItem = new Jawn(group, jawn, RecipeType.MAGIC_WORKBENCH, jawnRecipe);
        SlimefunItem lavaBoatItem = new LavaBoat(group, lavaBoat, RecipeType.MAGIC_WORKBENCH, lavaBoatRecipe);
        SlimefunItem trimVaultItem = new TrimVault(group, trimVault, RecipeType.MAGIC_WORKBENCH, trimVaultRecipe);
        SlimefunItem wisterShearsItem = new WisterShears(group, wisterShears, RecipeType.MAGIC_WORKBENCH, wisterShearsRecipe);
        SlimefunItem biomeCompassItem = new BiomeCompass(group, biomeCompass, RecipeType.MAGIC_WORKBENCH, biomeCompassRecipe);
        SlimefunItem chiselItem = new Chisel(group, chisel, RecipeType.MAGIC_WORKBENCH, chiselRecipe, 250);
        SlimefunItem electricPickItem = new ElectricExplosivePickaxe(group, electricPick, RecipeType.MAGIC_WORKBENCH, electricPickRecipe, 700);
        SlimefunItem electricShovelItem = new ElectricExplosiveShovel(group, electricShovel, RecipeType.MAGIC_WORKBENCH, electricShovelRecipe, 900);
        SlimefunItem slimeMealItem = new SlimeMeal(group, slimeMeal, RecipeType.MAGIC_WORKBENCH, slimeMealRecipe);
        SlimefunItem alarmClockItem = new AlarmClock(group, alarmClock, RecipeType.MAGIC_WORKBENCH, alarmClockRecipe);
        SlimefunItem tomeOfEnlightenmentItem = new TomeOfEnlightenment(group, tomeOfEnlightenment, RecipeType.ENHANCED_CRAFTING_TABLE, tomeOfEnlightenmentRecipe);

        SlimefunItem badOmenPotionItem = new SlimefunItem(group, badOmenPotion, RecipeType.JUICER, badOmenPotionRecipe);

        SlimefunItem flameheartIdolItem = new FlameheartIdol(group, flameheartIdol, RecipeType.MAGIC_WORKBENCH, flamheartIdolRecipe);
        SlimefunItem torrentIdolItem = new TorrentIdol(group, torrentIdol, RecipeType.MAGIC_WORKBENCH, torrentIdolRecipe);
        SlimefunItem terranIdolItem = new TerranIdol(group, terranIdol, RecipeType.MAGIC_WORKBENCH, terranIdolRecipe);
        SlimefunItem divineIdolItem = new DivineIdol(group, divineIdol, RecipeType.MAGIC_WORKBENCH, divineIdolRecipe);

        SlimefunItem creativeTransmitterItem = new GPSTransmitter(group, 9001, creativeTransmitter, RecipeType.NULL, null) {
            @Override
            public int getMultiplier(int y) {
                return Integer.MAX_VALUE;
            }
            @Override
            public int getEnergyConsumption() {
                return 1;
            }
        };
        SlimefunItem creativeCapacitorItem = new Capacitor(group, Integer.MAX_VALUE, creativeCapacitor, RecipeType.NULL, null);
        SlimefunItem creativeGeneratorItem = new SolarGenerator(group, Integer.MAX_VALUE, Integer.MAX_VALUE, creativeGenerator, RecipeType.NULL, null);

        //########################
        //  REGISTER
        //########################

        new Terrabore(group, terrabore).register(plugin);
        new AdvancedTerrabore(group, terraboreAdvanced).register(plugin);
        new EliteTerrabore(group, terraboreElite).register(plugin);

        stackDispenserItem.register(plugin);
        playerHopperItem.register(plugin);
        supplyHopperItem.register(plugin);
        electricCableItem.register(plugin);
        electricCableItem2.register(plugin);
        electricCableItem3.register(plugin);

        flameheartIdolItem.register(plugin);
        torrentIdolItem.register(plugin);
        terranIdolItem.register(plugin);
        divineIdolItem.register(plugin);

        magnetoidItem.register(plugin);
        jawnItem.register(plugin);
        lavaBoatItem.register(plugin);
        trimVaultItem.register(plugin);
        wisterShearsItem.register(plugin);
        biomeCompassItem.register(plugin);
        chiselItem.register(plugin);
        electricPickItem.register(plugin);
        electricShovelItem.register(plugin);
        slimeMealItem.register(plugin);
        alarmClockItem.register(plugin);
        tomeOfEnlightenmentItem.register(plugin);

        badOmenPotionItem.register(plugin);

        radiationabsorber.register(plugin);
        radiationabsorber2.register(plugin);
        radiationabsorber3.register(plugin);

        electricBlastFurnaceItem.register(plugin);
        electricBlastFurnaceItem2.register(plugin);
        electricBlastFurnaceItem3.register(plugin);

        electricSmokerItem.register(plugin);
        electricSmokerItem2.register(plugin);
        electricSmokerItem3.register(plugin);

        electricLogStripperItem.register(plugin);
        electricLogStripperItem2.register(plugin);
        electricLogStripperItem3.register(plugin);

        electricPoisonExtractorItem.register(plugin);
        electricShearerItem.register(plugin);

        zombieRepellerItem.register(plugin);
        spiderRepellerItem.register(plugin);
        skeletonRepellerItem.register(plugin);
        creeperRepellerItem.register(plugin);
        slimeRepellerItem.register(plugin);
        zombieVillagerRepellerItem.register(plugin);
        batRepellerItem.register(plugin);
        endermanRepellerItem.register(plugin);
        witchRepellerItem.register(plugin);

        creativeTransmitterItem.register(plugin);
        creativeCapacitorItem.register(plugin);
        creativeGeneratorItem.register(plugin);
    }
}
package dev.aaur1s.sky_vaults_server_gen;

import com.mojang.serialization.Lifecycle;
import iskallia.vault.VaultMod;
import iskallia.vault.core.SkyVaultsChunkGenerator;
import net.minecraft.core.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.world.ForgeWorldPreset;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("sky_vaults_server_gen")
public class SkyVaultsServerGen {
    public SkyVaultsServerGen() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void registerSkyVaultsPreset(RegistryEvent.Register<ForgeWorldPreset> event) {
        if (FMLEnvironment.dist == Dist.CLIENT) return; // on client SkyVaultsPreset already registered vanilla way

        // We register for server
        event.getRegistry().register(new ForgeWorldPreset(new ForgeWorldPreset.IChunkGeneratorFactory() {
            @Override // code from SkyVaultsPreset
            public ChunkGenerator createChunkGenerator(RegistryAccess arg, long l, String string) {
                throw new UnsupportedOperationException("Use generic world settings instead");
            }
            @Override // code from SkyVaultsPreset
            public WorldGenSettings createSettings(RegistryAccess dynamicRegistries, long seed, boolean generateStructures, boolean bonusChest, String generatorSettings) {
                WritableRegistry<LevelStem> stems = new MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.stable(), null);
                Registry<DimensionType> dimensions = dynamicRegistries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
                Registry<Biome> biomes = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY);
                Registry<StructureSet> structures = dynamicRegistries.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
                Registry<NoiseGeneratorSettings> settings = dynamicRegistries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
                Registry<NormalNoise.NoiseParameters> noises = dynamicRegistries.registryOrThrow(Registry.NOISE_REGISTRY);
                stems.register(LevelStem.OVERWORLD, new LevelStem(dimensions.getOrCreateHolder(DimensionType.OVERWORLD_LOCATION), new SkyVaultsChunkGenerator(structures, HolderSet.direct(new Holder[0]), noises, MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(biomes, false), seed, settings.getOrCreateHolder(NoiseGeneratorSettings.OVERWORLD))), Lifecycle.stable());
                stems.register(LevelStem.NETHER, new LevelStem(dimensions.getOrCreateHolder(DimensionType.NETHER_LOCATION), new SkyVaultsChunkGenerator(structures, HolderSet.direct(structures.getHolderOrThrow(BuiltinStructureSets.NETHER_COMPLEXES)), noises, MultiNoiseBiomeSource.Preset.NETHER.biomeSource(biomes, generateStructures), seed, settings.getOrCreateHolder(NoiseGeneratorSettings.NETHER))), Lifecycle.stable());
                stems.register(LevelStem.END, new LevelStem(dimensions.getOrCreateHolder(DimensionType.END_LOCATION), new NoiseBasedChunkGenerator(structures, noises, new TheEndBiomeSource(biomes, seed), seed, settings.getOrCreateHolder(NoiseGeneratorSettings.END))), Lifecycle.stable());
                return new WorldGenSettings(seed, generateStructures, bonusChest, stems);
            }
        }).setRegistryName(VaultMod.MOD_ID, "sky_vaults"));
    }
}

package ru.liahim.saltmod.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.SaltConfig;
import ru.liahim.saltmod.world.AlliumPatch;
import ru.liahim.saltmod.world.NewWorldGenClay;
import ru.liahim.saltmod.world.SaltLakeGenerator;
import ru.liahim.saltmod.world.SaltMarshPlantMix;
import ru.liahim.saltmod.world.SaltWortMix;
import ru.liahim.saltmod.world.WorldGenSaltBigTree;
import ru.liahim.saltmod.world.WorldGenSaltTree;

public class SaltMarshDecorator extends SaltBiomeBaseDecorator {
	
	
	
	
	@Override
	protected void decorate() {
		
		
		
		  for (attempt = 0; attempt < 7; ++attempt) {
              (new SaltLakeGenerator()).generateOverworld(world, rand, x + offsetXZ(), z + offsetXZ());
		  }

		  
		  for (attempt = 0; attempt < 5; ++attempt) { 
			  xx = x + offsetXZ();
			  zz = z + offsetXZ();
			  yy = world.getTopSolidOrLiquidBlock(xx, zz);
			  (new NewWorldGenClay(20)).generate(world, rand, xx, yy, zz);
          }

		  xx = x + offsetXZ();
		  zz = z + offsetXZ();
		  yy = world.getTopSolidOrLiquidBlock(xx, zz);
		  if (rand.nextInt(2) == 0) {
			  (new WorldGenSaltTree(false, 3)).generate(world, rand, xx, yy, zz);
		  }
		  
		  xx = x + offsetXZ();
		  zz = z + offsetXZ();
		  yy = world.getTopSolidOrLiquidBlock(xx, zz);
		  if (rand.nextInt(10) == 0) {
			  (new WorldGenSaltBigTree(false, false)).generate(world, rand, xx, yy, zz);
		  }

		  
		  for (attempt = 0; attempt < 5; ++attempt) { 
			  xx = x + offsetXZ();
			  zz = z + offsetXZ();
			  yy = world.getTopSolidOrLiquidBlock(xx, zz);
			  (new SaltMarshPlantMix(64)).generate(world, rand, xx, yy, zz);
          }
		  
		  for (attempt = 0; attempt < 5; ++attempt) { 
			  xx = x + offsetXZ();
			  zz = z + offsetXZ();
			  yy = world.getTopSolidOrLiquidBlock(xx, zz);
			  (new SaltWortMix(32)).generate(world, rand, xx, yy, zz);
          }
		  
		  for (attempt = 0; attempt < 1; ++attempt) { 
			  xx = x + offsetXZ();
			  zz = z + offsetXZ();
			  yy = world.getTopSolidOrLiquidBlock(xx, zz);
			  (new AlliumPatch(16)).generate(world, rand, xx, yy, zz);
          }
		  
		  
		  for (attempt = 0; attempt < 4; ++attempt) { 
			  xx = x + offsetXZ();
			  zz = z + offsetXZ();
			  yy = world.getTopSolidOrLiquidBlock(xx, zz);
			  (new WorldGenWaterlily()).generate(world, rand, xx, yy, zz);
          }
		  
		  for (attempt = 0; attempt < 3; ++attempt) { 
			  xx = x + offsetXZ();
			  zz = z + offsetXZ();
			  yy = world.getTopSolidOrLiquidBlock(xx, zz);
			  (new WorldGenReed()).generate(world, rand, xx, yy, zz);
          }
		  
		  
		  if (SaltConfig.saltOreBiome) {
		  WorldGenMinable gen = new WorldGenMinable(ModBlocks.saltOre, SaltConfig.saltOreSize, Blocks.stone);
		  int maxY = 96;
		  int minY = SaltConfig.saltDeepslateOreHeight + 1;
		  int heightRange = maxY - minY;
			for (attempt = 0; attempt < SaltConfig.saltOreFrequencyBiome; attempt++) {
				xx = x + rand.nextInt(16);
				yy = rand.nextInt(heightRange) + minY;
				zz = z + rand.nextInt(16);
				gen.generate(world, rand, xx, yy, zz);
			}
		  } 
    }
}

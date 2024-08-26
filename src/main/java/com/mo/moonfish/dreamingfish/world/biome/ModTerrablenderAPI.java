package com.mo.moonfish.dreamingfish.world.biome;

import com.mo.moonfish.DreamingFish;
import com.mo.moonfish.dreamingfish.world.biome.surface.ModMaterialRules;
import net.minecraft.util.Identifier;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class ModTerrablenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new ModOverworldRegion(new Identifier(DreamingFish.MOD_ID, "overworld"), 4));

        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, DreamingFish.MOD_ID, ModMaterialRules.makeRules());
    }
}

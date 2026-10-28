package com.secretmissions;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import com.secretmissions.game.MissionEvents;

@Mod(SecretMissions.MODID)
public final class SecretMissions {
    public static final String MODID = "secretmissions";
    public SecretMissions() { NeoForge.EVENT_BUS.register(new MissionEvents()); }
}

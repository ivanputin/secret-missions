package com.secretmissions.game;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

public final class MissionData extends SavedData {
    private static final String NAME = "secretmissions_progress";
    public List<MissionCatalog.Mission> missions = new ArrayList<>();
    public int index = 0, count = 0;
    public long startedAt = 0, missionStart = 0, lastTick = 0;
    public double distance = 0;
    public boolean running = false;
    public final Set<UUID> met = new HashSet<>();
    public double lastX, lastZ;
    public boolean positionValid = false;
    public static MissionData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(MissionData::new, MissionData::load), NAME);
    }
    public static MissionData load(CompoundTag tag, HolderLookup.Provider provider) {
        MissionData d = new MissionData();
        d.index=tag.getInt("Index"); d.count=tag.getInt("Count"); d.startedAt=tag.getLong("StartedAt");
        d.missionStart=tag.getLong("MissionStart"); d.distance=tag.getDouble("Distance");
        d.running=tag.getBoolean("Running"); d.lastTick=tag.getLong("LastTick");
        ListTag list=tag.getList("Missions",Tag.TAG_COMPOUND);
        for(int i=0;i<list.size();i++) {
            CompoundTag m=list.getCompound(i);
            d.missions.add(new MissionCatalog.Mission(m.getString("Category"),m.getString("Title"),m.getString("Detail"),m.getString("Type"),m.getString("Target"),m.getInt("Amount")));
        }
        ListTag uuids=tag.getList("MetUUIDs",Tag.TAG_STRING);
        for(int i=0;i<uuids.size();i++) try { d.met.add(UUID.fromString(uuids.getString(i))); } catch(IllegalArgumentException ignored) {}
        return d;
    }
    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("Index",index);tag.putInt("Count",count);tag.putLong("StartedAt",startedAt);
        tag.putLong("MissionStart",missionStart);tag.putLong("LastTick",lastTick);
        tag.putDouble("Distance",distance);tag.putBoolean("Running",running);
        ListTag list=new ListTag();
        for(var m:missions) { CompoundTag n=new CompoundTag();n.putString("Category",m.category());n.putString("Title",m.title());n.putString("Detail",m.detail());n.putString("Type",m.type());n.putString("Target",m.target());n.putInt("Amount",m.amount());list.add(n); }
        tag.put("Missions",list);
        ListTag uuids=new ListTag(); for(UUID id:met) uuids.add(net.minecraft.nbt.StringTag.valueOf(id.toString())); tag.put("MetUUIDs",uuids);
        return tag;
    }
    public void reset(ServerLevel level) {
        missions=MissionCatalog.generate(new Random());index=0;count=0;distance=0;met.clear();
        running=true;startedAt=level.getGameTime();missionStart=startedAt;lastTick=startedAt;positionValid=false;setDirty();
    }
    public MissionCatalog.Mission current() { return running && index<missions.size()?missions.get(index):null; }
    public void advance(ServerLevel level) {
        index++;count=0;distance=0;met.clear();missionStart=level.getGameTime();lastTick=missionStart;positionValid=false;
        if(index>=10)running=false;
        setDirty();
    }
}

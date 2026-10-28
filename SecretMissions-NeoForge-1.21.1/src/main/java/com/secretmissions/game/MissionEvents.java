package com.secretmissions.game;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.*;

public final class MissionEvents {
    private static MissionData data(ServerPlayer p) { return MissionData.get((ServerLevel)p.level()); }
    private static boolean matches(MissionCatalog.Mission m,String type,String target) { return m!=null && m.type().equals(type) && (m.target().isEmpty()||m.target().equals(target)); }
    private static void add(ServerPlayer p,String type,String target,int amount) {
        MissionData d=data(p);MissionCatalog.Mission m=d.current();
        if(!matches(m,type,target))return;
        d.count+=amount;d.setDirty();
        if(d.count>=m.amount())complete(p,d,m);
    }
    private static void complete(ServerPlayer p,MissionData d,MissionCatalog.Mission m) {
        p.sendSystemMessage(Component.literal("§a✔ Миссия выполнена: "+m.title()));
        d.advance((ServerLevel)p.level());
        MissionCatalog.Mission next=d.current();
        if(next==null)p.sendSystemMessage(Component.literal("§6★ Все 10 секретных миссий выполнены!"));
        else p.sendSystemMessage(Component.literal("§eНовая миссия "+(d.index+1)+"/10: "+next.detail()));
    }
    @SubscribeEvent public void onTick(PlayerTickEvent.Post e) {
        if(!(e.getEntity() instanceof ServerPlayer p) || p.level().isClientSide())return;
        MissionData d=data(p);
        if(d.missions.isEmpty()) {d.reset((ServerLevel)p.level());p.sendSystemMessage(Component.literal("§eSecret Missions: нажми M, чтобы открыть список миссий!"));}
        MissionCatalog.Mission m=d.current();if(m==null)return;
        ServerLevel level=(ServerLevel)p.level();long now=level.getGameTime();
        if(m.type().equals("walk")) {
            if(d.positionValid && p.onGround() && !p.isPassenger()) {
                double step=Math.hypot(p.getX()-d.lastX,p.getZ()-d.lastZ);
                if(step<1.5)d.distance+=step; // ignore teleports
            }
            d.lastX=p.getX();d.lastZ=p.getZ();d.positionValid=true;
            d.count=(int)d.distance; if(d.count>=m.amount()){complete(p,d,m);return;}
            if(now%40==0)d.setDirty();
        }
        if(m.type().equals("safe")) {
            d.count=(int)Math.max(0,(now-d.missionStart)/20);
            if(d.count>=m.amount()){complete(p,d,m);return;}
            if(now%40==0)d.setDirty();
        }
        if(now%20!=0)return;
        if(m.type().equals("inventory")) {
            int total=0;
            for(ItemStack stack:p.getInventory().items) if(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(m.target()))total+=stack.getCount();
            d.count=total;
            if(total>=m.amount()){complete(p,d,m);return;}
        }
        if(m.type().equals("biome")) {
            var biome=level.getBiome(p.blockPosition()).unwrapKey();
            if(biome.isPresent() && biome.get().location().toString().equals(m.target())){complete(p,d,m);return;}
        }
        if(m.type().equals("meet")) {
            for(Entity entity:level.getEntities(p,p.getBoundingBox().inflate(5),entity->entity instanceof LivingEntity)) {
                if(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString().equals(m.target()))d.met.add(entity.getUUID());
            }
            d.count=d.met.size();d.setDirty();
            if(d.count>=m.amount())complete(p,d,m);
        }
    }
    @SubscribeEvent public void onBreak(BlockEvent.BreakEvent e) {
        if(!(e.getPlayer() instanceof ServerPlayer p))return;
        String id=BuiltInRegistries.BLOCK.getKey(e.getState().getBlock()).toString();
        MissionCatalog.Mission m=data(p).current();
        if(m!=null && m.type().equals("crop") && m.target().equals(id) && e.getState().getBlock() instanceof CropBlock crop && crop.isMaxAge(e.getState()))add(p,"crop",id,1);
        else add(p,"break",id,1);
    }
    @SubscribeEvent public void onPlace(BlockEvent.EntityPlaceEvent e) {
        if(!(e.getEntity() instanceof ServerPlayer p))return;
        add(p,"place",BuiltInRegistries.BLOCK.getKey(e.getPlacedBlock().getBlock()).toString(),1);
    }
    @SubscribeEvent public void onCraft(PlayerEvent.ItemCraftedEvent e) {
        if(!(e.getEntity() instanceof ServerPlayer p))return;
        ItemStack stack=e.getCrafting();
        add(p,"craft",BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(),stack.getCount());
    }
    @SubscribeEvent public void onKill(LivingDeathEvent e) {
        if(!(e.getSource().getEntity() instanceof ServerPlayer p))return;
        add(p,"kill",BuiltInRegistries.ENTITY_TYPE.getKey(e.getEntity().getType()).toString(),1);
    }
    @SubscribeEvent public void onDamage(LivingDamageEvent.Post e) {
        if(!(e.getEntity() instanceof ServerPlayer p))return;
        MissionData d=data(p);MissionCatalog.Mission m=d.current();
        if(m!=null && m.type().equals("safe")) {d.missionStart=((ServerLevel)p.level()).getGameTime();d.count=0;d.setDirty();}
    }
}

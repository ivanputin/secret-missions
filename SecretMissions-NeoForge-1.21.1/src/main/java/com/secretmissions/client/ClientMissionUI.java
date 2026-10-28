package com.secretmissions.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.secretmissions.SecretMissions;
import com.secretmissions.game.MissionData;
import com.secretmissions.game.MissionCatalog;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

/** Integrated server is used directly: this release is intentionally singleplayer-only. */
@EventBusSubscriber(modid=SecretMissions.MODID,value=Dist.CLIENT,bus=EventBusSubscriber.Bus.MOD)
public final class ClientMissionUI {
    public static final KeyMapping OPEN=new KeyMapping("key.secretmissions.open",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_M,"key.categories.secretmissions");
    @SubscribeEvent public static void register(RegisterKeyMappingsEvent e) {e.register(OPEN);}
    @EventBusSubscriber(modid=SecretMissions.MODID,value=Dist.CLIENT)
    public static final class Tick {
        @SubscribeEvent public static void tick(ClientTickEvent.Post e) {
            Minecraft mc=Minecraft.getInstance();
            while(OPEN.consumeClick())if(mc.player!=null && mc.hasSingleplayerServer())mc.setScreen(new MissionScreen());
        }
    }
    public static final class MissionScreen extends Screen {
        private boolean confirm=false;
        public MissionScreen() {super(Component.literal("Secret Missions"));}
        private MissionData state() {
            Minecraft mc=Minecraft.getInstance();
            return mc.getSingleplayerServer()==null?null:MissionData.get(mc.getSingleplayerServer().overworld());
        }
        @Override protected void init() {
            int x=width/2;
            addRenderableWidget(Button.builder(Component.literal("Закрыть"),b->onClose()).bounds(x-105,height-34,100,20).build());
            addRenderableWidget(Button.builder(Component.literal(confirm?"Подтвердить сброс":"Новый забег"),b->{
                if(!confirm){confirm=true;b.setMessage(Component.literal("Подтвердить сброс"));return;}
                Minecraft mc=Minecraft.getInstance();
                if(mc.getSingleplayerServer()!=null)mc.getSingleplayerServer().execute(()->MissionData.get(mc.getSingleplayerServer().overworld()).reset(mc.getSingleplayerServer().overworld()));
                confirm=false;b.setMessage(Component.literal("Новый забег"));
            }).bounds(x+5,height-34,100,20).build());
        }
        @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
            super.render(g,mouseX,mouseY,partialTick);
            int x=width/2,top=Math.max(14,height/2-118);
            g.fill(x-168,top-8,x+168,top+236,0xE918202B);
            g.drawCenteredString(font,"§6✦ SECRET MISSIONS ✦",x,top,0xFFFFFF);
            MissionData d=state();if(d==null)return;
            g.drawCenteredString(font,"Выполнено: "+Math.min(d.index,10)+" / 10",x,top+16,0xA5E6B1);
            g.fill(x-140,top+31,x+140,top+37,0xFF343C4B);
            g.fill(x-140,top+31,x-140+28*Math.min(d.index,10),top+37,0xFF66C879);
            for(int i=0;i<10;i++) {
                int y=top+46+i*17;
                String label;
                int color;
                if(i<d.index){label="✔ "+(i+1)+". "+d.missions.get(i).title();color=0xFF80D89A;}
                else if(i==d.index && d.running) {
                    MissionCatalog.Mission m=d.current();
                    label="➤ "+(i+1)+". "+m.detail()+" ("+d.count+"/"+m.amount()+")";
                    color=0xFFFFD47A;
                }else {label="🔒 "+(i+1)+". ??? Секретная миссия";color=0xFF8A94A5;}
                g.drawString(font,label,x-144,y,color,false);
            }
            if(!d.running && d.index>=10)g.drawCenteredString(font,"§aВсе миссии выполнены!",x,top+220,0xFFFFFF);
            else g.drawCenteredString(font,"Категории перемешиваются при новом забеге",x,top+220,0xB3BDCC);
        }
        @Override public boolean isPauseScreen(){return true;}
    }
}

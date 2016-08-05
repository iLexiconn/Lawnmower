package net.ilexiconn.lawnmower.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.lawnmower.client.sound.EngineSound;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class EngineSoundMessage extends AbstractMessage<EngineSoundMessage> {
    private UUID uuid;

    public EngineSoundMessage() {

    }

    public EngineSoundMessage(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, EngineSoundMessage message, EntityPlayer player, MessageContext messageContext) {
        EntityPlayer target = client.theWorld.getPlayerEntityByUUID(message.uuid);
        if (target != null) {
            client.getSoundHandler().playSound(new EngineSound(target));
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, EngineSoundMessage message, EntityPlayer player, MessageContext messageContext) {
        Lawnmower.NETWORK_WRAPPER.sendToAll(this);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuid.toString());
    }
}

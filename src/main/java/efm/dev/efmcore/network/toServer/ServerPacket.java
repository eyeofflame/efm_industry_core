package efm.dev.efmcore.network.toServer;

import efm.dev.efmcore.mixin.doubleJump.LivingEntityAccessor;
import efm.dev.efmcore.network.NetworkInstance;
import efm.dev.efmcore.network.toClient.ClientPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerPacket {
    private final UUID playerId;
    private final String operation;

    public ServerPacket(UUID playerId, String operation) {
        this.operation = operation;
        this.playerId = playerId;
    }

    public static ServerPacket decode(FriendlyByteBuf buf) {
        return new ServerPacket(buf.readUUID(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeUtf(this.operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null && this.operation.equals("jump") && player.getUUID().equals(this.playerId) &&
                        !player.onGround() &&
                        !player.isInLava() &&
                        !player.isInWater() &&
                        ((LivingEntityAccessor) player).getNoJumpDelay() == 0 &&
                        player.getPersistentData().getInt("efm:jump") <= 1
                ) {

                    NetworkInstance.CLIENT_INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new ClientPacket(this.playerId, this.operation));
                    player.jumpFromGround();
                    ((LivingEntityAccessor) player).setNoJumpDelay(10);
                    player.getPersistentData().putInt("efm:jump", player.getPersistentData().getInt("efm:jump") + 1);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

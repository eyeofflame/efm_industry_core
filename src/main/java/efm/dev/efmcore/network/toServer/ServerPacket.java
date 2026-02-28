package efm.dev.efmcore.network.toServer;

import efm.dev.efmcore.network.NetworkInstance;
import efm.dev.efmcore.network.toClient.ClientPacket;
import net.minecraft.network.FriendlyByteBuf;
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
                if (this.operation.equals("jump") && ctx.get().getSender().getUUID().equals(this.playerId) && !ctx.get().getSender().onGround() && !ctx.get().getSender().isInLava() && !ctx.get().getSender().isInWater()) {
                    NetworkInstance.CLIENT_INSTANCE.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new ClientPacket(this.playerId, this.operation));
                    ctx.get().getSender().jumpFromGround();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

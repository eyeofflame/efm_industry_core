package efm.dev.efmcore.network.toClient;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientPacket {
    private final UUID id;
    private final String operation;

    public ClientPacket(UUID id, String operation) {
        this.id = id;
        this.operation = operation;
    }

    public static ClientPacket decode(FriendlyByteBuf buf) {
        return new ClientPacket(buf.readUUID(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(id);
        buf.writeUtf(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                handleClient(this.id);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void handleClient(UUID id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            if (!mc.player.getUUID().equals(this.id)) {
                System.out.println(mc.player.getUUID() + " + " + this.id);
                return;
            }
            mc.player.jumpFromGround();
            mc.player.fallDistance = 0f;
            mc.player.level().addParticle(ParticleTypes.POOF, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0.1, 0);
        }
    }
}

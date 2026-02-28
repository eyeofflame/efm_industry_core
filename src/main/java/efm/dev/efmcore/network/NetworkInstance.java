package efm.dev.efmcore.network;

import efm.dev.efmcore.Efmcore;
import efm.dev.efmcore.common.untils.EfmHelper;
import efm.dev.efmcore.network.toClient.ClientPacket;
import efm.dev.efmcore.network.toServer.ServerPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkInstance {
    private static final String VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            EfmHelper.buildRes(Efmcore.MODID, "main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    public static final SimpleChannel CLIENT_INSTANCE = NetworkRegistry.newSimpleChannel(
            EfmHelper.buildRes(Efmcore.MODID, "client"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    public static void register() {
        int id = 0;

        INSTANCE.registerMessage(
                id++,
                ServerPacket.class,
                ServerPacket::encode,
                ServerPacket::decode,
                ServerPacket::handle
        );

        int cid = 0;

        CLIENT_INSTANCE.registerMessage(
                cid++,
                ClientPacket.class,
                ClientPacket::encode,
                ClientPacket::decode,
                ClientPacket::handle
        );
    }
}

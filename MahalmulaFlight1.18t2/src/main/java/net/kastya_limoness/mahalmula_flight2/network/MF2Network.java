package net.kastya_limoness.mahalmula_flight2.network;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MF2Network {
    public static final String NETWORK_VERSION = "0.1.0";
    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(MahalmulaFlightII.getLocation("network"), () -> NETWORK_VERSION, version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
    public static void init()
    {
        CHANNEL.registerMessage(0, UpKeyPressedMessage.class, UpKeyPressedMessage::encode, UpKeyPressedMessage::decode, UpKeyPressedMessage::handle);
        CHANNEL.registerMessage(1, DownKeyPressedMessage.class, DownKeyPressedMessage::encode, DownKeyPressedMessage::decode, DownKeyPressedMessage::handle);
        CHANNEL.registerMessage(2, FlyKeyReleaseMessage.class, FlyKeyReleaseMessage::encode, FlyKeyReleaseMessage::decode, FlyKeyReleaseMessage::handle);
        CHANNEL.registerMessage(3, TeleportKeyPressedMessage.class, TeleportKeyPressedMessage::encode, TeleportKeyPressedMessage::decode, TeleportKeyPressedMessage::handle);
        CHANNEL.registerMessage(4, InventoryKeyPressedMessage.class, InventoryKeyPressedMessage::encode, InventoryKeyPressedMessage::decode, InventoryKeyPressedMessage::handle);

    }
}

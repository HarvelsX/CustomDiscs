package me.Navoei.customdiscsplugin.event;

import me.Navoei.customdiscsplugin.CustomDiscs;
import me.Navoei.customdiscsplugin.PlayerManager;
import me.Navoei.customdiscsplugin.VoicePlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.nio.file.Files;
import java.nio.file.Path;

public class GoatHorn implements Listener {

    private final static CustomDiscs customDiscs = CustomDiscs.getInstance();
    private final static PlayerManager playerManager = PlayerManager.instance();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onUse(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.GOAT_HORN
                || event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Player player = event.getPlayer();
        if (player.hasCooldown(Material.GOAT_HORN) || !isCustom(item)) return;

        Path path = customDiscs.getDataFolder().toPath().resolve("musicdata").resolve(fileName(item));
        if (Files.notExists(path)) return;

        assert VoicePlugin.voicechatServerApi != null;
        playerManager.playLocationalAudio(VoicePlugin.voicechatServerApi, path, player.getLocation(), Component.empty(), () -> {});
    }

    private String fileName(final ItemStack itemStack) {
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        return persistentDataContainer.get(new NamespacedKey(customDiscs, "customdisc"), PersistentDataType.STRING);
    }

    private boolean isCustom(final ItemStack itemStack) {
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        return persistentDataContainer.has(new NamespacedKey(customDiscs, "customdisc"));
    }
}

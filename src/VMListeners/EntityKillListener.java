package VMListeners;

import VMMissions.MissionUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillListener implements Listener {

    public EntityKillListener() {

    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if(killer == null)
            return;
        MissionUpdater.onKill(killer, e.getEntity());
    }

}

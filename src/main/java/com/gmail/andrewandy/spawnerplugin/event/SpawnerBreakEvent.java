package com.gmail.andrewandy.spawnerplugin.event;

import com.gmail.andrewandy.spawnerplugin.spawner.Spawner;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SpawnerBreakEvent extends SpawnerEvent implements Cancellable {

    public SpawnerBreakEvent(Spawner spawner) {
        super(spawner);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private boolean cancel;

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }
}

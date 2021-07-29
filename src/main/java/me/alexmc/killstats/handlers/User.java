package me.alexmc.killstats.handlers;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {
    private final String name;
    private final UUID uuid;

    private int kills = 0;
    private int deaths = 0;

    public User(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public void addKill() {
        kills++;
    }
    public void addDeath() {
        deaths++;
    }

}

package dev.redfrogss.mcmyconsole.classes;

import org.bukkit.configuration.file.FileConfiguration;

public class User {
    private String username, password;
    private FileConfiguration config;

    public User (FileConfiguration config) {
        this.config = config;

        this.username = this.config.getString("username");
        this.password = this.config.getString("password");
    }

    public boolean checkUser(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }
}

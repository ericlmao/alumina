/*
 *  MIT License
 *
 * Copyright (C) 2024 Negative Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */


package games.negative.alumina.sql;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * The SQLCredentials class represents the credentials required to connect to an SQL database.
 * It contains the host, port, database name, username, and password.
 * @deprecated Will be moved into dedicated library.
 */
public record SQLCredentials(String host, int port, String database, String username, String password) {

    /**
     * Create SQLCredentials object from a ConfigurationSection.
     * The ConfigurationSection should contain the following keys: "host", "port", "database", "username", "password".
     *
     * @param section The ConfigurationSection containing the necessary credentials.
     * @return The SQLCredentials object created from the ConfigurationSection.
     */
    public static SQLCredentials fromConfigurationSection(@NotNull ConfigurationSection section) {
        return new SQLCredentials(
                section.getString("host"),
                section.getInt("port"),
                section.getString("database"),
                section.getString("username"),
                section.getString("password")
        );
    }

}

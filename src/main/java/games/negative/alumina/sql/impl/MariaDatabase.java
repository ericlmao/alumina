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


package games.negative.alumina.sql.impl;

import com.google.common.base.Preconditions;
import games.negative.alumina.sql.SQLCredentials;
import games.negative.alumina.sql.SQLDatabase;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents a MariaDB database.
 * @deprecated Will be moved into dedicated library.
 */
@Deprecated
public class MariaDatabase implements SQLDatabase {

    private final Connection connection;

    public MariaDatabase(@NotNull SQLCredentials credentials) throws ClassNotFoundException, SQLException {
        Preconditions.checkNotNull(credentials, "Credentials cannot be null");

        Class.forName("org.mariadb.jdbc.Driver");

        this.connection = DriverManager.getConnection("jdbc:mariadb://" + credentials.host() + ":" + credentials.port() + "/" + credentials.database(), credentials.username(), credentials.password());
    }

    @Override
    public @NotNull Connection connection() {
        return connection;
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }
}

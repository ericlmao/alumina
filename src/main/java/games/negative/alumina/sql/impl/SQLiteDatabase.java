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
import games.negative.alumina.sql.SQLDatabase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents a SQLite database.
 * @deprecated Will be moved into dedicated library.
 */
@Deprecated
public class SQLiteDatabase implements SQLDatabase {

    private final File file;
    private final Connection connection;

    public SQLiteDatabase(@NotNull File file) throws IOException, ClassNotFoundException, SQLException {
        Preconditions.checkNotNull(file, "File cannot be null");
        Preconditions.checkArgument(file.getName().endsWith(".db"), "File must be a .db file");

        if (!file.exists()) file.createNewFile();

        this.file = file;

        Class.forName("org.sqlite.JDBC");

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    @Override
    public @NotNull Connection connection() {
        return connection;
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }

    @NotNull
    public File getFile() {
        return file;
    }
}

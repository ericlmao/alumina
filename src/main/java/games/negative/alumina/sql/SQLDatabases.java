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

import games.negative.alumina.sql.impl.MariaDatabase;
import games.negative.alumina.sql.impl.MySQLDatabase;
import games.negative.alumina.sql.impl.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * The SQLDatabases class provides factory methods to create instances of SQLDatabase implementations.
 * It provides methods to create connections to various SQL databases such as SQLite, MySQL, and MariaDB.
 * @deprecated Will be moved into dedicated library.
 */
@Deprecated
public interface SQLDatabases {

    /**
     * Returns a new instance of SQLiteDatabase, which is an implementation of SQLDatabase.
     *
     * @param file the file representing the SQLite database
     * @return a new instance of SQLiteDatabase
     * @throws SQLException if a database access error occurs
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the SQLite JDBC driver class is not found
     */
    static SQLDatabase lite(@NotNull File file) throws SQLException, IOException, ClassNotFoundException {
        return new SQLiteDatabase(file);
    }

    /**
     * Creates a MySQL database connection using the provided credentials.
     *
     * @param credentials the credentials used to connect to the MySQL database
     * @return a MySQLDatabase instance representing the connection to the MySQL database
     * @throws SQLException if a database access error occurs or the query syntax is invalid
     * @throws ClassNotFoundException if the MySQL Driver class is not found
     */
    static SQLDatabase mysql(@NotNull SQLCredentials credentials) throws SQLException, ClassNotFoundException {
        return new MySQLDatabase(credentials);
    }

    /**
     * Creates a new MariaDatabase instance with the provided SQL credentials.
     *
     * @param credentials the SQL credentials used to establish the connection
     * @return a new MariaDatabase instance
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the MariaDB JDBC driver class cannot be found
     */
    static SQLDatabase maria(@NotNull SQLCredentials credentials) throws SQLException, ClassNotFoundException {
        return new MariaDatabase(credentials);
    }

}

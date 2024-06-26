/*
 * MIT License
 *
 * Copyright (c) 2024 Negative Games
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
 */

package games.negative.alumina.version;

import com.google.gson.JsonObject;
import games.negative.alumina.logger.Logs;
import games.negative.alumina.util.HTTPUtil;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@UtilityClass
public class VersionChecker {

    @Nullable
    @CheckReturnValue
    public VersionCheckResult check(@NotNull String url, @NotNull VersionData current) {
        try {
            JsonObject object = HTTPUtil.get(url);
            if (object == null) {
                Logs.severe("Could not check for updates.");
                return null;
            }

            VersionData latest = VersionData.fromJson(object);
            if (latest == null) {
                Logs.severe("Could not check for updates. [NULL]");
                return null;
            }

            boolean upToDate = current.build() >= latest.build();

            return new VersionCheckResult(upToDate, current.version(), latest.version(), current.build(), latest.build());
        } catch (IOException e) {
            Logs.severe("Could not check for updates. [IOEXCEPTION]");
            return null;
        }
    }

}

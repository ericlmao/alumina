/*
 *  MIT License
 *
 * Copyright (C) 2025 Negative Games
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


package games.negative.alumina.util;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IntList {

    /**
     * Converts a list of strings to a list of integers. Each string in the input list can either be
     * a single integer or a range of integers separated by a hyphen.
     *
     * @param input the list of strings to convert to integers
     * @return the list of integers converted from the input strings
     */
    public static List<Integer> getList(@NotNull List<String> input) {
        List<Integer> output = Lists.newArrayList();

        for (String slot : input) {
            if (slot.contains("-")) {
                String[] split = slot.split("-");
                int start = Integer.parseInt(split[0]);
                int end = Integer.parseInt(split[1]);

                int min = Math.min(start, end);
                int max = Math.max(start, end);

                for (int i = min; i <= max; i++) {
                    output.add(i);
                }
            } else {
                try {
                    output.add(Integer.parseInt(slot));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return output;
    }

}

/*
 * MIT License
 *
 * Copyright (c) 2025 Negative Games
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

package games.negative.alumina.vector;

import org.bukkit.util.NumberConversions;

/**
 * Represents a ConfigLib compatible vector with x, y, and z coordinates
 * while being very closely mapped to Bukkit's {@link org.bukkit.util.Vector} class,
 * along with some methods copied from it for parody.
 *
 * @param x x-coordinate of the vector
 * @param y y-coordinate of the vector
 * @param z z-coordinate of the vector
 */
public record Vector(double x, double y, double z) {

    /**
     * Creates a new vector with the given x, y, and z coordinates.
     * @param other The other vector to add.
     * @return A new vector with the sum of the coordinates.
     */
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Subtracts the given vector from this vector.
     * @param other The other vector to subtract.
     * @return A new vector with the difference of the coordinates.
     */
    public Vector subtract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Multiplies this vector by the given scalar.
     * @param scalar The scalar to multiply by.
     * @return A new vector with the product of the coordinates.
     */
    public Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /**
     * Calculates the dot product of this vector and the given vector.
     * @param other The other vector to calculate the dot product with.
     * @return The dot product of the two vectors.
     */
    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Calculates the cross-product of this vector and the given vector.
     * @return A new vector with the cross-product of the coordinates.
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes this vector to a unit vector.
     * @return A new vector with the same direction but a length of 1.
     */
    public Vector normalize() {
        double length = length();
        if (length == 0) return new Vector(0, 0, 0);

        return new Vector(x / length, y / length, z / length);
    }

    /**
     * Calculates the cross-product of this vector and the given vector.
     * @param other The other vector to cross with.
     * @return A new vector with the cross-product of the coordinates.
     */
    public Vector cross(Vector other) {
        return new Vector(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x
        );
    }

    /**
     * Rotate this vector around the X axis by the given angle.
     * @param angle The angle in radians to rotate the vector.
     * @return A new vector with the rotated coordinates.
     */
    public Vector rotateAroundX(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(
            x,
            y * cos - z * sin,
            y * sin + z * cos
        );
    }

    /**
     * Rotate this vector around the Y axis by the given angle.
     * @param angle The angle in radians to rotate the vector.
     * @return A new vector with the rotated coordinates.
     */
    public Vector rotateAroundY(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(
            x * cos + z * sin,
            y,
            -x * sin + z * cos
        );
    }

    /**
     * Rotate this vector around the Z axis by the given angle.
     * @param angle The angle in radians to rotate the vector.
     * @return A new vector with the rotated coordinates.
     */
    public Vector rotateAroundZ(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(
            x * cos - y * sin,
            x * sin + y * cos,
            z
        );
    }

    /**
     * Check if this vector is inside an axis-aligned bounding box (AABB) defined by the given min and max vectors.
     * @param min The minimum corner of the AABB.
     * @param max The maximum corner of the AABB.
     * @return True if this vector is inside the AABB, false otherwise.
     */
    public boolean isInAABB(Vector min, Vector max) {
        return (x >= min.x && x <= max.x) && (y >= min.y && y <= max.y) && (z >= min.z && z <= max.z);
    }

    /**
     * Check if this vector is inside a sphere with the given origin and radius.
     * @param origin The origin of the sphere.
     * @param radius The radius of the sphere.
     * @return True if this vector is inside the sphere, false otherwise.
     */
    public boolean isInSphere(Vector origin, double radius) {
        return NumberConversions.square(origin.x - this.x) + NumberConversions.square(origin.y - this.y) + NumberConversions.square(origin.z - this.z) <= NumberConversions.square(radius);
    }

    /**
     * Get a Bukkit vector from this vector.
     * @return A new Bukkit vector with the same coordinates.
     */
    public org.bukkit.util.Vector toBukkitVector() {
        return new org.bukkit.util.Vector(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector(double x1, double y1, double z1))) return false;

        return Math.abs(this.x - x1) < 1.0E-6 && Math.abs(this.y - y1) < 1.0E-6 && Math.abs(this.z - z1) < 1.0E-6;
    }
}

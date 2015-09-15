/*
 * Copyright (C) 2014 Leo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package tw.edu.sju.ee.eea.utils.io;

import java.io.IOException;

/**
 *
 * @author Leo
 */
public interface ValueOutput {

    /**
     * Writes to the output stream the eight low-order bits of the argument
     * <code>b</code>. The 24 high-order bits of <code>b</code> are ignored.
     *
     * @param b the byte to be written.
     * @throws IOException if an I/O error occurs.
     */
    void write(int b) throws IOException;

    /**
     * Writes to the output stream all the bytes in array <code>b</code>. If
     * <code>b</code> is <code>null</code>, a <code>NullPointerException</code>
     * is thrown. If <code>b.length</code> is zero, then no bytes are written.
     * Otherwise, the byte <code>b[0]</code> is written first, then
     * <code>b[1]</code>, and so on; the last byte written is
     * <code>b[b.length-1]</code>.
     *
     * @param b the data.
     * @throws IOException if an I/O error occurs.
     */
    void write(byte b[]) throws IOException;

    /**
     * Writes <code>len</code> bytes from array <code>b</code>, in order, to the
     * output stream. If <code>b</code> is <code>null</code>, a
     * <code>NullPointerException</code> is thrown. If <code>off</code> is
     * negative, or <code>len</code> is negative, or <code>off+len</code> is
     * greater than the length of the array <code>b</code>, then an
     * <code>IndexOutOfBoundsException</code> is thrown. If <code>len</code> is
     * zero, then no bytes are written. Otherwise, the byte <code>b[off]</code>
     * is written first, then <code>b[off+1]</code>, and so on; the last byte
     * written is <code>b[off+len-1]</code>.
     *
     * @param b the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs.
     */
    void write(byte b[], int off, int len) throws IOException;

    public void writeValue(double value) throws IOException;
}

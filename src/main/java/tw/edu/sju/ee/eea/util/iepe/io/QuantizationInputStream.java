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
package tw.edu.sju.ee.eea.util.iepe.io;

import java.io.FilterInputStream;
import java.io.IOException;

/**
 *
 * @author Leo
 */
public class QuantizationInputStream extends FilterInputStream {

    private int sampleSizeInBits;
    private double rate;

    public QuantizationInputStream(VoltageInputStream voltage, int sampleSizeInBits, double rate) {
        super(voltage);
        this.sampleSizeInBits = sampleSizeInBits;
        this.rate = rate;
    }

    public QuantizationInputStream(VoltageInputStream voltage, int sampleSizeInBits) {
        this(voltage, sampleSizeInBits, 1);
    }

    public byte[] readQuantization() throws IOException {
        byte[] buffer = new byte[sampleSizeInBits / 8];
        long max = 0;
        for (int i = 0; i < buffer.length; i++) {
            max = (max << 8) | 0xFF;
        }
        max >>= 1;
        long data = (long) (((VoltageInputStream) in).readValue() * rate * max);
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) (data >> (8 * i));
        }
        return buffer;
    }

}

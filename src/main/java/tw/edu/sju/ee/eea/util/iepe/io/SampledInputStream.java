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
import java.io.InputStream;

/**
 *
 * @author Leo
 */
public class SampledInputStream extends FilterInputStream {

    private int sample;
    private boolean plus_minus;

    public SampledInputStream(VoltageInputStream iepe, int sample) {
        super(iepe);
        this.sample = sample;
    }

    @Override
    public int available() throws IOException {
        return super.available() / sample;
    }

    public double readSampled() throws IOException {
        while (available() < 1) {
            Thread.yield();
        }
        double tmp = 0;
        if (sample == 1) {
            tmp = ((VoltageInputStream) in).readValue();
        } else if (plus_minus = !plus_minus) {
            for (int i = 0; i < sample; i++) {
                tmp = Math.max(tmp, ((VoltageInputStream) in).readValue());
            }
        } else {
            for (int i = 0; i < sample; i++) {
                tmp = Math.min(tmp, ((VoltageInputStream) in).readValue());
            }
        }
        return tmp;
    }

}

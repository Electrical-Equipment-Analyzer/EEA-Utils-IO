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

import java.io.FilterOutputStream;
import java.io.IOException;

/**
 *
 * @author Leo
 */
public class SampledOutputStream {

    private ValueOutput out;
    private int sample;
    private boolean plus_minus;

    public SampledOutputStream(ValueOutput out, int sample) {
//        super(iepe);
        this.out = out;
        this.sample = sample;
    }

    int i = 0;
    double tmp = 0;

    public void writeSampled(double value) throws IOException {
        if (sample == 1) {
            out.writeValue(value);
            return;
        }
        if (i++ < sample) {
            tmp = plus_minus ? Math.max(tmp, value) : Math.min(tmp, value);
        } else {
            out.writeValue(tmp);
            i = 0;
            tmp = 0;
            plus_minus = !plus_minus;
        }
    }

}

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
package tw.edu.sju.ee.eea.utils.io.function;

import java.io.IOException;
import java.io.InputStream;
import tw.edu.sju.ee.eea.utils.io.ValueInput;

/**
 *
 * @author Leo
 */
public class RootMeanSquareInputStream extends InputStream implements ValueInput{

    private ValueInput in;
    private int length;

    public RootMeanSquareInputStream(ValueInput in, int length) {
        this.in = in;
        this.length = length;
    }

    public double readValue() throws IOException {
        double xt[] = new double[length];
        for (int i = 0; i < xt.length; i++) {
            xt[i] = in.readValue();
        }
        return rms(xt);
    }

    public static double rms(double[] nums) {
        double rms = 0;
        for (int i = 0; i < nums.length; i++) {
            rms += nums[i] * nums[i];
        }
        rms /= nums.length;
        return Math.sqrt(rms);
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

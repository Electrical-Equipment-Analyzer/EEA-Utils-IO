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
import java.util.Arrays;
import tw.edu.sju.ee.eea.utils.io.ValueInput;

/**
 *
 * @author Leo
 */
public class RevolutionsInputStream extends InputStream implements ValueInput {

    private ValueInput in;
    private int length;
    boolean level;
    private int count = 0;

    public RevolutionsInputStream(ValueInput in, int length) {
        this.in = in;
        this.length = length;
    }

    public double readValue() throws IOException {
        for (int i = 0; i < 100; i++) {

            double read = in.readValue();
//            count++;
            if (!level && read > 3) {
                level = true;
                i += count * 100;
                count = 0;
                return i;
            } else if (level && read < 3) {
                level = false;
            }
        }
        count++;
        return Double.NaN;
//        while (in.available() > 0) {
//            double read = in.readValue();
//            count++;
//            if (!level && read > 3) {
//                level = true;
//                int tmp = count;
//                return tmp;
//            } else if (level && read < 3) {
//                level = false;
//            }
//        }
//
//        return Double.NaN;
//        double xt[] = new double[length];
//        for (int i = 0; i < xt.length; i++) {
//            xt[i] = in.readValue();
//        }
//        return trig(xt);
    }

    private static double trig(double[] nums) {
        double edge = 0;
        boolean level = true;
        for (int i = 0; i < nums.length; i++) {
            if (!level && nums[i] > 3) {
                edge++;
                level = true;
            } else if (level && nums[i] < 3) {
                level = false;
            }
        }
        return edge;
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

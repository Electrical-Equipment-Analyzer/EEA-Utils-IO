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

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.jfree.data.xy.XYDataItem;
import tw.edu.sju.ee.eea.utils.io.ValueInput;

/**
 *
 * @author Leo
 */
public class RevolutionsInputStream extends InputStream implements PlotInput {
    
    private ValueInput in;
    private int samplerate;
    private double timebase;

    private double length;
//    private int interval;
    
    boolean level;
    private int x = 0;
    private int y;
    
    public RevolutionsInputStream(ValueInput in,  int samplerate, double timebase) {
        this.in = in;
        this.samplerate = samplerate;
        this.timebase = timebase;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setInterval(int interval) {
//        this.interval = interval;
    }
    
    public XYDataItem readPlot() throws IOException {
        int length = (int) (this.samplerate * this.length);
        while (x++ < length) {
            y++;
            double read = in.readValue();
            if (!level && read > 3) {
                level = true;
                int re = y;
                y = 0;
                return new XYDataItem(x*timebase/samplerate, 60*samplerate/re);
            } else if (level && read < 3) {
                level = false;
            }
        }
        x = 0;
        return null;
    }
    
    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

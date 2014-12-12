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
package tw.edu.sju.ee.eea.utils.io.tools;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tw.edu.sju.ee.eea.utils.io.ValueInputStream;
import tw.edu.sju.ee.eea.utils.io.ValueOutput;
import tw.edu.sju.ee.eea.utils.io.ValueOutputStream;

/**
 *
 * @author Leo
 */
public class IOChannel extends Thread {

    private ValueOutputStream pipeIn;
    private ValueInputStream pipeOut;

    private ArrayList<ValueOutput> stream = new ArrayList<ValueOutput>();

    public IOChannel() throws IOException {
        PipedInputStream pipe = new PipedInputStream(512000);
        pipeOut = new ValueInputStream(pipe);
        pipeIn = new ValueOutputStream(new PipedOutputStream(pipe));
        this.start();
    }

    public void writeVoltageArray(double[] data) throws IOException {
        for (double d : data) {
            pipeIn.writeDouble(d);
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                while (pipeOut.available() < 1) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(IOChannel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                double buffer = pipeOut.readValue();
                for (int i = 0; i < stream.size(); i++) {
                    stream.get(i).writeValue(buffer);
                }
            } catch (IOException ex) {
                Logger.getLogger(IOChannel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized ValueOutput addStream(ValueOutput stream) {
        this.stream.add(stream);
        return stream;
    }
    
    public synchronized void removeStream(ValueOutput stream) {
        if (stream == null) {
            return;
        }
        if (this.stream == null) {
            return;
        }
        this.stream.remove(stream);
    }

    public static class IepePipeStream extends ValueInputStream implements ValueOutput {

        private DataOutputStream pipe;

        public IepePipeStream() throws IOException {
            super(new PipedInputStream(1024000));
            pipe = new DataOutputStream(new PipedOutputStream((PipedInputStream) super.in));
        }

        public void writeValue(double value) throws IOException {
                pipe.writeDouble(value);
        }

    }

}

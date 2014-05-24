/*
 * Copyright (C) 2014, St. John's University and/or its affiliates. All rights reserved.
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
package tw.edu.sju.ee.eea.util.iepe;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tw.edu.sju.ee.eea.util.iepe.io.IepeInputStream;

/**
 * This class is an Utility to get data from IEPE device.
 *
 * @author Leo
 * @since 1.0
 */
public class IEPEInput implements Runnable {

    private IEPEDevice device;
    private int length;
    private ArrayList<Stream>[] stream;

    /**
     * Creates a IEPE Utility where the IEPE device and the port number are
     * specified value.
     *
     * @param device The IEPE device
     * @param channel read channels number
     * @param length samples per channel in once read.
     */
    public IEPEInput(IEPEDevice device, int[] channel, int length) {
        this.device = device;
        this.length = length;
        stream = new ArrayList[8];
    }

    /**
     * Creates a IEPE Utility where the IEPE device and the port number are
     * specified value.
     *
     * @param device The IEPE device
     * @param channel read channels number
     */
    public IEPEInput(IEPEDevice device, int[] channel) {
        this(device, channel, 128);
    }

    public Stream addStream(int channel, Stream stream) {
        if (channel > this.stream.length) {
            System.out.println("OutOfLength");
            return null;
        }
        if (this.stream[channel] == null) {
            this.stream[channel] = new ArrayList<Stream>();
        }
        this.stream[channel].add(stream);
        return stream;
    }

    public Stream addStream(int channel, OutputStream stream) {
        if (channel > this.stream.length) {
            System.out.println("OutOfLength");
            return null;
        }
        if (this.stream[channel] == null) {
            this.stream[channel] = new ArrayList<Stream>();
        }
        Stream s = new IepeOutputStream(stream);
        this.stream[channel].add(s);
        return s;
    }

    public void removeStream(int channel, Stream stream) {
        if (stream == null) {
            return;
        }
        if (channel > this.stream.length) {
            System.out.println("OutOfLength");
            return;
        }
        if (this.stream[channel] == null) {
            return;
        }
        this.stream[channel].remove(stream);
    }

    public synchronized Stream replaceStream(int channel, Stream regex, Stream replacement) throws IOException {
        removeStream(channel, regex);
        regex.close();
        return addStream(channel, replacement);
    }

    /**
     * The Utility thread.
     *
     */
    @Override
    public void run() {
        try {
            device.openDevice();
            device.configure();
            device.start();
            try {
                double[][] read;
                while (!Thread.interrupted()) {
                    read = device.read(length);
                    synchronized (this) {
                        for (int i = 0; i < this.stream.length; i++) {
                            if (stream[i] == null) {
                                continue;
                            }
                            for (int j = 0; j < stream[i].size(); j++) {
                                stream[i].get(j).write(read[i]);
                            }
                        }
                    }
                }
            } catch (IEPEException ex) {
                Logger.getLogger(IEPEInput.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedIOException ex) {
            } catch (IOException ex) {
                Logger.getLogger(IEPEInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IEPEException ex) {
            Logger.getLogger(IEPEInput.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                device.stop();
                device.closeDevice();
            } catch (IEPEException ex) {
                Logger.getLogger(IEPEInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public interface Stream extends Closeable, Flushable {

        void write(double[] data) throws IOException;
    }

    public static class IepeStream extends IepeInputStream implements Stream {

        private DataOutputStream pipe;

        public IepeStream() throws IOException {
            super(new PipedInputStream(1024000));
            pipe = new DataOutputStream(new PipedOutputStream((PipedInputStream) super.in));
        }

        public void write(double[] data) throws IOException {
            for (double d : data) {
                pipe.writeDouble(d);
            }
        }

        public void flush() throws IOException {
            pipe.flush();
        }

    }

    public static class IepeOutputStream extends DataOutputStream implements Stream {

        public IepeOutputStream(OutputStream out) {
            super(out);
        }

        public void write(double[] data) throws IOException {
            for (double d : data) {
                writeDouble(d);
            }
        }
    }

}

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class IEPEInput extends Thread {

    private IEPEDevice device;
//    private IepeStream[] iepeStreams;
    private int length;
//    private int channel[];
    private ArrayList<IepeStream>[] stream;

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
//        this.channel = channel;
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

    public void addStream(int channel, IepeStream stream) {
        if (channel > this.stream.length) {
            System.out.println("OutOfLength");
            return;
        }
        if (this.stream[channel] == null) {
            this.stream[channel] = new ArrayList<IepeStream>();
        }
        this.stream[channel].add(stream);
    }

//    public IepeInputStream getIepeStreams(int channel) {
//        return iepeStreams[channel].iepe;
//    }
//
//    /**
//     * Start the Utility to acquire data.
//     *
//     * @throws IEPEException if an IEPE error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    public void startIEPE() throws IEPEException, IOException {
//        device.openDevice();
//        device.configure();
//        device.start();
//        iepeStreams = new IepeStream[this.channel.length];
//        for (int i = 0; i < this.channel.length; i++) {
//            iepeStreams[i] = new IepeStream();
//        }
//        super.start();
//    }
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
                    for (int i = 0; i < this.stream.length; i++) {
                        if (stream[i] == null) {
                            continue;
                        }
                        for (int j = 0; j < stream[i].size(); j++) {
                            stream[i].get(j).write(read[i]);
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

    private class IepeStream extends IepeInputStream {

        private DataOutputStream pipe;

        public IepeStream() throws IOException {
            super(new PipedInputStream(1024000));
            pipe = new DataOutputStream(new PipedOutputStream((PipedInputStream) this.in));
        }

        private void write(double[] data) throws IOException {
            for (double d : data) {
                pipe.writeDouble(d);
            }
        }

    }

}

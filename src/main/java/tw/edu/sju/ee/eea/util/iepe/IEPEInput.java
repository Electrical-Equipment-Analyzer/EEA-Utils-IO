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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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
    private IepeStream[] iepeStreams;
    private int length = 128;
    private int channel[];

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
        this.channel = channel;
    }

    /**
     * Creates a IEPE Utility where the IEPE device and the port number are
     * specified value.
     *
     * @param device The IEPE device
     * @param channel read channels number
     */
    public IEPEInput(IEPEDevice device, int[] channel) {
        this.device = device;
        this.channel = channel;
    }

    /**
     * Gets the InputStream.
     *
     * @param channel the channel number
     * @return the InputStream of channel.
     */
    public IepeInputStream getIepeStreams(int channel) {
        return iepeStreams[channel].iepe;
    }

    /**
     * Start the Utility to acquire data.
     *
     * @throws IEPEException if an IEPE error occurs
     * @throws IOException if an I/O error occurs
     */
    public void startIEPE() throws IEPEException, IOException {
        device.openDevice();
        device.configure();
        device.start();
        iepeStreams = new IepeStream[this.channel.length];
        for (int i = 0; i < this.channel.length; i++) {
            iepeStreams[i] = new IepeStream();
        }
        super.start();
    }

    /**
     * Stop the Utility to acquire data.
     *
     * @throws IEPEException if an IEPE error occurs
     */
    public void stopIEPE() throws IEPEException {
        super.stop();
        device.stop();
        device.closeDevice();
    }

    /**
     * The Utility thread.
     *
     */
    @Override
    public void run() {
        try {
            double[][] read;
            while (true) {
                read = device.read(length);
                for (int i = 0; i < this.channel.length; i++) {
                    iepeStreams[i].write(read[this.channel[i]]);
                }
            }
        } catch (IEPEException ex) {
            Logger.getLogger(IEPEInput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedIOException ex) {
        } catch (IOException ex) {
            Logger.getLogger(IEPEInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class IepeStream {

        private DataOutputStream pipe;
        private IepeInputStream iepe;

        public IepeStream() throws IOException {
            PipedInputStream in = new PipedInputStream(1024000);
            iepe = new IepeInputStream(in);
            pipe = new DataOutputStream(new PipedOutputStream(in));
        }

        void write(double[] data) throws IOException {
            for (double d : data) {
                pipe.writeDouble(d);
            }
        }

    }

}

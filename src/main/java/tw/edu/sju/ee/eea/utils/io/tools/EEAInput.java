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
package tw.edu.sju.ee.eea.utils.io.tools;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 * This class is an Utility to get data from IEPE device.
 *
 * @author Leo
 * @since 1.0
 */
public class EEAInput implements Runnable {

    private EEADevice device;
    private int length;
    private InputChannel[] channels;

    /**
     * Creates a IEPE Utility where the IEPE device and the port number are
     * specified value.
     *
     * @param device The IEPE device
     * @param channel read channels number
     * @param length samples per channel in once read.
     */
    public EEAInput(EEADevice device, int[] channel, int length) {
        this.device = device;
        this.length = length;
        try {
            channels = new InputChannel[this.device.getChannelLength()];
        } catch (EEAException ex) {
            Logger.getLogger(EEAInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < channels.length; i++) {
            try {
                channels[i] = new InputChannel();
            } catch (IOException ex) {
                Logger.getLogger(EEAInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Creates a IEPE Utility where the IEPE device and the port number are
     * specified value.
     *
     * @param device The IEPE device
     * @param channel read channels number
     */
    public EEAInput(EEADevice device, int[] channel) {
        this(device, channel, 16);
    }

    public EEAInput(EEADevice device) {
        this(device, null, 16);
    }

    public InputChannel[] getIOChannel() {
        return channels;
    }

    public InputChannel getIOChannel(int i) {
        return channels[i];
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
                    for (int i = 0; i < this.channels.length; i++) {
                        this.channels[i].writeVoltageArray(read[i]);
                    }
                }
            } catch (EEAException ex) {
                Logger.getLogger(EEAInput.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedIOException ex) {
            } catch (IOException ex) {
                Logger.getLogger(EEAInput.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
            }
        } catch (EEAException ex) {
            Logger.getLogger(EEAInput.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                device.stop();
                device.closeDevice();
            } catch (EEAException ex) {
                Logger.getLogger(EEAInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

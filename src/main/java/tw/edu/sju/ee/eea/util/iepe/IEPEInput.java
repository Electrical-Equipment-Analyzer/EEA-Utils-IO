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
package tw.edu.sju.ee.eea.util.iepe;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leo
 */
public class IEPEInput extends Thread {

    private IEPEDevice device;
    private IEPEInputStream[] iepeStreams;
    private int length = 128;
    private int channel[];

    public IEPEInput(IEPEDevice device, int[] channel, int length) {
        this.device = device;
        this.length = length;
        this.channel = channel;
    }

    public IEPEInput(IEPEDevice device, int[] channel) {
        this.device = device;
        this.channel = channel;
    }

    public IEPEInputStream getIepeStreams(int channel) {
        return iepeStreams[channel];
    }

    public void startIEPE() throws IEPEException, IOException {
        device.openDevice();
        device.configure();
        device.start();
        iepeStreams = new IEPEInputStream[this.channel.length];
        for (int i = 0; i < this.channel.length; i++) {
            iepeStreams[i] = new IEPEInputStream();
        }
        super.start();
    }

    public void stopIEPE() throws IEPEException {
        super.stop();
        device.stop();
        device.closeDevice();
    }

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

}

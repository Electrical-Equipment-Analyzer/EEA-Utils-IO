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

/**
 * A {@code IEPEDevice} is a IEPE sensor input device.
 * This is the basic mathod to control device.
 *
 * @author Leo
 * @see IEPEInput
 * @since 1.0
 */
public interface IEPEDevice {

    /**
     * Open this device to connected.
     * If the device is already opened then invoking this
     * method has no effect.
     *
     * @throws IEPEException if an error occurs
     */
    public void openDevice() throws IEPEException;

    /**
     * Close this device to releases resources associated with it.
     * If the device is already opened then invoking this
     * method has no effect.
     *
     * @throws IEPEException if an error occurs
     */
    public void closeDevice() throws IEPEException;

    /**
     * Gets the <code>DeviceID</code>.
     *
     * @return the DeviceID.
     * @throws IEPEException if an error occurs
     */
    public int getDeviceId() throws IEPEException;

    /**
     * Configure the setting to device.
     *
     * @throws IEPEException if an error occurs
     */
    public void configure() throws IEPEException;

    /**
     * Read data from device.
     *
     * @param length the sample per channel.
     * @return Channels data.
     * @throws IEPEException if an error occurs
     */
    public double[][] read(int length) throws IEPEException;

    /**
     * Start the device to acquire data.
     * 
     * @throws IEPEException if an error occurs
     */
    public void start() throws IEPEException;

    /**
     * Stop the device to acquire data.
     * 
     * @throws IEPEException if an error occurs
     */
    public void stop() throws IEPEException;

}

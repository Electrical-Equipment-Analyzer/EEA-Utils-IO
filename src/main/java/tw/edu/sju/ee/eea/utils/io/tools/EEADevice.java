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

/**
 * A {@code IEPEDevice} is a IEPE sensor input device. This is the basic mathod
 * to control device.
 *
 * @author Leo
 * @see IEPEInput
 * @since 1.0
 */
public interface EEADevice {

    public String getDeviceName();

    public String getDeviceModel();

    public String getSerialNumber();

    /**
     * Open this device to connected. If the device is already opened then
     * invoking this method has no effect.
     *
     * @throws EEAException if an error occurs
     */
    public void openDevice() throws EEAException;

    /**
     * Close this device to releases resources associated with it. If the device
     * is already opened then invoking this method has no effect.
     *
     * @throws EEAException if an error occurs
     */
    public void closeDevice() throws EEAException;

    /**
     * Gets the <code>DeviceID</code>.
     *
     * @return the DeviceID.
     * @throws EEAException if an error occurs
     */
    public int getDeviceId() throws EEAException;

    /**
     * Gets the <code>ChannelLength</code>.
     *
     * @return the ChannelLength.
     * @throws EEAException if an error occurs
     */
    public int getChannelLength() throws EEAException;

    /**
     * Configure the setting to device.
     *
     * @throws EEAException if an error occurs
     */
    public void configure() throws EEAException;

    /**
     * Read data from device.
     *
     * @param length the sample per channel.
     * @return Channels data.
     * @throws EEAException if an error occurs
     */
    public double[][] read(int length) throws EEAException;

    /**
     * Start the device to acquire data.
     *
     * @throws EEAException if an error occurs
     */
    public void start() throws EEAException;

    /**
     * Stop the device to acquire data.
     *
     * @throws EEAException if an error occurs
     */
    public void stop() throws EEAException;

}

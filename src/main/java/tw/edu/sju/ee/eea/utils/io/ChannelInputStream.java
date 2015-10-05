/*
 * Copyright (C) 2015 D10307009
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
package tw.edu.sju.ee.eea.utils.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.openide.util.Exceptions;

/**
 *
 * @author D10307009
 */
public class ChannelInputStream extends ValueInputStream implements ValueOutput {

    private ValueOutputStream pipe;
    private int skip;
    private int size;

    public ChannelInputStream(int size, int skip) throws IOException {
        super(new PipedInputStream(size * Double.BYTES));
        this.size = size / 2;
        pipe = new ValueOutputStream(new PipedOutputStream((PipedInputStream) super.in));
        this.skip = skip;
    }

    public ChannelInputStream() throws IOException {
        this(1024000, 0);
    }

    boolean write;

    @Override
    public void writeValue(double value) throws IOException {
//        System.out.println(this.available());
//        System.out.println(size);
        if (this.available() > size) {
//            System.out.println("full");
            write = false;
        } else if (this.available() < (size - 1200)) {
//            System.out.println("unf");
            write = true;
        }
        if (write) {
//            System.out.println("wr");
            pipe.writeDouble(value);
        }
//        System.out.println(this.available() < size);
//        if (value == Double.MAX_VALUE) {
//            System.out.println("MAX");
//            write = this.available() < size;
//            return;
//        }
//        if (write) {
//            pipe.writeDouble(value);
//        }
//            if (skip != 0) {
//                skip();
//            }
    }

//        private void skip() {
//            try {
//                while (this.available() > skip*2) {
//                    this.skip(skip);
//                }
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
    public void write(int b) throws IOException {
        pipe.write(b);
    }

    public void write(byte[] b) throws IOException {
        if (this.available() > size) {
            System.out.println("A");
            throw new OutOfBufferException();
        }
        System.out.println("   B");
        pipe.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        pipe.write(b, off, len);
    }
}

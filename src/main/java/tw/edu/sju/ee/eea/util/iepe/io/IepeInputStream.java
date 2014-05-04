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
package tw.edu.sju.ee.eea.util.iepe.io;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Leo
 */
public class IepeInputStream extends FilterInputStream {

    public IepeInputStream(InputStream in) {
        super(new DataInputStream(in));
    }

    public double readValue() throws IOException {
        return ((DataInputStream) in).readDouble();
    }

    @Override
    public int available() throws IOException {
        return super.available() / 8;
    }

    @Override
    public long skip(long n) throws IOException {
        return super.skip(n * 8);
    }

}

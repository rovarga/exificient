/*
 * Copyright (C) 2007, 2008 Siemens AG
 *
 * This program and its interfaces are free software;
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.siemens.ct.exi.io.channel;

import java.io.IOException;
import java.io.InputStream;

import com.siemens.ct.exi.io.BitInputStream;

/**
 * Simple datatype decoder based on an underlying <code>BitInputStream</code>.
 * Reading a single bit from the underlying stream involves several VM
 * operations. Thus, whenever possible, whole bytes should be read instead.
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.2.20081111
 */

public class BitDecoderChannel extends AbstractDecoderChannel implements
		DecoderChannel {
	/**
	 * Underlying bit input stream from which bits and bytes are read.
	 */
	protected BitInputStream istream;

	/**
	 * Construct a decoder from input stream
	 */
	public BitDecoderChannel(InputStream is) {
		this.istream = new BitInputStream(is);
	}

	public void align() throws IOException {
		istream.align();
	}

	public int decode() throws IOException {
		return istream.readBits(8);
	}

	/**
	 * Decodes and returns an n-bit unsigned integer.
	 */
	public int decodeNBitUnsignedInteger(int n) throws IOException {
		assert (n >= 0);

		if (n == 0) {
			return 0;
		} else {
			return istream.readBits(n);
		}
	}

	/**
	 * Decode a single boolean value. The value false is represented by the bit
	 * 0, and the value true is represented by the bit 1.
	 */
	public boolean decodeBoolean() throws IOException {
		return (istream.readBit() == 1);
	}

	/**
	 * Decode a binary value as a length-prefixed sequence of octets.
	 */
	public byte[] decodeBinary() throws IOException {
		final int length = decodeUnsignedInteger();
		byte[] result = new byte[length];

		for (int i = 0; i < length; i++) {
			result[i] = (byte) istream.readBits(8);
		}
		return result;
	}

}
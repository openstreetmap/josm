/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.compress.compressors.zstandard;


import java.io.IOException;
import java.io.OutputStream;

import com.github.luben.zstd.ZstdOutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

/**
 * {@link CompressorOutputStream} implementation to create Zstandard encoded stream.
 * Library relies on <a href="https://github.com/luben/zstd-jni/">Zstandard JNI</a>
 *
 * @since 1.16
 */
public class ZstdCompressorOutputStream extends CompressorOutputStream {

    private final com.github.luben.zstd.ZstdOutputStream encOS;

    public ZstdCompressorOutputStream(final OutputStream out) throws IOException {
        this.encOS = new ZstdOutputStream(out);
    }

    @Override
    public void close() throws IOException {
        encOS.close();
    }

    @Override
    public void write(final int b) throws IOException {
        encOS.write(b);
    }

    @Override
    public void write(final byte[] buf, final int off, final int len) throws IOException {
        encOS.write(buf, off, len);
    }

    @Override
    public String toString() {
        return encOS.toString();
    }

    @Override
    public void flush() throws IOException {
        encOS.flush();
    }
}

/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.hc.core5.http.nio.entity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.util.Args;

/**
 * @since 5.0
 */
public abstract class AbstractCharAsyncEntityConsumer<T> extends AbstractCharDataConsumer implements AsyncEntityConsumer<T> {

    private volatile FutureCallback<T> resultCallback;
    private volatile T content;

    protected abstract void streamStart(ContentType contentType) throws HttpException, IOException;

    protected abstract T generateContent() throws IOException;

    @Override
    public final void streamStart(
            final EntityDetails entityDetails,
            final FutureCallback<T> resultCallback) throws IOException, HttpException {
        Args.notNull(resultCallback, "Result callback");
        this.resultCallback = resultCallback;
        try {
            final ContentType contentType = entityDetails != null ? ContentType.parse(entityDetails.getContentType()) : null;
            Charset charset = contentType != null ? contentType.getCharset() : null;
            if (charset == null) {
                charset = StandardCharsets.US_ASCII;
            }
            setCharset(charset);
            streamStart(contentType);
        } catch (final UnsupportedCharsetException ex) {
            throw new UnsupportedEncodingException(ex.getMessage());
        }
    }

    @Override
    protected final void completed() throws IOException {
        content = generateContent();
        if (resultCallback != null) {
            resultCallback.completed(content);
        }
        releaseResources();
    }

    @Override
    public final void failed(final Exception cause) {
        if (resultCallback != null) {
            resultCallback.failed(cause);
        }
        releaseResources();
    }

    @Override
    public final T getContent() {
        return content;
    }

}

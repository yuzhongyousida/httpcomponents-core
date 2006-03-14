/*
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 *  Copyright 1999-2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http;

import java.util.Iterator;

import org.apache.http.params.HttpParams;

/**
 * A generic HTTP message.
 * Holds what is common between requests and responses.
 *
 * @author <a href="mailto:oleg at ural.ru">Oleg Kalnichevski</a>
 *
 * @version $Revision$
 * 
 * @since 4.0
 */
public interface HttpMessage {
    
    public HttpVersion getHttpVersion();

    boolean containsHeader(String name);
    
    Header[] getHeaders(String name);

    Header getFirstHeader(String name);

    Header getLastHeader(String name);

    Header[] getAllHeaders();

    void addHeader(Header header);

    void setHeader(Header header);

    void removeHeader(Header header);
    
    void removeHeaders(String name);
    
    Iterator headerIterator();
    
    HttpParams getParams();
    
    void setParams(HttpParams params);
        
}

package org.jocean.jolokia;

import org.jocean.http.Interact;
import org.jocean.jolokia.spi.ExecResponse;
import org.jocean.jolokia.spi.JolokiaRequest;
import org.jocean.jolokia.spi.ListResponse;
import org.jocean.jolokia.spi.ReadAttrResponse;

import rx.Observable;
import rx.functions.Func1;

public interface JolokiaAPI {
    public Func1<Interact, Observable<ListResponse>> list(final String uri);
    
    public Func1<Interact, Observable<ReadAttrResponse>> readAttribute(final String uri, final String mbean);
    
    public Func1<Interact, Observable<ExecResponse>> exec(final String uri, final JolokiaRequest req);
}

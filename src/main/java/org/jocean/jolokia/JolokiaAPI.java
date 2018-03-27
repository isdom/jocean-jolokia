package org.jocean.jolokia;

import org.jocean.http.Interact;
import org.jocean.jolokia.spi.ExecResponse;
import org.jocean.jolokia.spi.JolokiaRequest;

import rx.Observable;
import rx.functions.Func1;

public interface JolokiaAPI {
    public Func1<Interact, Observable<ExecResponse>> exec(final JolokiaRequest req);
}

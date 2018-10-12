package org.jocean.jolokia;

import org.jocean.http.RpcRunner;
import org.jocean.jolokia.spi.ExecResponse;
import org.jocean.jolokia.spi.JolokiaRequest;
import org.jocean.jolokia.spi.JolokiaResponse;
import org.jocean.jolokia.spi.ListResponse;
import org.jocean.jolokia.spi.ReadAttrResponse;

import rx.Observable.Transformer;

public interface JolokiaAPI {
    public Transformer<RpcRunner, ListResponse> list(final String uri);

    public Transformer<RpcRunner, ReadAttrResponse> readAttribute(final String uri, final String objectName);

    public Transformer<RpcRunner, ExecResponse> exec(final String uri, final JolokiaRequest req);

    public <T extends JolokiaResponse> Transformer<RpcRunner, T[]> batch(final String uri,
            final JolokiaRequest[] reqs, final Class<T[]> cls);
}

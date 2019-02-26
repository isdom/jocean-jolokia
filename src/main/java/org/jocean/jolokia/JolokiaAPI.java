package org.jocean.jolokia;

import org.jocean.http.RpcRunner;
import org.jocean.jolokia.api.ExecResponse;
import org.jocean.jolokia.api.JolokiaRequest;
import org.jocean.jolokia.api.JolokiaResponse;
import org.jocean.jolokia.api.ListResponse;
import org.jocean.jolokia.api.ReadAttrResponse;

import rx.Observable.Transformer;

public interface JolokiaAPI {
    public Transformer<RpcRunner, ListResponse> list(final String uri);

    public Transformer<RpcRunner, ReadAttrResponse> readAttribute(final String uri, final String objectName);

    public Transformer<RpcRunner, ExecResponse> exec(final String uri, final JolokiaRequest req);

    public <T extends JolokiaResponse> Transformer<RpcRunner, T[]> batch(final String uri,
            final JolokiaRequest[] reqs, final Class<T[]> cls);
}

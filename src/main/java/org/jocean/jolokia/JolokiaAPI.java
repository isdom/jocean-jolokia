package org.jocean.jolokia;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jocean.http.RpcRunner;
import org.jocean.jolokia.api.ExecResponse;
import org.jocean.jolokia.api.JolokiaRequest;
import org.jocean.jolokia.api.JolokiaResponse;
import org.jocean.jolokia.api.ListResponse;
import org.jocean.jolokia.api.ReadAttrResponse;
import org.jocean.rpc.annotation.RpcBuilder;

import rx.Observable;
import rx.Observable.Transformer;

public interface JolokiaAPI {

    @RpcBuilder
    interface ListBuilder {

        @Path("uri")
        ListBuilder uri(final String uri);

        @POST
        @Path("${uri}")
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<ListResponse> call();
    }

    public Transformer<RpcRunner, ListResponse> list(final String uri);

    public Transformer<RpcRunner, ReadAttrResponse> readAttribute(final String uri, final String objectName);

    public Transformer<RpcRunner, ExecResponse> exec(final String uri, final JolokiaRequest req);

    public <T extends JolokiaResponse> Transformer<RpcRunner, T[]> batch(final String uri,
            final JolokiaRequest[] reqs, final Class<T[]> cls);
}

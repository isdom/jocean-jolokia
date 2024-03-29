package org.jocean.jolokia;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.jocean.http.RpcRunner;
import org.jocean.jolokia.api.ExecResponse;
import org.jocean.jolokia.api.JolokiaRequest;
import org.jocean.jolokia.api.JolokiaResponse;
import org.jocean.jolokia.api.ListResponse;
import org.jocean.jolokia.api.ReadAttrResponse;
import org.jocean.rpc.annotation.RpcBuilder;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.Observable.Transformer;

public interface JolokiaAPI {

    @RpcBuilder
    interface ListBuilder {

        @JSONField(name="type", defaultValue="list")
        ListBuilder type(final String type);

        @PathParam("uri")
        ListBuilder uri(final String uri);

        @POST
        @Path("{uri}")
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<ListResponse> call();
    }

    public Transformer<RpcRunner, ListResponse> list(final String uri);

    @RpcBuilder
    interface ReadAttributeBuilder {

        @JSONField(name="type", defaultValue="read")
        ReadAttributeBuilder type(final String type);

        @JSONField(name="mbean")
        ReadAttributeBuilder mbean(final String mbean);

        @PathParam("uri")
        ReadAttributeBuilder uri(final String uri);

        @POST
        @Path("{uri}")
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<ReadAttrResponse> call();
    }

    public Transformer<RpcRunner, ReadAttrResponse> readAttribute(final String uri, final String objectName);

    @RpcBuilder
    interface ExecBuilder {

        @PathParam("uri")
        ReadAttributeBuilder uri(final String uri);

        @POST
        @Path("{uri}")
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<ExecResponse> call();
    }

    public Transformer<RpcRunner, ExecResponse> exec(final String uri, final JolokiaRequest req);

    public <T extends JolokiaResponse> Transformer<RpcRunner, T[]> batch(final String uri,
            final JolokiaRequest[] reqs, final Class<T[]> cls);
}

package org.jocean.jolokia.internal;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.jocean.http.ContentUtil;
import org.jocean.http.Feature;
import org.jocean.http.Interact;
import org.jocean.http.Interaction;
import org.jocean.http.MessageUtil;
import org.jocean.http.RpcRunner;
import org.jocean.jolokia.JolokiaAPI;
import org.jocean.jolokia.spi.ExecResponse;
import org.jocean.jolokia.spi.JolokiaRequest;
import org.jocean.jolokia.spi.JolokiaResponse;
import org.jocean.jolokia.spi.ListResponse;
import org.jocean.jolokia.spi.ReadAttrResponse;

import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;
import rx.Observable.Transformer;

public class DefaultJolokiaAPI implements JolokiaAPI {

    private Observable<? extends Interaction> sendreq(final Interact interact, final URI uri, final Object req) {
        return interact.method(HttpMethod.POST)
                .uri(uri.toString())
                .path(uri.getRawPath())
                .body(req, ContentUtil.TOJSON)
                .feature(Feature.ENABLE_LOGGING, Feature.ENABLE_COMPRESSOR)
                .execution();
    }

    @Override
    public Transformer<RpcRunner, ListResponse> list(final String uri) {
        return rpcs -> rpcs.flatMap(rpc -> rpc.execute(interact -> {
            final JolokiaRequest req = new JolokiaRequest();
            req.setType("list");

            try {
                return sendreq(interact, new URI(uri), req)
                    .compose(MessageUtil.responseAs(ListResponse.class, MessageUtil::unserializeAsJson))
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public Transformer<RpcRunner, ReadAttrResponse> readAttribute(final String uri, final String objectName) {
        return rpcs -> rpcs.flatMap(rpc -> rpc.execute(interact -> {
            final JolokiaRequest req = new JolokiaRequest();
            req.setType("read");
            req.setMBean(objectName);

            try {
                return sendreq(interact, new URI(uri), req)
                    .compose(MessageUtil.responseAs(ReadAttrResponse.class, MessageUtil::unserializeAsJson))
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public Transformer<RpcRunner, ExecResponse> exec(final String uri, final JolokiaRequest req) {
        return rpcs -> rpcs.flatMap(rpc -> rpc.execute(interact -> {
            try {
                return sendreq(interact, new URI(uri), req)
                    .compose(MessageUtil.responseAs(ExecResponse.class, MessageUtil::unserializeAsJson))
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public <T extends JolokiaResponse> Transformer<RpcRunner, T[]> batch(final String uri,
            final JolokiaRequest[] reqs, final Class<T[]> cls) {
        return rpcs -> rpcs.flatMap(rpc -> rpc.execute(interact -> {
            try {
                return sendreq(interact, new URI(uri), reqs)
                    .compose(MessageUtil.responseAs(cls, MessageUtil::unserializeAsJson))
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

}

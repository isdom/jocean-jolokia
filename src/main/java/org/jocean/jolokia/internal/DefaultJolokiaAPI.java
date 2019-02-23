package org.jocean.jolokia.internal;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.jocean.http.ContentUtil;
import org.jocean.http.Interact;
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

    private Interact sendreq(final Interact interact, final URI uri, final Object req) {
        return interact.method(HttpMethod.POST).uri(uri.toString()).path(uri.getRawPath()).body(req, ContentUtil.TOJSON);
    }

    @Override
    public Transformer<RpcRunner, ListResponse> list(final String uri) {
        return runners -> runners.flatMap(runner -> runner.name("jolokia.list").execute(interact -> {
            final JolokiaRequest req = new JolokiaRequest();
            req.setType("list");

            try {
                return sendreq(interact, new URI(uri), req)
                    .responseAs(ContentUtil.ASJSON, ListResponse.class)
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public Transformer<RpcRunner, ReadAttrResponse> readAttribute(final String uri, final String objectName) {
        return runners -> runners.flatMap(runner -> runner.name("jolokia.readAttribute").execute(interact -> {
            final JolokiaRequest req = new JolokiaRequest();
            req.setType("read");
            req.setMBean(objectName);

            try {
                return sendreq(interact, new URI(uri), req)
                    .responseAs(ContentUtil.ASJSON, ReadAttrResponse.class)
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public Transformer<RpcRunner, ExecResponse> exec(final String uri, final JolokiaRequest req) {
        return runners -> runners.flatMap(runner -> runner.name("jolokia.exec").execute(interact -> {
            try {
                return sendreq(interact, new URI(uri), req)
                    .responseAs(ContentUtil.ASJSON, ExecResponse.class)
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public <T extends JolokiaResponse> Transformer<RpcRunner, T[]> batch(final String uri,
            final JolokiaRequest[] reqs, final Class<T[]> cls) {
        return runners -> runners.flatMap(runner -> runner.name("jolokia.batch").execute(interact -> {
            try {
                return sendreq(interact, new URI(uri), reqs)
                    .responseAs(ContentUtil.ASJSON, cls)
                    .timeout(1, TimeUnit.SECONDS);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

}

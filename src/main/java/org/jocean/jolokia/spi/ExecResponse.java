package org.jocean.jolokia.spi;

import com.alibaba.fastjson.annotation.JSONField;

public class ExecResponse extends JolokiaResponse {

    @JSONField(name="value")
    public Object getValue() {
        return _value;
    }

    @JSONField(name="value")
    public void setValue(final Object value) {
        this._value = value;
    }

    private Object _value;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ExecValueResponse [value=").append(_value)
                .append(", super=").append(super.toString()).append("]");
        return builder.toString();
    }
}

package dynatrace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.sdk.common.InstrumentationLibraryInfo;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.data.EventData;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.data.StatusData;

final class SpanData implements io.opentelemetry.sdk.trace.data.SpanData {

    private final io.opentelemetry.sdk.trace.data.SpanData spanData;
    private final Resource dtMetadata;

    private SpanData(io.opentelemetry.sdk.trace.data.SpanData spanData, Resource metadata) {
        this.spanData = spanData;
        this.dtMetadata = metadata;
    }

    static Collection<io.opentelemetry.sdk.trace.data.SpanData> wrap(Collection<io.opentelemetry.sdk.trace.data.SpanData> spans, Resource metadata) {
        if ((spans == null) || spans.isEmpty()) {
            return spans;
        }
        ArrayList<io.opentelemetry.sdk.trace.data.SpanData> wrapped = new ArrayList<>(spans.size());
        for (io.opentelemetry.sdk.trace.data.SpanData spanData : spans) {
            wrapped.add(new SpanData(spanData, metadata));
        }
        return wrapped;
    }

    @Override
    public Resource getResource() {
        return spanData.getResource().merge(dtMetadata);
    }

    @Override
    public Attributes getAttributes() {
        return spanData.getAttributes();
    }

    @Override
    public long getEndEpochNanos() {
        return spanData.getEndEpochNanos();
    }

    @Override
    public List<EventData> getEvents() {
        return spanData.getEvents();
    }

    @Override
    @Deprecated
    public InstrumentationLibraryInfo getInstrumentationLibraryInfo() {
        return spanData.getInstrumentationLibraryInfo();
    }

    @Override
    public SpanKind getKind() {
        return spanData.getKind();
    }

    @Override
    public List<LinkData> getLinks() {
        return spanData.getLinks();
    }

    @Override
    public String getName() {
        return spanData.getName();
    }

    @Override
    public SpanContext getParentSpanContext() {
        return spanData.getParentSpanContext();
    }

    @Override
    public SpanContext getSpanContext() {
        return spanData.getSpanContext();
    }

    @Override
    public long getStartEpochNanos() {
        return spanData.getStartEpochNanos();
    }

    @Override
    public StatusData getStatus() {
        return spanData.getStatus();
    }

    @Override
    public int getTotalAttributeCount() {
        return spanData.getTotalAttributeCount();
    }

    @Override
    public int getTotalRecordedEvents() {
        return spanData.getTotalRecordedEvents();
    }

    @Override
    public int getTotalRecordedLinks() {
        return spanData.getTotalRecordedLinks();
    }

    @Override
    public boolean hasEnded() {
        return spanData.hasEnded();
    }

}

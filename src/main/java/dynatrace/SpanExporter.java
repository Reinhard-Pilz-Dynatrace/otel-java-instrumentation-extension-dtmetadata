package dynatrace;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.resources.Resource;

final class SpanExporter implements io.opentelemetry.sdk.trace.export.SpanExporter {

    private final io.opentelemetry.sdk.trace.export.SpanExporter exporter;
    private Resource metadata = null;

    SpanExporter(io.opentelemetry.sdk.trace.export.SpanExporter exporter) {
        this.exporter = exporter;
    }

    static io.opentelemetry.sdk.trace.export.SpanExporter wrap(io.opentelemetry.sdk.trace.export.SpanExporter exporter, ConfigProperties properties) {
        return new SpanExporter(exporter);
    }

    @Override
    public CompletableResultCode export(Collection<io.opentelemetry.sdk.trace.data.SpanData> spans) {
        synchronized (exporter) {
            if (metadata == null) {
                Resource resource = Resource.empty();

                for (String name : new String[] { "dt_metadata_e617c525669e072eebe3d0f08212e8f2.properties",
                        "/var/lib/dynatrace/enrichment/dt_metadata.properties" }) {
                    try {
                        Properties props = new Properties();
                        props.load(name.startsWith("/var") ? new FileInputStream(name)
                                : new FileInputStream(Files.readAllLines(Paths.get(name)).get(0)));
                                resource = resource.merge(Resource.create(props.entrySet().stream()
                                .collect(Attributes::builder, (b, e) -> b.put(e.getKey().toString(), e.getValue().toString()),
                                        (b1, b2) -> b1.putAll(b2.build()))
                                .build()));
                    } catch (IOException e) {
                    }
                }
                metadata = resource;
            }
        }
        if (metadata == Resource.empty()) {
            return exporter.export(spans);
        }

        return exporter.export(SpanData.wrap(spans, metadata));
    }

    @Override
    public CompletableResultCode flush() {
        return exporter.flush();
    }

    @Override
    public CompletableResultCode shutdown() {
        return exporter.shutdown();
    }
    
}

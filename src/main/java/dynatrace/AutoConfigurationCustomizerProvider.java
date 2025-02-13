package dynatrace;

import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;

public class AutoConfigurationCustomizerProvider implements io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider {

    @Override
    public void customize(AutoConfigurationCustomizer customizer) {
        customizer.addSpanExporterCustomizer(SpanExporter::wrap);
    }
    
}

package dynatrace;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.api.common.Attributes;

public class MetadataResourceProvider implements ResourceProvider {

    @Override
    public Resource createResource(ConfigProperties config) {
        Resource dtMetadata = Resource.empty();

        for (String name : new String[] { "dt_metadata_e617c525669e072eebe3d0f08212e8f2.properties",
                "/var/lib/dynatrace/enrichment/dt_metadata.properties" }) {
            try {
                Properties props = new Properties();
                props.load(name.startsWith("/var") ? new FileInputStream(name)
                        : new FileInputStream(Files.readAllLines(Paths.get(name)).get(0)));
                dtMetadata = dtMetadata.merge(Resource.create(props.entrySet().stream()
                        .collect(Attributes::builder, (b, e) -> b.put(e.getKey().toString(), e.getValue().toString()),
                                (b1, b2) -> b1.putAll(b2.build()))
                        .build()));
            } catch (IOException e) {
            }
        }
        return dtMetadata;
    }
}

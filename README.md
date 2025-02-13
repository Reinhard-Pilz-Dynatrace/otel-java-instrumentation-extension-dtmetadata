# Extending OpenTelemetry Java Auto Instrumentation with Dynatrace Resource Attributes

The [OpenTelemetry Java Instrumentation Agent](https://github.com/open-telemetry/opentelemetry-java-instrumentation) represents a quick an easy way to introduce OpenTelemetry into an existing Java Application without the need for actual code changes.

Configuring the OpenTelemetry SDK introduced by this Agent relies by default on mechanisms known as [Zero-code SDK autoconfigure](https://opentelemetry.io/docs/languages/java/configuration/#zero-code-sdk-autoconfigure). By default System Properties and/or Environment Variables will be taken into consideration.

That approach comes with the limitation that configuration details need to be known before the JVM is getting launched. That is unfortunately not the case for Resource Attributes like `dt.entity.process_group_instance`, `dt.entity.process_group` and `dt.entity.host`. Dynatrace OneAgent determines them once the process is getting launched - and only that process is able to query for them. The official recommendation for adding these Resource Attributes by Dynatrace is to switch to [Manual Instrumentation](https://docs.dynatrace.com/docs/ingest-from/opentelemetry/walkthroughs/java/java-manual#instrument-your-application). But that approach cannot be used when using the OpenTelemetry Java Agent.

This solution allows for combining both approaches.

## Why and when do I need these additional Resource Attributes?
* With OneAgent in FullStack Mode you likely don't require OpenTelemetry at all
* With OneAgent in Infrastructure Mode or Discovery Mode these additional Resource Attributes allow Dynatrace to assign incoming OpenTelemetry signals (traces, metrics or logs) to the entities (host, process and process group) OneAgent already has discovered. Without these Resource Attributes Dynatrace may contain two separate entities per process.

## Download locations for binaries

* Download the [opentelemetry-javaagent.jar](https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.12.0/opentelemetry-javaagent.jar)
* Download [otel-java-instrumentation-extension-dtmetadata-v2.0.0.jar](https://github.com/Reinhard-Pilz-Dynatrace/otel-java-instrumentation-extension-dtmetadata/releases/download/v2.0.0/otel-java-instrumentation-extension-dtmetadata-v2.0.0.jar) from this repository.


## Usage with environment variables on Linux
```
export JAVA_TOOL_OPTIONS=-javaagent:/path/to/opentelemetry-javaagent.jar
export OTEL_JAVAAGENT_EXTENSIONS=otel-java-instrumentation-extension-dtmetadata-v2.0.0.jar
```
Execute your Java Application using `java -jar your-main-jar`

## Usage with environment variables on Windows
```
set JAVA_TOOL_OPTIONS=-javaagent:/path/to/opentelemetry-javaagent.jar
set OTEL_JAVAAGENT_EXTENSIONS=otel-java-instrumentation-extension-dtmetadata-v2.0.0.jar
```
Execute your Java Application using `java -jar your-main-jar`

## Usage with with JVM argumentes
Execute your Java Application using `java -javaagent:/path/to/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=otel-java-instrumentation-extension-dtmetadata-v2.0.0.jar -jar your-main-jar`

## Implementation Details
This solution utilizes the [Java Service Provider Interface](https://opentelemetry.io/docs/languages/java/configuration/#spi-service-provider-interface) to configure the OpenTelemetry SDK injected by the OpenTelemetry Java Agent.
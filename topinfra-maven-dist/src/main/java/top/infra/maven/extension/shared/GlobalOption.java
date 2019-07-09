package top.infra.maven.extension.shared;

import java.util.Optional;
import java.util.Properties;
import java.util.function.BiFunction;

import top.infra.maven.CiOption;
import top.infra.maven.CiOptionContext;

public enum GlobalOption implements CiOption {

    FAST("fast"),
    /**
     * Auto detect infrastructure using for this build.<br/>
     * example of gitlab-ci's CI_PROJECT_URL: "https://example.com/gitlab-org/gitlab-ce"<br/>
     * ossrh, private or customized infrastructure name.
     */
    INFRASTRUCTURE("infrastructure"),
    ;

    private final String defaultValue;
    private final String propertyName;

    GlobalOption(final String propertyName) {
        this(propertyName, null);
    }

    GlobalOption(final String propertyName, final String defaultValue) {
        this.defaultValue = defaultValue;
        this.propertyName = propertyName;
    }

    public static Optional<String> getInfrastructureSpecificValue(
        final CiOption ciOption,
        final CiOptionContext context
    ) {
        return INFRASTRUCTURE.getValue(context)
            .map(infra -> {
                final String propName = infra + "." + ciOption.getPropertyName();
                final String systemPropName = CiOptions.systemPropertyName(propName);
                return Optional.ofNullable(context.getUserProperties().getProperty(propName))
                    .orElseGet(() -> context.getSystemProperties().getProperty(systemPropName));
            });
    }

    public static Optional<String> setInfrastructureSpecificValue(
        final CiOption ciOption,
        final BiFunction<CiOptionContext, Properties, Optional<String>> superSetProperties,
        final CiOptionContext context,
        final Properties properties
    ) {
        final Optional<String> result = superSetProperties.apply(context, properties);

        result.ifPresent(value ->
            INFRASTRUCTURE.getValue(context).ifPresent(infra ->
                properties.setProperty(infra + "." + ciOption.getPropertyName(), value))
        );

        return result;
    }

    @Override
    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(this.defaultValue);
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }
}

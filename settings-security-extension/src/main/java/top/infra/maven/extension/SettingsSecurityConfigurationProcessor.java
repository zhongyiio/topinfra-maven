package top.infra.maven.extension;

import static org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.cli.CommandLine;
import org.apache.maven.cli.CLIManager;
import org.apache.maven.cli.CliRequest;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;

import top.infra.maven.logging.Logger;
import top.infra.maven.logging.LoggerPlexusImpl;

// @Component(role = OrderedConfigurationProcessor.class, hint = "settings-security")
@Named
@Singleton
public class SettingsSecurityConfigurationProcessor implements OrderedConfigurationProcessor {

    private static final String SS_XML = "settings-security.xml";

    private Logger logger;

    // @Requirement
    private SecDispatcher secDispatcher;

    @Inject
    public SettingsSecurityConfigurationProcessor(
        final org.codehaus.plexus.logging.Logger logger,
        final SecDispatcher secDispatcher
    ) {
        this.logger = new LoggerPlexusImpl(logger);
        this.secDispatcher = secDispatcher;
    }

    @Override
    public int getOrder() {
        return Orders.ORDER_SETTINGS_SECURITY;
    }

    @Override
    public void process(final CliRequest cliRequest) throws Exception {
        final Optional<Path> settingsSecurity = this.findSettingsSecurity(cliRequest);

        if (logger.isInfoEnabled()) {
            logger.info(String.format("settingsSecurity %s", settingsSecurity.orElse(null)));
        }

        settingsSecurity.ifPresent(ss -> {
            final DefaultSecDispatcher defaultSecDispatcher = (DefaultSecDispatcher) this.secDispatcher;
            defaultSecDispatcher.setConfigurationFile(ss.toAbsolutePath().toString());
        });
    }

    private Optional<Path> findSettingsSecurity(final CliRequest cliRequest) {
        // Same logic, 3 different styles :)

        /*
        final String result;
        final String ssInUserProperties = Functions.ssInUserProperties(cliRequest).orElse(null);
        if (ssInUserProperties == null) {
            final String ssInSystemProperties = Functions.ssInSystemProperties(cliRequest).orElse(null);
            if (ssInSystemProperties == null) {
                final String ssInWorkingDir = Functions.ssInWorkingDir(cliRequest).orElse(null);
                if (ssInWorkingDir == null) {
                    result = Functions.ssInCustomSettingsDir(cliRequest).orElse(null);
                } else {
                    result = ssInWorkingDir;
                }
            } else {
                result = ssInSystemProperties;
            }
        } else {
            result = ssInUserProperties;
        }
        return Optional.ofNullable(result);
         */

        /*
        return Optional.ofNullable(Functions.ssInUserProperties(cliRequest)
            .orElseGet(() -> Functions.ssInSystemProperties(cliRequest)
                .orElseGet(() -> Functions.ssInWorkingDir(cliRequest)
                    .orElseGet(() -> Functions.ssInCustomSettingsDir(cliRequest).orElse(null)))));
         */

        return Stream.<Function<CliRequest, Optional<Path>>>of(
            Functions::ssInUserProperties,
            Functions::ssInSystemProperties,
            Functions::ssInWorkingDir,
            Functions::ssInCustomSettingsDir
        )
            .map(fun -> fun.apply(cliRequest))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
    }

    private static class Functions {
        private static Optional<Path> ssInUserProperties(final CliRequest cliRequest) {
            return Optional.ofNullable(cliRequest.getUserProperties().getProperty(SYSTEM_PROPERTY_SEC_LOCATION))
                .map(Paths::get);
        }

        private static Optional<Path> ssInSystemProperties(final CliRequest cliRequest) {
            return Optional.ofNullable(
                Optional.ofNullable(cliRequest.getSystemProperties().getProperty(SYSTEM_PROPERTY_SEC_LOCATION))
                    .orElseGet(() -> cliRequest.getSystemProperties().getProperty("env.CI_OPT_SETTINGS_SECURITY")))
                .map(Paths::get);
        }

        private static Optional<Path> ssInWorkingDir(final CliRequest cliRequest) {
            final Path workingDirectory = Paths.get(cliRequest.getWorkingDirectory());
            final Path ssInWorkingDir = workingDirectory.resolve(SS_XML);
            return Optional.ofNullable(ssInWorkingDir.toFile().exists() ? ssInWorkingDir : null);
        }

        private static Optional<Path> ssInCustomSettingsDir(final CliRequest cliRequest) {
            final CommandLine commandLine = cliRequest.getCommandLine();
            final Optional<Path> result;
            if (commandLine.hasOption(CLIManager.ALTERNATE_USER_SETTINGS)) {
                final Path settingsDir = Paths.get(commandLine.getOptionValue(CLIManager.ALTERNATE_USER_SETTINGS)).getParent();
                final Path ssInSettingsDir = settingsDir.resolve(SS_XML);
                result = Optional.ofNullable(ssInSettingsDir.toFile().exists() ? ssInSettingsDir : null);
            } else {
                result = Optional.empty();
            }
            return result;
        }
    }
}

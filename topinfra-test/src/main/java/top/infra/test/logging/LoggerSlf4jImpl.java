package top.infra.test.logging;

import top.infra.logging.Logger;

public class LoggerSlf4jImpl implements Logger {

    private final org.slf4j.Logger delegate;

    public LoggerSlf4jImpl(final org.slf4j.Logger logger) {
        this.delegate = logger;
    }

    @Override
    public void debug(final String message) {
        this.delegate.debug(message);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        this.delegate.debug(message, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.delegate.isDebugEnabled();
    }

    @Override
    public void info(final String message) {
        this.delegate.info(message);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        this.delegate.info(message, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.delegate.isInfoEnabled();
    }

    @Override
    public void warn(final String message) {
        this.delegate.warn(message);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        this.delegate.warn(message, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.delegate.isWarnEnabled();
    }

    @Override
    public void error(final String message) {
        this.delegate.error(message);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        this.delegate.error(message, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.delegate.isErrorEnabled();
    }
}

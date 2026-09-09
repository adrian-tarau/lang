package net.microfalx.lang.service;

import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

/**
 * A logger implementation which wraps the SLF4J logger.
 */
public class LoggerImpl implements Logger {

    private final org.slf4j.Logger delegate;

    static Logger create(Class<?> clazz) {
        return new LoggerImpl(org.slf4j.LoggerFactory.getLogger(clazz));
    }

    public LoggerImpl(org.slf4j.Logger delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return shouldForward() && delegate.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        forward(() -> delegate.trace(msg));
        if (delegate.isTraceEnabled()) append(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        forward(() -> delegate.trace(format, arg));
        if (delegate.isTraceEnabled()) append(format(format, arg));
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        forward(() -> delegate.trace(format, arg1, arg2));
        if (delegate.isTraceEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void trace(String format, Object... arguments) {
        forward(() -> delegate.trace(format, arguments));
        if (delegate.isTraceEnabled()) append(format(format, arguments));
    }

    @Override
    public void trace(String msg, Throwable t) {
        forward(() -> delegate.trace(msg, t));
        if (delegate.isTraceEnabled()) append(msg);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return shouldForward() && delegate.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        forward(() -> delegate.trace(marker, msg));
        if (delegate.isTraceEnabled()) append(msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        forward(() -> delegate.trace(marker, format, arg));
        if (delegate.isTraceEnabled()) append(format(format, arg));
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        forward(() -> delegate.trace(marker, format, arg1, arg2));
        if (delegate.isTraceEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        forward(() -> delegate.trace(marker, format, argArray));
        if (delegate.isTraceEnabled()) append(format(format, argArray));
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        forward(() -> delegate.trace(marker, msg, t));
        if (delegate.isTraceEnabled()) append(msg);
    }

    @Override
    public boolean isDebugEnabled() {
        return shouldForward() && delegate.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        forward(() -> delegate.debug(msg));
        if (delegate.isDebugEnabled()) append(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        forward(() -> delegate.debug(format, arg));
        if (delegate.isDebugEnabled()) append(format(format, arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        forward(() -> delegate.debug(format, arg1, arg2));
        if (delegate.isDebugEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void debug(String format, Object... arguments) {
        forward(() -> delegate.debug(format, arguments));
        if (delegate.isDebugEnabled()) append(format(format, arguments));
    }

    @Override
    public void debug(String msg, Throwable t) {
        forward(() -> delegate.debug(msg, t));
        if (delegate.isDebugEnabled()) append(msg);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return shouldForward() && delegate.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        forward(() -> delegate.debug(marker, msg));
        if (delegate.isDebugEnabled()) append(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        forward(() -> delegate.debug(marker, format, arg));
        if (delegate.isDebugEnabled()) append(format(format, arg));
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        forward(() -> delegate.debug(marker, format, arg1, arg2));
        if (delegate.isDebugEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        forward(() -> delegate.debug(marker, format, arguments));
        if (delegate.isDebugEnabled()) append(format(format, arguments));
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        forward(() -> delegate.debug(marker, msg, t));
        if (delegate.isDebugEnabled()) append(msg);
    }

    @Override
    public boolean isInfoEnabled() {
        return shouldForward() && delegate.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        forward(() -> delegate.info(msg));
        if (delegate.isInfoEnabled()) append(msg);
    }

    @Override
    public void info(String format, Object arg) {
        forward(() -> delegate.info(format, arg));
        if (delegate.isInfoEnabled()) append(format(format, arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        forward(() -> delegate.info(format, arg1, arg2));
        if (delegate.isInfoEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void info(String format, Object... arguments) {
        forward(() -> delegate.info(format, arguments));
        if (delegate.isInfoEnabled()) append(format(format, arguments));
    }

    @Override
    public void info(String msg, Throwable t) {
        forward(() -> delegate.info(msg, t));
        if (delegate.isInfoEnabled()) append(msg);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return shouldForward() && delegate.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        forward(() -> delegate.info(marker, msg));
        if (delegate.isInfoEnabled()) append(msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        forward(() -> delegate.info(marker, format, arg));
        if (delegate.isInfoEnabled()) append(format(format, arg));
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        forward(() -> delegate.info(marker, format, arg1, arg2));
        if (delegate.isInfoEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        forward(() -> delegate.info(marker, format, arguments));
        if (delegate.isInfoEnabled()) append(format(format, arguments));
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        forward(() -> delegate.info(marker, msg, t));
        if (delegate.isInfoEnabled()) append(msg);
    }

    @Override
    public boolean isWarnEnabled() {
        return shouldForward() && delegate.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        forward(() -> delegate.warn(msg));
        if (delegate.isWarnEnabled()) append(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        forward(() -> delegate.warn(format, arg));
        if (delegate.isWarnEnabled()) append(format(format, arg));
    }

    @Override
    public void warn(String format, Object... arguments) {
        forward(() -> delegate.warn(format, arguments));
        if (delegate.isWarnEnabled()) append(format(format, arguments));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        forward(() -> delegate.warn(format, arg1, arg2));
        if (delegate.isWarnEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void warn(String msg, Throwable t) {
        forward(() -> delegate.warn(msg, t));
        if (delegate.isWarnEnabled()) append(msg);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return shouldForward() && delegate.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        forward(() -> delegate.warn(marker, msg));
        if (delegate.isWarnEnabled()) append(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        forward(() -> delegate.warn(marker, format, arg));
        if (delegate.isWarnEnabled()) append(format(format, arg));
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        forward(() -> delegate.warn(marker, format, arg1, arg2));
        if (delegate.isWarnEnabled()) append(format(format, arg1, arg2));
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        forward(() -> delegate.warn(marker, format, arguments));
        if (delegate.isWarnEnabled()) append(format(format, arguments));
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        forward(() -> delegate.warn(marker, msg, t));
        if (delegate.isWarnEnabled()) append(msg);
    }

    @Override
    public boolean isErrorEnabled() {
        return shouldForward() && delegate.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        forward(() -> delegate.error(msg));
        append(msg);
    }

    @Override
    public void error(String format, Object arg) {
        forward(() -> delegate.error(format, arg));
        append(format(format, arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        forward(() -> delegate.error(format, arg1, arg2));
        append(format(format, arg1, arg2));
    }

    @Override
    public void error(String format, Object... arguments) {
        forward(() -> delegate.error(format, arguments));
        append(format(format, arguments));
    }

    @Override
    public void error(String msg, Throwable t) {
        forward(() -> delegate.error(msg, t));
        append(msg);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return shouldForward() && delegate.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        forward(() -> delegate.error(marker, msg));
        append(msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        forward(() -> delegate.error(marker, format, arg));
        append(format(format, arg));
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        forward(() -> delegate.error(marker, format, arg1, arg2));
        append(format(format, arg1, arg2));
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        forward(() -> delegate.error(marker, format, arguments));
        append(format(format, arguments));
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        forward(() -> delegate.error(marker, msg, t));
        append(msg);
    }

    private void forward(Runnable runnable) {
        if (shouldForward()) runnable.run();
    }

    private void append(String message) {
        if (message != null && shouldAppend()) {
            message = delegate.getName() + " : " + message;
            getLogger().append(message);
        }
    }

    private String format(String pattern, Object... arguments) {
        if (pattern == null) return null;
        return MessageFormatter.arrayFormat(pattern, arguments).getMessage();
    }

    private static boolean shouldForward() {
        return !ServiceLocator.isQuiet();
    }

    private static boolean shouldAppend() {
        return ServiceLocator.isQuiet();
    }

    private static net.microfalx.lang.Logger getLogger() {
        return ServiceLocator.quietLogger;
    }
}

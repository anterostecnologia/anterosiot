package br.com.anteros.iot.protocol.modbus;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.anteros.iot.protocol.modbus.type.ModbusProtocolErrorCode;

public class ModbusProtocolException extends Exception {

    private static final long serialVersionUID = -6155136065068974723L;

    private static final String PROTOCOL_GENERIC_MESSAGES_PATTERN = "Generic Error - {0}: {1} {2} {3} {4} {5}";
    private static final String PROTOCOL_EXCEPTION_MESSAGES_BUNDLE = "org.eclipse.kura.protocol.messages.ProtocolExceptionMessagesBundle";

    private static final Logger s_logger = LogManager.getLogger(ModbusProtocolException.class);

    protected ModbusProtocolErrorCode m_code;
    private Object[] m_arguments;
    private String m_complement;

    @SuppressWarnings("unused")
    private ModbusProtocolException() {
        super();
    }

    @SuppressWarnings("unused")
    private ModbusProtocolException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    private ModbusProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unused")
    private ModbusProtocolException(Throwable t) {
        super(t);
    }

    /**
     * Builds a new EdcException instance based on the supplied EdcErrorCode.
     *
     * @param code
     */
    public ModbusProtocolException(ModbusProtocolErrorCode code) {
        this.m_code = code;
    }

    /**
     * Builds a new EdcException instance based on the supplied EdcErrorCode and an optional complement string
     *
     * @param code
     * @param complement
     */
    public ModbusProtocolException(ModbusProtocolErrorCode code, String complement) {
        this.m_code = code;
        this.m_complement = complement;
    }

    /**
     * Builds a new EdcException instance based on the supplied EdcErrorCode, an optional Throwable cause, and optional
     * arguments for the associated exception message.
     *
     * @param code
     * @param arguments
     */
    public ModbusProtocolException(ModbusProtocolErrorCode code, Throwable cause, Object... arguments) {
        super(cause);

        this.m_code = code;
        this.m_arguments = arguments;
    }

    public ModbusProtocolErrorCode getCode() {
        return this.m_code;
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(Locale.US);
    }

    @Override
    public String getLocalizedMessage() {
        return getLocalizedMessage(Locale.getDefault());
    }

    private String getLocalizedMessage(Locale locale) {

        String pattern = getMessagePattern(locale, this.m_code);
        String message = MessageFormat.format(pattern, this.m_arguments) + " " + this.m_complement;
        return message;
    }

    private String getMessagePattern(Locale locale, ModbusProtocolErrorCode code) {

        //
        // Load the message pattern from the bundle
        String messagePattern = null;
        ResourceBundle resourceBundle = null;
        try {

            resourceBundle = ResourceBundle.getBundle(PROTOCOL_EXCEPTION_MESSAGES_BUNDLE, locale);
            messagePattern = resourceBundle.getString(code.name());
            if (messagePattern == null) {
                s_logger.warn("Could not find Exception Messages for Locale {} and code {}", locale, code);
            }
        } catch (MissingResourceException mre) {
            // log the failure to load a message bundle
            s_logger.warn("Could not load Exception Messages Bundle for Locale {}", locale);
        }

        //
        // If no bundle or code in the bundle is found, use a generic message
        if (messagePattern == null) {
            // build a generic message format
            messagePattern = MessageFormat.format(PROTOCOL_GENERIC_MESSAGES_PATTERN, code.name());
        }

        return messagePattern;
    }

}

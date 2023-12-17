package com.ljparfan.showbooking.command;

import com.ljparfan.showbooking.exception.AppException;
import org.jline.terminal.Terminal;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.shell.ParameterValidationException;
import org.springframework.shell.command.annotation.ExceptionResolver;
import org.springframework.shell.command.annotation.ExitCode;

import java.io.PrintWriter;

import static com.ljparfan.showbooking.util.ApplicationConstants.DEFAULT_ERROR_MESSAGE;

public abstract class AppCommands {
    @ExceptionResolver(ConversionFailedException.class)
    @ExitCode(1)
    void paramConversionErrorHandler(ConversionFailedException e, Terminal terminal) {
        PrintWriter writer = terminal.writer();
        writer.println(DEFAULT_ERROR_MESSAGE);
        writer.flush();
    }
    @ExceptionResolver(ParameterValidationException.class)
    @ExitCode(2)
    void commandOptionParsingErrorHandler(ParameterValidationException e, Terminal terminal) {
        PrintWriter writer = terminal.writer();
        e.getConstraintViolations().forEach(violation -> writer.println(violation.getMessageTemplate()));
        writer.flush();
    }

    @ExceptionResolver(AppException.class)
    @ExitCode(1)
    void customExceptionErrorHandler(AppException e, Terminal terminal) {
        PrintWriter writer = terminal.writer();
        writer.println(e.getMessage());
        writer.flush();
    }
}

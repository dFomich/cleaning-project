package com.epam.cleaningProject.command.factory;

import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.CommandType;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.util.MessageManager;

/**
 * Define command.
 *
 * @return the Optional<Command>
 */
public class CommandFactory {
    private final static Logger logger = LogManager.getLogger();
    public Optional<Command> defineCommand(RequestContent content) {
        Optional<Command> current;
        String action = content.getRequestParameter(ConstantName.PARAM_NAME_COMMAND);
        if (action == null || action.isEmpty()) {
            return Optional.empty();
        }
        try {
            CommandType currentType = CommandType.valueOf(action.toUpperCase());
            current = Optional.of(currentType.getCurrentCommand());
        } catch ( IllegalArgumentException e) {
            current = Optional.empty();
            content.addRequestAttribute(ConstantName.ATTRIBUTE_WRONG_ACTION,
                    MessageManager.getProperty(ConstantName.MESSAGE_WRONG_ACTION));
            logger.log(Level.ERROR, "Can't define command", e);
        }
        return current;
    }
}

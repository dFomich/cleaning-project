package com.epam.cleaningProject.command.cleaning;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.Cleaning;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.CleaningServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class GoCleaningProfileCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        String start = content.getRequestParameter(ConstantName.ATTRIBUTE_START);
        CleaningServiceImpl cleaningService = new CleaningServiceImpl();
        Long cleaningId = Long.valueOf(content.getRequestParameter(ConstantName.PARAMETER_CLEANING_ID));
        try {
            Optional<Cleaning> cleaningOptional = cleaningService.findCleaningById(cleaningId);
            if (cleaningOptional.isPresent()) {
                Cleaning cleaning = cleaningOptional.get();
                content.addSessionAttribute(ConstantName.ATTRIBUTE_CLEANING_ID, cleaningId);
                content.addSessionAttribute(ConstantName.ATTRIBUTE_CLEANING, cleaning);
                content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANING_PROFILE));
            } else {
                content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                content.addRequestAttribute(ConstantName.ATTRIBUTE_CLEANING_EDIT_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_CLEANING_SHOW_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_CLEANINGS));
            }
        } catch (ServiceException e) {
            logger.error("Error while executing command", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

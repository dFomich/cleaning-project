package com.epam.cleaningProject.command.cleaner;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.Cleaner;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.CleanerServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class GoCleanerProfileCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        CleanerServiceImpl cleanerService = new CleanerServiceImpl();
        User user = (User) content.getSessionAttribute(ConstantName.PARAMETER_USER);
        Long cleanerId = user.getUserId();
        try {
            Optional<Cleaner> cleanerOptional = cleanerService.findCleanerById(cleanerId);
            if (cleanerOptional.isPresent()) {
                Cleaner cleaner = cleanerOptional.get();
                content.addSessionAttribute(ConstantName.ATTRIBUTE_USER_PROFILE, cleaner);
//                content.addRequestAttribute(ConstantName.ATTRIBUTE_VALIDATED_MAP, cleaner);
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_PROFILE));
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_EDIT_PROFILE_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_PROFILE_SHOW_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_CABINET));
            }
        } catch (ServiceException e) {
            logger.error("Error while executing command", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
            router.setType(RouteType.FORWARD);
        }
        return router;
    }
}

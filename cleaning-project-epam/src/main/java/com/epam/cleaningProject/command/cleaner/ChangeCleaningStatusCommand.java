package com.epam.cleaningProject.command.cleaner;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.Cleaning;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.CleaningServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class ChangeCleaningStatusCommand implements Command {
    private final static Logger logger = LogManager.getLogger();
    /**
     * Gets cleaning status which cleaner wants to change from the request.
     * Edits cleaning value (updates database) and returns router to the same page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see CleaningServiceImpl#changeCleaningStatus(long, boolean)
     */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        CleaningServiceImpl cleaningService = new CleaningServiceImpl();
        List<Cleaning> cleaningList;
        String start = content.getRequestParameter(ConstantName.PARAMETER_PAGE_START);
        User user = (User) content.getSessionAttribute(ConstantName.PARAMETER_USER);
        Long cleaningId = Long.valueOf(content.getRequestParameter(ConstantName.PARAMETER_CLEANING_ID));
        Boolean availableStatus = Boolean.valueOf(content.getRequestParameter(ConstantName.PARAMETER_CLEANING_STATUS));
        try {
            if (cleaningService.changeCleaningStatus(cleaningId, availableStatus)) {
                cleaningList = cleaningService.findCleaningByCleanerId(user.getUserId());
                if (!cleaningList.isEmpty()) {
                    content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                    content.addSessionAttribute(ConstantName.ATTRIBUTE_CLEANING_LIST, cleaningList);
                    router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_CLEANINGS));
                } else {
                    content.addRequestAttribute(ConstantName.ATTRIBUTE_SHOW_CLEANING_ERROR,
                            MessageManager.getProperty(ConstantName.MESSAGE_SHOW_CLEANING_ERROR));
                    router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_CLEANINGS));
                }
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_CLEANING_STATUS_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_CLEANING_STATUS_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_CLEANINGS));
            }
        } catch (ServiceException e) {
            logger.error("Error while changing cleaning status", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }

        return router;
    }
}

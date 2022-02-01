package com.epam.cleaningProject.command.admin;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.Cleaner;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.CleanerServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class ShowBlockedCleanersCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets all blocked cleaners from the database,
     * sets the session attribute to show them and
     * returns router to the blocked cleaners page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see CleanerServiceImpl#findBlockedCleaners()
     */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        CleanerServiceImpl cleanerService = new CleanerServiceImpl();
        List<Cleaner> cleanerBlockedList;
        try {
            cleanerBlockedList = cleanerService.findBlockedCleaners();
            if (!cleanerBlockedList.isEmpty()) {
                content.addSessionAttribute(ConstantName.ATTRIBUTE_CLEANER__BLOCKED_LIST, cleanerBlockedList);
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_SHOW_BLOCKED_CLEANERS));
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_SHOW_CLEANERS_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_SHOW_CLEANER_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ADMIN_CABINET));
            }
        } catch (ServiceException e) {
            logger.error("Error while getting all blocked cleaners", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

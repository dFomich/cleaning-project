package com.epam.cleaningProject.command.cleaning;

import java.util.List;

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


public class ShowCatalogCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets all cleanings from the database,
     * sets session attributes to show this collection and
     * returns router to the cleanings page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see CleaningServiceImpl#findAllCleaning()
     */

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        CleaningServiceImpl cleaningService = new CleaningServiceImpl();
        List<Cleaning> cleaningList;
        try {
            cleaningList = cleaningService.findAllCleaning();
            if (!cleaningList.isEmpty()) {
                content.addSessionAttribute(ConstantName.ATTRIBUTE_CLEANING_LIST, cleaningList);
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_SHOW_CLEANING));
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_SHOW_CLEANING_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_SHOW_CLEANING_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_SHOW_CLEANING));
            }
        } catch (ServiceException e) {
            logger.error("Error while getting all cleanings", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

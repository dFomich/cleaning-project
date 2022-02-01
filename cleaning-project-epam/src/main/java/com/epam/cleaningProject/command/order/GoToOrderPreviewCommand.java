package com.epam.cleaningProject.command.order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.service.serviceImpl.CleaningServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;

public class GoToOrderPreviewCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets a customer's order list from the session attribute,
     * defines its total price,
     * returns router to the order preview page.
     * If the list is empty, total sum isn't calculated.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see CleaningServiceImpl#findCleaningById(long)
     */

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        String start = content.getRequestParameter(ConstantName.PARAMETER_PAGE_START);
        content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_GO_TO_CLEANING_LIST));
        router.setType(RouteType.FORWARD);
        return router;
    }
}

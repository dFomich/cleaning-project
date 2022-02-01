package com.epam.cleaningProject.command.admin;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.UserServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class ChangeUserStatusCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets user status which administrator wants to change from the request.
     * Edits user value (updates database) and returns router to the same page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see UserServiceImpl#changeUserStatus(long, boolean)
     */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        UserServiceImpl userService = new UserServiceImpl();
        String start = content.getRequestParameter(ConstantName.PARAMETER_PAGE_START);
        Long id = Long.valueOf(content.getRequestParameter(ConstantName.PARAMETER_ID));
        String page = (String) content.getSessionAttribute(ConstantName.ATTRIBUTE_PAGE_PATH);
        Boolean status = Boolean.valueOf(content.getRequestParameter(ConstantName.PARAMETER_ACTIVE_STATUS));
        try {
            if (userService.changeUserStatus(id, status)) {
                content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                router.setPagePath(page);
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_BLOCK_USER_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_BLOCKING_ERROR));
                router.setPagePath(page);
            }
        } catch (ServiceException e) {
            logger.error("Error while changing user status", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

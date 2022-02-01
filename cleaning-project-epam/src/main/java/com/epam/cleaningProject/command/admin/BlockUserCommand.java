package com.epam.cleaningProject.command.admin;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.service.serviceImpl.UserServiceImpl;
import com.epam.cleaningProject.util.MessageManager;



public class BlockUserCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public Router execute(RequestContent content) {
        logger.log(Level.DEBUG, "IN BlockUserCommand");
        Router router = new Router();
        UserServiceImpl userService = new UserServiceImpl();
        String start = content.getRequestParameter(ConstantName.PARAMETER_PAGE_START);
        Long id = Long.valueOf(content.getRequestParameter(ConstantName.PARAMETER_ID));
        String page = (String) content.getSessionAttribute(ConstantName.ATTRIBUTE_PAGE_PATH);
        logger.log(Level.DEBUG, page);
        logger.log(Level.DEBUG, start + " START");
        logger.log(Level.DEBUG, id + " CLIENT ID");
        boolean blocked = userService.blockUser(id);
		logger.log(Level.DEBUG, blocked + "BLOCKED STATUS");
		if (blocked) {
		    content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
		    content.addRequestAttribute(ConstantName.ATTRIBUTE_BLOCK_USER,
		            MessageManager.getProperty(ConstantName.MESSAGE_BLOCK_USER));
		    router.setPagePath(page);
		    logger.log(Level.DEBUG, router.getPagePath() + " FROM BLOCK COMMAND IF");
		    router.setType(RouteType.FORWARD);
		} else {
		    content.addRequestAttribute(ConstantName.ATTRIBUTE_BLOCK_USER_ERROR,
		            MessageManager.getProperty(ConstantName.MESSAGE_BLOCKING_ERROR));
		    router.setPagePath(page);
		    logger.log(Level.DEBUG, router.getPagePath() + " FROM BLOCK COMMAND ELSE");
		    router.setType(RouteType.FORWARD);
		}


        return router;
    }
}

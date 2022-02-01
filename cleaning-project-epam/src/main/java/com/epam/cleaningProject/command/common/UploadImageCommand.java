package com.epam.cleaningProject.command.common;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.UserServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class UploadImageCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public Router execute(RequestContent content) {
        UserServiceImpl userService = new UserServiceImpl();
        Router router = new Router();
        User user = (User) content.getSessionAttribute(ConstantName.PARAMETER_USER);
        Long id = user.getUserId();
        Part part = (Part) content.getRequestAttribute(ConstantName.ATTR_PARTS);
        String pagePath = (String) content.getSessionAttribute(ConstantName.ATTRIBUTE_PAGE_PATH);
        try {
            InputStream stream = part.getInputStream();
            if (userService.setUserAvatar(id, stream)) {
                router.setPagePath(pagePath);
                content.addRequestAttribute(ConstantName.ATTRIBUTE_UPLOAD_SUCCESS,
                        MessageManager.getProperty(ConstantName.MESSAGE_UPLOAD_SUCCESS));
                user = userService.findById(id).get();
                content.addSessionAttribute(ConstantName.ATTRIBUTE_USER, user);
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_UPLOAD_FAIL,
                        MessageManager.getProperty(ConstantName.MESSAGE_UPLOAD_FAIL));
                router.setPagePath(pagePath);
            }
        } catch (ServiceException | IOException e) {
            logger.error("Error while saving avatar path", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

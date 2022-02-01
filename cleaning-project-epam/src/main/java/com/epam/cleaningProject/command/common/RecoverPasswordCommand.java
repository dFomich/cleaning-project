package com.epam.cleaningProject.command.common;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.email.SendEmail;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.UserServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class RecoverPasswordCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        SendEmail sendEmail = new SendEmail();
        UserServiceImpl userService = new UserServiceImpl();
        String login = content.getRequestParameter(ConstantName.PARAMETER_LOGIN).trim();
        try {
            if (userService.checkUserLogin(login)) {
                Optional<User> userOptional = userService.findByLogin(login);
                if (userOptional.isPresent()) {
                    if (userService.changePassword(userOptional.get(), ConstantName.EMAIL_TEMPORARY_PASSWORD)) {
                        sendEmail.send(login, ConstantName.SUBJECT_PASSWORD_RECOVER, ConstantName.EMAIL_PASSWORD_RECOVER);
                        content.addRequestAttribute(ConstantName.ATTRIBUTE_PASSWORD_RECOVER_SUCCESS,
                                MessageManager.getProperty(ConstantName.MESSAGE_PASSWORD_RECOVER_SUCCESS));
                        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_LOGIN));
                    } else {
                        setErrorMessage(content, router);
                    }
                } else {
                    setErrorMessage(content, router);
                }
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_PASSWORD_RECOVER_NO_LOGIN,
                        MessageManager.getProperty(ConstantName.MESSAGE_PASSWORD_RECOVER_NO_LOGIN));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_PASSWORD_RECOVER));
            }
        } catch (ServiceException e) {
            logger.error("Error while recovering password", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
    private void setErrorMessage(RequestContent content, Router router) {
        content.addRequestAttribute(ConstantName.ATTRIBUTE_PASSWORD_RECOVER_ERROR,
                MessageManager.getProperty(ConstantName.MESSAGE_PASSWORD_RECOVER_ERROR));
        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_PASSWORD_RECOVER));
    }
}

package com.epam.cleaningProject.command.admin;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.email.SendEmail;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.util.ConfigurationManager;

public class SendEmailCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        String page = ConfigurationManager.getProperty(ConstantName.JSP_LOGIN);
        SendEmail sendEmail = new SendEmail();
        String mailTo = content.getRequestParameter(ConstantName.PARAMETER_MAIL);
        String subject = content.getRequestParameter(ConstantName.PARAMETER_MAIL_SUBJECT);
        String message = content.getRequestParameter(ConstantName.PARAMETER_MAIL_MESSAGE);
        try {
            sendEmail.send(mailTo, subject, message);
            router.setPagePath(page);
        } catch (ServiceException e) {
            logger.error("Error while changing user status", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

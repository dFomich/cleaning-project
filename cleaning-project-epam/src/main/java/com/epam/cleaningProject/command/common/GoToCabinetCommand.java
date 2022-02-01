package com.epam.cleaningProject.command.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.entity.UserRole;
import com.epam.cleaningProject.util.ConfigurationManager;

public class GoToCabinetCommand implements Command {
    private final static Logger logger = LogManager.getLogger();
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        User user = (User) content.getSessionAttribute(ConstantName.ATTRIBUTE_USER);
        UserRole userRole = user.getUserRole();
        switch (userRole) {
            case ADMIN:
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ADMIN_CABINET));
                break;
            case CLEANER:
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_CABINET));
                break;
            case CLIENT:
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLIENT_CABINET));
                break;
            default:
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_LOGIN));
        }
        return router;
    }
}

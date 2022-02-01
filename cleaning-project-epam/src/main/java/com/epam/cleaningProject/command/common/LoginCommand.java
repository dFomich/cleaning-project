package com.epam.cleaningProject.command.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.CleaningItem;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.entity.UserRole;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.CleanerServiceImpl;
import com.epam.cleaningProject.service.serviceImpl.ClientServiceImpl;
import com.epam.cleaningProject.service.serviceImpl.UserServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;


public class LoginCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets login and password values from the request.
     * Validates this values, if input data is not valid, or no such user is presented in the database (user is null),
     * or user is blocked, returns router to the same page with message about incorrect login or password.
     * Otherwise, finds the user by this values and sets sessions attributes and
     * returns router to the user's cabinet page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see UserServiceImpl#findUserByLoginAndPassword(String, String)
     * @see UserServiceImpl#getUserRoleId(String)
     * @see UserServiceImpl#findByLogin(String)
     */
    @Override
    public Router execute(RequestContent content) {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        ClientServiceImpl clientService = new ClientServiceImpl();
        CleanerServiceImpl cleanerService = new CleanerServiceImpl();
        User user;
        Router router = new Router();
        List<CleaningItem> clientCleaningList = new ArrayList<>();
        BigDecimal totalSum = new BigDecimal(0);
        String login = content.getRequestParameter(ConstantName.PARAMETER_LOGIN);
        String password = content.getRequestParameter(ConstantName.PARAMETER_PASSWORD);
        try {
            if (userServiceImpl.findUserByLoginAndPassword(login, password)) {
                Optional<User> userOptional = userServiceImpl.findByLogin(login);
                if (userOptional.isPresent()) {
                    user = userOptional.get();
                    if (user.getIsActive()) {
                        content.addSessionAttribute(ConstantName.ATTRIBUTE_USER, user);
                        UserRole userRole = user.getUserRole();
                        switch (userRole) {
                            case ADMIN:
                                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ADMIN_CABINET));
                                break;
                            case CLEANER:
                                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLEANER_CABINET));
                                break;
                            case CLIENT:
                                content.addSessionAttribute(ConstantName.ATTRIBUTE_ORDER_LIST, clientCleaningList);
                                content.addSessionAttribute(ConstantName.ATTRIBUTE_TOTAL_ORDER_SUM, totalSum);
                                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLIENT_CABINET));
                                break;
                            default:
                                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_MAIN));
                        }
                    } else {
                        content.addRequestAttribute(ConstantName.ATTRIBUTE_USER_IS_BLOCKED_ERROR,
                                MessageManager.getProperty(ConstantName.MESSAGE_BLOCKED_USER_ERROR));
                        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_LOGIN));
                        router.setType(RouteType.FORWARD);
                    }
                } else {
                    content.addRequestAttribute(ConstantName.ATTRIBUTE_LOGIN_ERROR,
                            MessageManager.getProperty(ConstantName.MESSAGE_LOGIN_ERROR));
                    router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_LOGIN));
                }
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_LOGIN_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_LOGIN_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_LOGIN));
            }
        } catch (ServiceException e) {
            logger.error("Error while login command", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
            router.setType(RouteType.REDIRECT);
        }
        return router;
    }
}

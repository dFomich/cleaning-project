package com.epam.cleaningProject.command.client;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.Client;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.ClientServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class GoClientProfileCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        ClientServiceImpl clientService = new ClientServiceImpl();
        User user = (User) content.getSessionAttribute(ConstantName.PARAMETER_USER);
        Long clientId = user.getUserId();
        try {
            Optional<Client> clientOptional = clientService.findById(clientId);
            if (clientOptional.isPresent()) {
                Client client = clientOptional.get();
                content.addRequestAttribute(ConstantName.ATTRIBUTE_USER_PROFILE, client);
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLIENT_PROFILE));
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_EDIT_PROFILE_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_PROFILE_SHOW_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLIENT_CABINET));
            }
        } catch (ServiceException e) {
            logger.error("Error while executing command", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

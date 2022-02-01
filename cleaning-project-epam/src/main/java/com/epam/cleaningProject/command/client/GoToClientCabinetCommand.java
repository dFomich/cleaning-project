package com.epam.cleaningProject.command.client;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.util.ConfigurationManager;

public class GoToClientCabinetCommand implements Command {
    @Override
    public Router execute(RequestContent request) {
        Router router = new Router();
       router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLIENT_CABINET));
       router.setType(RouteType.FORWARD);
        return router;
    }
}

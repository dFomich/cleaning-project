package com.epam.cleaningProject.command.common;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.util.ConfigurationManager;

public class MainCommand implements Command {
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_MAIN));
        return router;
    }
}

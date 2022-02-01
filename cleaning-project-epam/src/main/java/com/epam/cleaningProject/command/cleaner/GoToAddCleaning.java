package com.epam.cleaningProject.command.cleaner;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.util.ConfigurationManager;

public class GoToAddCleaning implements Command {
    @Override
    public Router execute(RequestContent content) {
        Router router =new Router();
        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ADD_CLEANING));
        return router;
    }
}

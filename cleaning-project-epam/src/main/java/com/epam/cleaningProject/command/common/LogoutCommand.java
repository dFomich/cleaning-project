package com.epam.cleaningProject.command.common;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.util.ConfigurationManager;

public class LogoutCommand implements Command {
    /**
     * Invalidates user session.
     * Returns router to the main page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        content.setInvalidateSession(true);
        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_LOGIN));
        return router;
    }
}

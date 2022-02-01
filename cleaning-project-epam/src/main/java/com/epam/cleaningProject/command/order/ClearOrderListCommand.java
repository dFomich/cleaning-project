package com.epam.cleaningProject.command.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.CleaningItem;
import com.epam.cleaningProject.util.ConfigurationManager;

public class ClearOrderListCommand implements Command {
    /**
     * Remove all cleaning from the order list.
     * Sets total sum value 0.
     * Returns router to the cleanings catalog.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        List<CleaningItem> cleaningList = new ArrayList<>();
        BigDecimal totalSum = new BigDecimal(ConstantName.ZERO_VALUE);
        content.addSessionAttribute(ConstantName.ATTRIBUTE_TOTAL_ORDER_SUM, totalSum);
        content.addSessionAttribute(ConstantName.ATTRIBUTE_ORDER_LIST, cleaningList);
        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_GO_TO_CLEANING_LIST));
        router.setType(RouteType.FORWARD);
        return router;
    }
}

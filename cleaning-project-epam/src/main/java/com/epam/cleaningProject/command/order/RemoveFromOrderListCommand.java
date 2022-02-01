package com.epam.cleaningProject.command.order;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.CleaningItem;
import com.epam.cleaningProject.service.CleaningListAction;
import com.epam.cleaningProject.util.ConfigurationManager;


public class RemoveFromOrderListCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets cleaning id from the request,
     * removes the cleaning from the shopping cart and
     * recalculates cart sum.
     * Returns router to the same page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see CleaningListAction#removeItem(List, long)
     */

    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        CleaningListAction action = new CleaningListAction();
        List<CleaningItem> cleaningList = (List<CleaningItem>)
                content.getSessionAttribute(ConstantName.ATTRIBUTE_ORDER_LIST);
        Long itemId = Long.valueOf(content.getRequestParameter(ConstantName.ATTRIBUTE_CLEANING_ID));
        action.removeItem(cleaningList, itemId);
        BigDecimal totalSum = action.calculateTotalSum(cleaningList);
        content.addSessionAttribute(ConstantName.ATTRIBUTE_TOTAL_ORDER_SUM, totalSum);
        content.addSessionAttribute(ConstantName.ATTRIBUTE_ORDER_LIST, cleaningList);
        router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_GO_TO_CLEANING_LIST));
        router.setType(RouteType.FORWARD);
        return router;
    }
}


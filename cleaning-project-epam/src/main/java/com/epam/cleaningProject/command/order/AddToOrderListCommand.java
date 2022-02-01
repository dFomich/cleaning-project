package com.epam.cleaningProject.command.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.Cleaning;
import com.epam.cleaningProject.entity.CleaningItem;
import com.epam.cleaningProject.service.CleaningListAction;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.CleaningServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class AddToOrderListCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets the cleaning id and quantity from the request.
     * Add cleaning to the order list, calculate total sum.
     * Returns router to the same page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see CleaningServiceImpl#findCleaningById(long)
     * @see CleaningListAction#addItem(List, Cleaning, int)
     * @see CleaningListAction#calculateTotalSum(List)
     */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        CleaningServiceImpl cleaningService = new CleaningServiceImpl();
        CleaningListAction action = new CleaningListAction();
        String pagePath = (String) content.getSessionAttribute(ConstantName.ATTRIBUTE_PAGE_PATH);
        String start = content.getRequestParameter(ConstantName.PARAMETER_PAGE_START);
        List<CleaningItem> cleaningList =
                (List<CleaningItem>) content.getSessionAttribute(ConstantName.ATTRIBUTE_ORDER_LIST);
        Long itemId = Long.valueOf(content.getRequestParameter(ConstantName.ATTRIBUTE_CLEANING_ID));
        Integer quantity = Integer.valueOf(content.getRequestParameter(ConstantName.ATTRIBUTE_ITEM_QUANTITY_IN_ORDER));
        try {
            Optional<Cleaning> cleaningOptional = cleaningService.findCleaningById(itemId);
            if (cleaningOptional.isPresent()) {
                Cleaning cleaning = cleaningOptional.get();
                action.addItem(cleaningList, cleaning, quantity);
                BigDecimal totalSum = (action.calculateTotalSum(cleaningList));
                content.addSessionAttribute(ConstantName.ATTRIBUTE_TOTAL_ORDER_SUM, totalSum);
                content.addSessionAttribute(ConstantName.ATTRIBUTE_ORDER_LIST, cleaningList);
                content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                router.setPagePath(pagePath);
      //          router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_SHOW_CLEANING));
                router.setType(RouteType.FORWARD);
            } else {
                content.addRequestAttribute(ConstantName.ATTRIBUTE_ADD_TO_ORDER_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_EMPTY_ORDER));
                content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_SHOW_CLEANING));
                router.setType(RouteType.FORWARD);
            }
        } catch (ServiceException e) {
            logger.error("Error while adding cleaning to the order list", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
            router.setType(RouteType.REDIRECT);
        }
        return router;
    }
}

package com.epam.cleaningProject.command.client;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.Order;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.OrderServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;

public class ShowClientOrdersCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Shows all client orders.
     * Sets the session attribute to show orders and
     * returns router to the orders page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see OrderServiceImpl#findAllClientOrders(Long)
      */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        OrderServiceImpl orderService = new OrderServiceImpl();
        List<Order> orderList;
        User user = (User) content.getSessionAttribute(ConstantName.ATTRIBUTE_USER);
        String start = content.getRequestParameter(ConstantName.PARAMETER_PAGE_START);
        Long id = user.getUserId();
        try {
            orderList = orderService.findAllClientOrders(id);
            if (!orderList.isEmpty()) {
                content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                content.addSessionAttribute(ConstantName.ATTRIBUTE_CLIENT_ORDER_LIST, orderList);
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLIENT_ORDERS));
            } else {
                content.addSessionAttribute(ConstantName.ATTRIBUTE_START, start);
                content.addRequestAttribute(ConstantName.ATTRIBUTE_SHOW_ORDERS_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_SHOW_ORDER_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_CLIENT_ORDERS));
            }
        } catch (ServiceException e) {
            logger.error("Error while getting all client orders", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;


    }
}

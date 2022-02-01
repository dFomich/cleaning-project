package com.epam.cleaningProject.service.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.dao.DaoException;
import com.epam.cleaningProject.dao.EntityTransaction;
import com.epam.cleaningProject.dao.impl.CleanerDaoImpl;
import com.epam.cleaningProject.dao.impl.CleaningDaoImpl;
import com.epam.cleaningProject.dao.impl.ClientDaoImpl;
import com.epam.cleaningProject.dao.impl.OrderDaoImpl;
import com.epam.cleaningProject.entity.Cleaner;
import com.epam.cleaningProject.entity.CleaningItem;
import com.epam.cleaningProject.entity.Client;
import com.epam.cleaningProject.entity.Order;
import com.epam.cleaningProject.entity.OrderStatus;
import com.epam.cleaningProject.entity.PaymentType;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.CleaningListAction;
import com.epam.cleaningProject.service.OrderService;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.util.DataTimeParser;

public class OrderServiceImpl implements OrderService {
    private final static Logger logger = LogManager.getLogger();
    @Override
    public List<Order> findAllClientOrders(Long id) throws ServiceException {
        OrderDaoImpl orderDao = new OrderDaoImpl();
        CleaningDaoImpl cleaningDao = new CleaningDaoImpl();
        CleanerDaoImpl cleanerDao = new CleanerDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        List<Order> orderList;
        transaction.begin(orderDao, cleaningDao, cleanerDao);
        try {
            orderList = orderDao.findAllOrderByClientId(id);
            for (Order order : orderList) {
                List<CleaningItem> itemList = cleaningDao.findCleaningsInOrder(order.getId());
                Optional<Cleaner> cleaner = cleanerDao.findByOrderId(order.getId());
                if (cleaner.isPresent()) {
                    order.setCleaner(cleaner.get());
                }
                order.setCleaningList(itemList);
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Exception while committing transaction", e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return orderList;
    }

    @Override
    public List<Order> findAllCleanerOrders(Long id) throws ServiceException {
        OrderDaoImpl orderDao = new OrderDaoImpl();
        CleaningDaoImpl cleaningDao = new CleaningDaoImpl();
        ClientDaoImpl clientDao = new ClientDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        List<Order> orderList;
        transaction.begin(orderDao, cleaningDao, clientDao);
        try {
            orderList = orderDao.findAllOrderByCleanerId(id);
            for (Order order : orderList) {
                List<CleaningItem> itemList = cleaningDao.findCleaningsInOrder(order.getId());
                Optional<Client> client = clientDao.findByOrderId(order.getId());
                if (client.isPresent()) {
                    order.setClient(client.get());
                }
                order.setCleaningList(itemList);
            }
            Collections.reverse(orderList);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Exception while committing transaction", e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return orderList;
    }

    @Override
    public List<Order> findAllOrders() throws ServiceException {
        OrderDaoImpl orderDao = new OrderDaoImpl();
        CleaningDaoImpl cleaningDao = new CleaningDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        List<Order> orderList;
        transaction.begin(orderDao, cleaningDao);
        try {
            orderList = orderDao.findAll();
            for (Order order : orderList) {
                List<CleaningItem> itemList = cleaningDao.findCleaningsInOrder(order.getId());
                order.setCleaningList(itemList);
            }
            Collections.reverse(orderList);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Exception while committing transaction", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return orderList;
    }

    @Override
    public Map<Long, Long> createOrder(User user, String date, String paymentType, String comment,
                                       List<CleaningItem> orderCleaningList) throws ServiceException {
        DataTimeParser parser = new DataTimeParser();
        CleaningListAction action = new CleaningListAction();
        OrderDaoImpl orderDao = new OrderDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        Map<Long, Long> resultMap = new HashMap<>();
        Set<Long> cleanerIdSet = action.getCleanersId(orderCleaningList);
        long createdId;
        transaction.begin(orderDao);
        try {
            for (Long cleanerId : cleanerIdSet) {
                List<CleaningItem> subList = orderCleaningList.stream().filter((p) ->
                        p.getCleaning().getCleanerId() == cleanerId).collect(Collectors.toList());
                BigDecimal orderSumSub = action.calculateTotalSum(subList);
                Order order = new Order(orderSumSub, LocalDateTime.now(), parser.getTime(date), OrderStatus.NEW,
                        PaymentType.valueOf(paymentType.toUpperCase()), false, comment);
                orderDao.create(order);
                createdId = order.getId();
                resultMap.put(cleanerId, createdId);
                orderDao.linkOrderClient(user.getUserId(), createdId, subList.size());
                orderDao.linkOrderCleaner(cleanerId, createdId);
                for (CleaningItem item : subList) {
                    orderDao.linkOrderCleaning(item.getCleaning().getId(), createdId, item.getQuantity());
                }
                transaction.commit();
            }
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Exception while committing transaction", e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return resultMap;
    }
    @Override
    public boolean changeOrderStatus(long orderId, OrderStatus orderStatus) throws ServiceException {
        boolean updated;
        OrderDaoImpl orderDao = new OrderDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.beginNoTransaction(orderDao);
        try {
            updated = orderDao.updateOrderStatus(orderId, orderStatus);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while executing service", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return updated;
    }
    @Override
    public boolean changePaymentStatus(long orderId) throws ServiceException {
        boolean updated;
        OrderDaoImpl orderDao = new OrderDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(orderDao);
        try {
            orderDao.updateOrderStatus(orderId, OrderStatus.FULFILLED);
            updated = orderDao.updatePaymentStatus(orderId);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Exception while executing service", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return updated;
    }
}

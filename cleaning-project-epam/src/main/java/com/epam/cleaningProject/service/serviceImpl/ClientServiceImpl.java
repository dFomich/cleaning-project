package com.epam.cleaningProject.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.dao.DaoException;
import com.epam.cleaningProject.dao.EntityTransaction;
import com.epam.cleaningProject.dao.impl.ClientDaoImpl;
import com.epam.cleaningProject.entity.Client;
import com.epam.cleaningProject.service.ClientService;
import com.epam.cleaningProject.service.ServiceException;

public class ClientServiceImpl implements ClientService {
    private final static Logger logger = LogManager.getLogger();
    @Override
    public boolean updateClient(Client client) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        ClientDaoImpl clientDao = new ClientDaoImpl();
        boolean updated;
        transaction.beginNoTransaction(clientDao);
        try {
            updated = clientDao.update(client);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while updating client", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return updated;
    }
    @Override
    public List<Client> findBlockedClients() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        ClientDaoImpl clientDao = new ClientDaoImpl();
        List<Client> clientList;
        transaction.beginNoTransaction(clientDao);
        try {
            clientList = clientDao.findBlockedClients();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while getting all blocked clients", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return clientList;
    }
    @Override
    public Optional<Client> findById(Long clientId) throws ServiceException {
        Optional<Client> clientOptional;
        EntityTransaction transaction = new EntityTransaction();
        ClientDaoImpl clientDao = new ClientDaoImpl();
        transaction.beginNoTransaction(clientDao);
        try {
            clientOptional = clientDao.findById(clientId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while getting client", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return clientOptional;
    }
    @Override
    public List<Client> findAllClients() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        ClientDaoImpl clientDao = new ClientDaoImpl();
        List<Client> clientList;
        transaction.beginNoTransaction(clientDao);
        try {
            clientList = clientDao.findAll();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while getting all clients", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return clientList;
    }
    @Override
    public Optional<Client> findByCleaningId(Long cleaningId) throws ServiceException {
        Optional<Client> clientOptional;
        EntityTransaction transaction = new EntityTransaction();
        ClientDaoImpl clientDao = new ClientDaoImpl();
        transaction.beginNoTransaction(clientDao);
        try {
            clientOptional = clientDao.findByOrderId(cleaningId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while getting client", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return clientOptional;
    }
}

package com.epam.cleaningProject.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.dao.DaoException;
import com.epam.cleaningProject.dao.EntityTransaction;
import com.epam.cleaningProject.dao.impl.CleanerDaoImpl;
import com.epam.cleaningProject.entity.Cleaner;
import com.epam.cleaningProject.service.CleanerService;
import com.epam.cleaningProject.service.ServiceException;

public class CleanerServiceImpl implements CleanerService {
    private final static Logger logger = LogManager.getLogger();
    @Override
    public List<Cleaner> findAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        CleanerDaoImpl cleanerDao = new CleanerDaoImpl();
        List<Cleaner> cleaners;
        transaction.beginNoTransaction(cleanerDao);
        try {
            cleaners = cleanerDao.findAll();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while getting all cleaners", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return cleaners;
    }
    @Override
    public Optional<Cleaner> findCleanerById(Long cleanerId) throws ServiceException {
        Optional<Cleaner> cleanerOptional;
        EntityTransaction transaction = new EntityTransaction();
        CleanerDaoImpl cleanerDao = new CleanerDaoImpl();
        transaction.beginNoTransaction(cleanerDao);
        try {
            cleanerOptional = cleanerDao.findById(cleanerId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while getting a cleaner", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return cleanerOptional;
    }
    @Override
    public boolean updateCleaner(Cleaner cleaner) throws ServiceException {
           EntityTransaction transaction = new EntityTransaction();
        CleanerDaoImpl cleanerDao = new CleanerDaoImpl();
        boolean created;
        transaction.beginNoTransaction(cleanerDao);
        try {
            created = cleanerDao.update(cleaner);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while updating cleaner", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return created;
    }
    @Override
    public List<Cleaner> findBlockedCleaners() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        CleanerDaoImpl cleanerDao = new CleanerDaoImpl();
        List<Cleaner> cleaners;
        transaction.beginNoTransaction(cleanerDao);
        try {
            cleaners = cleanerDao.findBlockedCleaners();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Exception while getting all blocked cleaners", e);
            throw new ServiceException(e);
        } finally {
            transaction.endNoTransaction();
        }
        return cleaners;
    }
}

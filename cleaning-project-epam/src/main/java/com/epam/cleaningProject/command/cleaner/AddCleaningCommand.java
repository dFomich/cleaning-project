package com.epam.cleaningProject.command.cleaner;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.cleaningProject.command.Command;
import com.epam.cleaningProject.command.ConstantName;
import com.epam.cleaningProject.command.RequestContent;
import com.epam.cleaningProject.command.RouteType;
import com.epam.cleaningProject.command.Router;
import com.epam.cleaningProject.entity.User;
import com.epam.cleaningProject.service.ServiceException;
import com.epam.cleaningProject.service.serviceImpl.CleaningServiceImpl;
import com.epam.cleaningProject.util.ConfigurationManager;
import com.epam.cleaningProject.util.MessageManager;
import com.epam.cleaningProject.validator.DataValidator;

public class AddCleaningCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    /**
     * Gets name, price, quantity, cleaning type and description from the request.
     * Validates this values, if input data is not valid, returns router to the same page
     * with message about invalid input data.
     * Otherwise, creates and adds new cleaning to the database and redirects router to the same page.
     *
     * @param content an {@link RequestContent} object that
     *                contains the request the client has made
     *                of the servlet
     * @return a {@code Router} object
     * @see DataValidator#validateCleaningInputData(Map)
     * @see CleaningServiceImpl#addCleaning(Map, long)
     */
    @Override
    public Router execute(RequestContent content) {
        Router router = new Router();
        CleaningServiceImpl cleaningService = new CleaningServiceImpl();
        DataValidator validator = new DataValidator();
        Map<String, String> cleaningParameters = new HashMap<>();
        String name = content.getRequestParameter(ConstantName.PARAMETER_CLEANING_NAME.trim());
        String priceUnparsed = content.getRequestParameter(ConstantName.PARAMETER_CLEANING_PRICE).trim();
        String cleaningTypeUnparsed = content.getRequestParameter(ConstantName.PARAMETER_CLEANING_TYPE).trim();
        String quantityUnparsed = content.getRequestParameter(ConstantName.PARAMETER_CLEANING_QUANTITY).trim();
        String description = content.getRequestParameter(ConstantName.PARAMETER_CLEANING_DESCRIPTION).trim();
        User user = (User) content.getSessionAttribute(ConstantName.PARAMETER_USER);
        long id = user.getUserId();
        cleaningParameters.put(ConstantName.PARAMETER_CLEANING_NAME, name);
        cleaningParameters.put(ConstantName.PARAMETER_CLEANING_PRICE, priceUnparsed);
        cleaningParameters.put(ConstantName.PARAMETER_CLEANING_TYPE, cleaningTypeUnparsed);
        cleaningParameters.put(ConstantName.PARAMETER_CLEANING_QUANTITY, quantityUnparsed);
        cleaningParameters.put(ConstantName.PARAMETER_CLEANING_DESCRIPTION, description);
        validator.validateCleaningInputData(cleaningParameters);
        try {
            if (!cleaningParameters.containsValue(null) &&
                    !cleaningParameters.containsValue(ConstantName.ATTRIBUTE_EMPTY_VALUE)) {
                if (cleaningService.addCleaning(cleaningParameters, id)) {
                    router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ADD_CLEANING));
                    router.setType(RouteType.REDIRECT);
                } else {
                    content.addRequestAttribute(ConstantName.ATTRIBUTE_ADD_CLEANING_ERROR,
                            MessageManager.getProperty(ConstantName.MESSAGE_ADD_CLEANING_ERROR));
                    router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ADD_CLEANING));
                }
            } else {
                content.addSessionAttribute(ConstantName.ATTRIBUTE_VALIDATED_MAP, cleaningParameters);
                content.addRequestAttribute(ConstantName.ATTRIBUTE_VALIDATE_CLEANING_ERROR,
                        MessageManager.getProperty(ConstantName.MESSAGE_VALIDATE_CLEANING_ERROR));
                router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ADD_CLEANING));
            }
        } catch (ServiceException e) {
            logger.error("Error adding cleaning", e);
            router.setPagePath(ConfigurationManager.getProperty(ConstantName.JSP_ERROR));
        }
        return router;
    }
}

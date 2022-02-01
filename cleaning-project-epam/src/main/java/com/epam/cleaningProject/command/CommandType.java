package com.epam.cleaningProject.command;

import com.epam.cleaningProject.command.admin.ChangeUserStatusCommand;
import com.epam.cleaningProject.command.admin.SendEmailCommand;
import com.epam.cleaningProject.command.admin.ShowAllCleanersCommand;
import com.epam.cleaningProject.command.admin.ShowAllClientsCommand;
import com.epam.cleaningProject.command.admin.ShowBlockedCleanersCommand;
import com.epam.cleaningProject.command.admin.ShowBlockedClientsCommand;
import com.epam.cleaningProject.command.admin.ShowOrdersCommand;
import com.epam.cleaningProject.command.cleaner.AddCleaningCommand;
import com.epam.cleaningProject.command.cleaner.ChangeCleaningStatusCommand;
import com.epam.cleaningProject.command.cleaner.ChangeOrderStatusCommand;
import com.epam.cleaningProject.command.cleaner.ConfirmPaymentCommand;
import com.epam.cleaningProject.command.cleaner.EditCleanerProfileCommand;
import com.epam.cleaningProject.command.cleaner.GoCleanerProfileCommand;
import com.epam.cleaningProject.command.cleaner.GoToAddCleaning;
import com.epam.cleaningProject.command.cleaner.ShowCleanerCleaningCommand;
import com.epam.cleaningProject.command.cleaner.ShowCleanerOrdersCommand;
import com.epam.cleaningProject.command.cleaning.EditCleaningCommand;
import com.epam.cleaningProject.command.cleaning.GoCleaningProfileCommand;
import com.epam.cleaningProject.command.cleaning.ShowCatalogCommand;
import com.epam.cleaningProject.command.client.CancelOrderCommand;
import com.epam.cleaningProject.command.client.EditClientProfileCommand;
import com.epam.cleaningProject.command.client.GoClientProfileCommand;
import com.epam.cleaningProject.command.client.ShowClientOrdersCommand;
import com.epam.cleaningProject.command.common.AddCleanerCommand;
import com.epam.cleaningProject.command.common.ChangePasswordCommand;
import com.epam.cleaningProject.command.common.GoToCabinetCommand;
import com.epam.cleaningProject.command.common.GoToChangePasswordCommand;
import com.epam.cleaningProject.command.common.LanguageCommand;
import com.epam.cleaningProject.command.common.LoginCommand;
import com.epam.cleaningProject.command.common.LogoutCommand;
import com.epam.cleaningProject.command.common.MainCommand;
import com.epam.cleaningProject.command.common.RecoverPasswordCommand;
import com.epam.cleaningProject.command.common.RegistrationCommand;
import com.epam.cleaningProject.command.common.UploadImageCommand;
import com.epam.cleaningProject.command.order.AddToOrderListCommand;
import com.epam.cleaningProject.command.order.ClearOrderListCommand;
import com.epam.cleaningProject.command.order.GoToOrderCommand;
import com.epam.cleaningProject.command.order.GoToOrderPreviewCommand;
import com.epam.cleaningProject.command.order.OrderCommand;
import com.epam.cleaningProject.command.order.RemoveFromOrderListCommand;

public enum CommandType {

    // Common commands
    CHANGE_LANGUAGE {{
        this.command = new LanguageCommand();
    }},
    REGISTRATION {{
        this.command = new RegistrationCommand();
    }},
    LOGIN {{
        this.command = new LoginCommand();
    }},
    RECOVER_PASSWORD {{
        this.command = new RecoverPasswordCommand();
    }},
    ADD_CLEANER {{
        this.command = new AddCleanerCommand();
    }},
    GO_TO_MAIN {{
        this.command = new MainCommand();
    }},
    SHOW_CATALOG {{
        this.command = new ShowCatalogCommand();
    }},


    //for users
    UPLOAD_IMAGE {{
        this.command = new UploadImageCommand();
    }},
    LOGOUT {{
        this.command = new LogoutCommand();
    }},
    CHANGE_PASSWORD {{
        this.command = new ChangePasswordCommand();
    }},
    GO_TO_CABINET {{
        this.command = new GoToCabinetCommand();
    }},
    GO_TO_CHANGE_PASSWORD {{
        this.command = new GoToChangePasswordCommand();
    }},

    //Admin commands
    SHOW_CLIENTS {{
        this.command = new ShowAllClientsCommand();
    }},
    SHOW_CLEANERS {{
        this.command = new ShowAllCleanersCommand();
    }},
    SHOW_ORDERS {{
        this.command = new ShowOrdersCommand();
    }},
    SEND_EMAIL {{
        this.command = new SendEmailCommand();
    }},
    CHANGE_USER_STATUS {{
        this.command = new ChangeUserStatusCommand();
    }},
    SHOW_BLOCKED_CLEANERS {{
        this.command = new ShowBlockedCleanersCommand();
    }},
    SHOW_BLOCKED_CLIENTS {{
        this.command = new ShowBlockedClientsCommand();
    }},


    //Client commands
    CONFIRM_ORDER {{
        this.command = new OrderCommand();
    }},
    ADD_TO_ORDER_LIST {{
        this.command = new AddToOrderListCommand();
    }},
    REMOVE_FROM_ORDER_LIST {{
        this.command = new RemoveFromOrderListCommand();
    }},
    GO_TO_ORDER_PREVIEW {{
        this.command = new GoToOrderPreviewCommand();
    }},
    CLEAR_ORDER_LIST {{
        this.command = new ClearOrderListCommand();
    }},
    GO_CLIENT_PROFILE {{
        this.command = new GoClientProfileCommand();
    }},
    EDIT_CLIENT_PROFILE {{
        this.command = new EditClientProfileCommand();
    }},
    SHOW_CLIENT_ORDERS {{
        this.command = new ShowClientOrdersCommand();
    }},
    CANCEL_ORDER {{
        this.command = new CancelOrderCommand();
    }},
    GO_TO_ORDER {{
        this.command = new GoToOrderCommand();
    }},

    //Cleaner commands
    ADD_CLEANING {{
        this.command = new AddCleaningCommand();
    }},
    SHOW_CLEANER_ORDERS {{
        this.command = new ShowCleanerOrdersCommand();
    }},
    EDIT_CLEANER_PROFILE {{
        this.command = new EditCleanerProfileCommand();
    }},
    GO_CLEANER_PROFILE {{
        this.command = new GoCleanerProfileCommand();
    }},
    EDIT_CLEANING {{
        this.command = new EditCleaningCommand();
    }},
    GO_CLEANING_PROFILE {{
        this.command = new GoCleaningProfileCommand();
    }},
    SHOW_CLEANER_CLEANINGS {{
        this.command = new ShowCleanerCleaningCommand();
    }},
    CHANGE_ORDER_STATUS {{
        this.command = new ChangeOrderStatusCommand();
    }},
    CHANGE_CLEANING_STATUS {{
        this.command = new ChangeCleaningStatusCommand();
    }},
    CONFIRM_PAYMENT {{
        this.command = new ConfirmPaymentCommand();
    }},
    GO_TO_ADD_CLEANING {{
        this.command = new GoToAddCleaning();
    }};


    Command command;
    public Command getCurrentCommand() {
        return command;
    }

}

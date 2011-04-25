package org.push2android;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 */


public class SendServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SendServlet.class.getName());

    private String LOGIN_REQUIRED_STATUS = "LOGIN_REQUIRED";

    private String LOGGED_IN_STATUS = "LOGGED_IN";


    /**
     * @see HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
            response.getWriter().println(LOGGED_IN_STATUS);
        } else {
            response.getWriter().println(LOGIN_REQUIRED_STATUS);
        }


    }


}

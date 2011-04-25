package org.push2android;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 *
 * Servlet used to authenticate the user with its Google Account from the Chrome extension
 */
public class AuthServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(AuthServlet.class.getName());

    /**
     * @see HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String stateUrl = request.getParameter("stateUrl");

        if (request.getParameter("completed") != null) {

            response.getWriter().print("<meta http-equiv=\"refresh\" content=\"0;url=" + stateUrl + "\">");

        } else {

            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();

            response.sendRedirect(userService.createLoginURL("https://push2android.appspot.com/login?completed=true&stateUrl=" + URLEncoder.encode(stateUrl, "UTF-8")));

        }
    }
} 
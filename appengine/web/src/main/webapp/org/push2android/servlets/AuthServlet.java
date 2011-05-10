/**
 *
 *  Copyright 2011 Jérémy Hérault
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


package org.push2android.servlets;

import com.google.appengine.api.oauth.OAuthService;
import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.push2android.Status;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 *         <p/>
 *         Servlet used to authenticate the user with its Google Account from the Chrome extension
 */
public class AuthServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(AuthServlet.class.getName());

    /**
     * @see HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String ID = request.getParameter("ID");
        String caller = request.getParameter("caller");
        String back_url = request.getParameter("back_url");

        log.info("ID: " + ID);
        log.info("Caller: " + caller);
        log.info("Back URL: " + back_url);

        if (request.getParameter("completed") != null) {

            response.getWriter().print("<meta http-equiv=\"refresh\" content=\"0;url=" + back_url + "\">");

        } else {

            UserService userService = UserServiceFactory.getUserService();

            response.sendRedirect(userService.createLoginURL("https://push2android.appspot.com/login?completed=true&stateUrl=" + URLEncoder.encode(back_url, "UTF-8")));
        }
    }

    /**
     * copied from Chrome To Phone sources: http://code.google.com/p/chrometophone/source/browse/tags/2.2.0/appengine/src/com/google/android/chrometophone/server/RegisterServlet.java
     * Get the user using the UserService.
     * <p/>
     * If not logged in, return an error message.
     *
     * @return user, or null if not logged in.
     * @throws IOException
     */
    public static User checkUser(HttpServletRequest req, HttpServletResponse resp,
                                 boolean errorIfNotLoggedIn) throws IOException {
        // Is it OAuth ?
        User user = null;
        OAuthService oauthService = OAuthServiceFactory.getOAuthService();
        try {
            user = oauthService.getCurrentUser();
            if (user != null) {
                log.info("Found OAuth user " + user);
                return user;
            }
        } catch (Throwable t) {
            user = null;
        }

        UserService userService = UserServiceFactory.getUserService();
        user = userService.getCurrentUser();
        if (user == null && errorIfNotLoggedIn) {
            // TODO: redirect to OAuth/user service login, or send the URL
            // TODO: 401 instead of 400
            resp.setStatus(400);
            resp.getWriter().println(Status.NOT_LOGGED);
        }
        return user;
    }
} 
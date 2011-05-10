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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.oauth.OAuthService;
import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.push2android.DeviceInfo;
import org.push2android.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 *         Servlet use by the Android Client to register the device and the associated user account
 */
public class RegisterServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get registration id,

        User user = checkUser(req,resp,true);

        String deviceRegistrationId = req.getParameter("deviceRegistrationID");
        if (deviceRegistrationId == null || "".equals(deviceRegistrationId.trim())) {
            resp.setStatus(400);
            resp.getWriter().println(Status.ERROR + "(Must specify devregid)");
            log.severe("Missing registration id ");
            return;
        }

        String deviceId = req.getParameter("deviceId");

        String suffix =
                (deviceId != null ? "#" + Long.toHexString(Math.abs(deviceId.hashCode())) : "");
        Key key = KeyFactory.createKey(DeviceInfo.class.getSimpleName(),
                user.getEmail() + suffix);


    }

    /**
     * copied from Chrome To Phone source
     * Get the user using the UserService.
     *
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


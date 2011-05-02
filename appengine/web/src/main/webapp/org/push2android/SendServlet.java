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

package org.push2android;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 */


public class SendServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SendServlet.class.getName());

    /**
     * @see HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String ID = request.getParameter("ID");
        String caller = request.getParameter("caller");
        String notification = request.getParameter("notification");

        log.info("ID: " + ID);
        log.info("Caller: " + caller);
        log.info("Notification :" + notification);

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
            response.getWriter().println(Status.LOGGED);

            // get devices infos for this Google account
            // persist data
            // send to C2DM webservice to push the notification available

            //maybe retrieve SENT Status instead of Logged (if sent then also Logged )

        } else {
            response.getWriter().println(Status.NOT_LOGGED);
        }


    }
}

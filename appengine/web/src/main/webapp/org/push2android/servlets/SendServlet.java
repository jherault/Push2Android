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

import com.google.android.c2dm.server.C2DMessaging;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.push2android.DeviceInfo;
import org.push2android.Status;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 *         Servlet used to receive notifications from third party and to push it in the cloud to C2DM
 */
public class SendServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SendServlet.class.getName());

    /**
     * @see HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String ID = request.getParameter("ID");
        String caller = request.getParameter("caller");
        String notification = request.getParameter("notification");

        log.info("ID: " + ID);
        log.info("Caller: " + caller);
        log.info("Notification :" + notification);

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {

            // TODO
            // get devices infos for this Google account
            // persist data
            // send to C2DM webservice to push the notification available

            C2DMessaging push = C2DMessaging.get(getServletContext());

            PersistenceManager pm = C2DMessaging.getPMF(getServletContext()).getPersistenceManager();

            List<DeviceInfo> devicesInfo = DeviceInfo.getDeviceInfoForUser(pm, user.getEmail());

            if (!devicesInfo.isEmpty()) {

                boolean ok = false;
                boolean res = false;

                for (DeviceInfo deviceInfo : devicesInfo) {

                    try {

                        res = sendToDevice(push, deviceInfo, notification);

                    } catch (IOException ex) {

                        if ("NotRegistered".equals(ex.getMessage()) ||
                                "InvalidRegistration".equals(ex.getMessage())) {
                            // Prune device, it no longer works
                            pm.deletePersistent(deviceInfo);
                        } else {
                            throw ex;
                        }
                    }
                    if (res) {

                        log.info("Notification send to device! collapse_key: " + notification.hashCode());
                        ok = true;
                    }
                }

                //notification send to at least one device
                if (ok) {

                    response.getWriter().println(Status.SENT);
                } else {

                    response.getWriter().println(Status.ERROR);
                }

            } else {

                response.getWriter().print(Status.NO_DEVICE_REGISTERED);
            }

        } else {
            response.getWriter().println(Status.NOT_LOGGED);
        }
    }

    private boolean sendToDevice(C2DMessaging push, DeviceInfo deviceInfo, String notification) throws IOException {

        boolean res = false;

        Map<String, String[]> map = new HashMap<String, String[]>();

        map.put("data.app", new String[]{"Android_Notification"});

        map.put("data.notification", new String[]{notification});

        res = push.sendNoRetry(deviceInfo.getDeviceRegistrationID(), "" + notification.hashCode(), map, true);

        return res;
    }
}

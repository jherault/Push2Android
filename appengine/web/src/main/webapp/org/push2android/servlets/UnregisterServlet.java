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
import org.push2android.DeviceInfo;
import org.push2android.Status;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 *         Servlet use by the Android Client to unregister the device and the associated user account
 */
public class UnregisterServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(UnregisterServlet.class.getName());

    /**
     * a good copy/paste from Chrome to Phone sources with few changes
     *
     * @see HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String deviceRegistrationID = req.getParameter("deviceRegistrationID");
        if (deviceRegistrationID == null) {
            resp.setStatus(400);
            resp.getWriter().println(Status.ERROR + " (Must specify deviceregistrationid)");
            return;
        }


        User user = AuthServlet.checkUser(req, resp, true);

        if (user != null) {
            PersistenceManager pm =
                    C2DMessaging.getPMF(getServletContext()).getPersistenceManager();
            try {
                List<DeviceInfo> registrations = DeviceInfo.getDeviceInfoForUser(pm, user.getEmail());

                if (!registrations.isEmpty()) {

                    for (DeviceInfo deviceInfo : registrations) {
                        if (deviceInfo.getDeviceRegistrationID().equals(deviceRegistrationID)) {
                            pm.deletePersistent(deviceInfo);
                            // Keep looping in case of duplicates
                        }
                    }

                    resp.getWriter().println(Status.OK);
                } else {

                    resp.getWriter().println(Status.NO_DEVICE_REGISTERED);
                }


            } catch (JDOObjectNotFoundException e) {
                resp.setStatus(400);
                resp.getWriter().println(Status.ERROR + " (User unknown)");
                log.warning("User unknown");
            } catch (Exception e) {
                resp.setStatus(500);
                resp.getWriter().println(Status.ERROR + " (Error unregistering device)");
                log.warning("Error unregistering device: " + e.getMessage());
            } finally {
                pm.close();
            }
        } else {
            resp.getWriter().println(Status.NOT_LOGGED + " (Not authorized)");
        }
    }


}

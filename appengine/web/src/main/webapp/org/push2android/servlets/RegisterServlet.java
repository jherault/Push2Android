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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import org.push2android.DeviceInfo;
import org.push2android.Status;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
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

    /**
     *
     * @see HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = AuthServlet.checkUser(req, resp, true);

        if (user != null) {

            String deviceRegistrationId = req.getParameter("deviceRegistrationID");
            if (deviceRegistrationId == null || "".equals(deviceRegistrationId.trim())) {
                resp.setStatus(400);
                resp.getWriter().println(Status.ERROR + "(Must specify deviceregistrationid)");
                log.severe("Missing registration id ");
                return;
            }

            String deviceId = req.getParameter("deviceId");

            String suffix =
                    (deviceId != null ? "#" + Long.toHexString(Math.abs(deviceId.hashCode())) : "");
            Key key = KeyFactory.createKey(DeviceInfo.class.getSimpleName(),
                    user.getEmail() + suffix);


            PersistenceManager pm =
                    C2DMessaging.getPMF(getServletContext()).getPersistenceManager();
            try {
                DeviceInfo device = null;
                try {
                    device = pm.getObjectById(DeviceInfo.class, key);
                } catch (JDOObjectNotFoundException e) {
                }
                if (device == null) {
                    device = new DeviceInfo(key, deviceRegistrationId);
                } else {
                    // update registration id
                    device.setDeviceRegistrationID(deviceRegistrationId);
                }

                pm.makePersistent(device);

                resp.getWriter().print(Status.OK);
            } catch (Exception e) {
                resp.setStatus(500);
                resp.getWriter().print(Status.ERROR + " error during registration");

            } finally {

                pm.close();
            }
        }


    }
}


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

import com.google.appengine.api.datastore.Key;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.ArrayList;
import java.util.List;


/**
 * Class used to register a device for an account.
 * One user can have more than one device, and a device can be used by more than one user
 *
 * (largely inspired from Chrome To Phone: http://code.google.com/p/chrometophone/source/browse/tags/2.2.0/appengine/src/com/google/android/chrometophone/server/DeviceInfo.java )
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DeviceInfo {


    /**
     * Format: user.email#device.id
     */
    @PrimaryKey
    @Persistent
    private Key key;

    /**
     * Device registration ID
     */
    @Persistent
    private String deviceRegistrationID;

    /**
     * Constructor
     * @param key

     * @param deviceRegistrationID
     */
    public DeviceInfo(Key key, String deviceRegistrationID){

        this.key = key;
        this.deviceRegistrationID = deviceRegistrationID;
    }

     public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getDeviceRegistrationID() {
        return deviceRegistrationID;
    }

    public void setDeviceID(String deviceRegistrationID) {
        this.deviceRegistrationID = deviceRegistrationID;
    }

    /**
     * Static method to get all devices for a user
     * @param pm the persistence manager
     * @param useremail the user account
     * @return the list of DeviceInfo for the given user account
     */
    public static List<DeviceInfo> getDeviceInfoForUser(PersistenceManager pm, String useremail){

        List<DeviceInfo> devicesInfo = new ArrayList<DeviceInfo>();

        Query query = pm.newQuery(DeviceInfo.class);
        query.setFilter("key >= '" + useremail + "' && key < '" + useremail + "$'");

        List<DeviceInfo> tmp = (List<DeviceInfo>) query.execute();

        devicesInfo.addAll(tmp);

        query.closeAll();

        return devicesInfo;
    }

}

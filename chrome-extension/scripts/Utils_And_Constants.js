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

const STATUS = {

    NOT_LOGGED: "NOT_LOGGED",
    LOGGED_IN: "LOGGED",
    UNKNOWN_APP: "UNKNOWN_APP",
    NO_DEVICE_REGISTERED: "NO_DEVICE_REGISTERED"
}

const PUSH2ANDROID_URL = "https://push2android.appspot.com/";

const CALLER = "browser-google-chrome";

const ISSUE_TRACKER_URL = "https://github.com/jherault/Push2Android/issues/new";

const ISSUE_TRACKER_MESSAGE = "Please copy the error and paste it on Github (<a href='" + ISSUE_TRACKER_URL + "'>" + ISSUE_TRACKER_URL + "</a>)";


var Operation = {

    /**
     * Function sending the request to the push2android server side, putting the message in the cloud for the device
     * @param ID The Push2Android client app ID
     * @param notification message to send to the Android Device (should be shorten than 1000 characters)
     * @param success_handler callback on success
     * @param error_handler callback if an error occurs
     *
     */
    send: function(ID, notification, success_handler, error_handler) {

        $.ajax({
            url: PUSH2ANDROID_URL + "send?caller=" + CALLER + "&ID=" + ID + "&notification=" + notification,
            async:false,
            success: function(msg) {
                success_handler(msg);
            },
            error:  function(jqXHR, textStatus, errorThrown) {

                return error_handler(textStatus, errorThrown);
            }
        });
    },
    /**
     * Create the url to open in an other tab or window to log in, and go back to your "login success page"
     * @param back_url - the "login success page"
     * return the url to open in a new tab or in a new window
     */
    login: function(back_url) {

        return PUSH2ANDROID_URL + "login?caller=" + CALLER + "&back_url=" + back_url;
    }
};





/**
 *
 * @author Jeremy Herault - jeremy.herault@gmail.com
 *
 */
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

//TODO get the list from appengine
const GOOGLE_MAIL = {id:1, appname:"Gmail"};
const GOOGLE_CALENDAR = {id:2, appname:"Agenda"};

var apps = [GOOGLE_MAIL, GOOGLE_CALENDAR];

const NOT_LOGGED_STATUS = "NOT_LOGGED";
const LOGGED_IN_STATUS = "LOGGED";

const PUSH2ANDROID_URL = "https://push2android.appspot.com/";

const CALLER = "browser-google-chrome";

const ISSUE_TRACKER_URL = "https://github.com/jherault/Push2Android/issues/new";

const ISSUE_TRACKER_MESSAGE = "Please copy the error and paste it on Github (<a href='" + ISSUE_TRACKER_URL + "'>" + ISSUE_TRACKER_URL + "</a>) : ";

const BACK_URL = encodeURIComponent(chrome.extension.getURL('html/options.html'));


var Operation = {

    send: "send",
    login: "login"
};





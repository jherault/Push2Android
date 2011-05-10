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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Logger;

/**
 * @author Jeremy Herault
 * 404 Servlet, printing a specific message with the given unknown url
 */
public class NotFoundServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(NotFoundServlet.class.getName());

    /**
     * @see HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String notFoundPage = (String)request.getAttribute("javax.servlet.error.request_uri");

        String nfpTemplate = "";

        File f = new File(this.getServletContext().getRealPath("/templates/nfp.html"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

        String tmp = "";

        while ((tmp = reader.readLine()) != null){

            nfpTemplate += tmp + "\n";
        }

        nfpTemplate = nfpTemplate.replace("#nfp", notFoundPage);

        response.getWriter().print(nfpTemplate);
    }
}

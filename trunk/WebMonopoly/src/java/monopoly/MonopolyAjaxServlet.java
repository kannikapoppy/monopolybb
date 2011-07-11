/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirsh
 */
public class MonopolyAjaxServlet extends HttpServlet {
   
    public static final String CONTENT_TYPE_JSON = "application/json";
    private static int i = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        response.setContentType(CONTENT_TYPE_JSON);
        response.setHeader("pragma-", "no-cache");
        dispatcher = getServletContext().getRequestDispatcher("/state" + i + ".json");
        dispatcher.forward(request, response);
        i = ++i % 5;
    }
}



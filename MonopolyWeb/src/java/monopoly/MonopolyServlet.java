/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirsh
 */
public class MonopolyServlet extends HttpServlet
{
    public static final int SERVER_FAILED = -1;
    public static final String CONTENT_TYPE_JSON = "application/json";
    private static int i = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //String action = request.getParameter("action");
        replyServerFailed("hello", request, response);
//        WebClient client = getClient(request, true);
//        if(client.getClientState() == WebClient.ClientState.Init)
//        {
//            if(client.initClient() == SERVER_FAILED)
//                replyServerFailed(client.getServerMessage(), request, response);
//        }
//        if(client.getClientState() == WebClient.ClientState.Creating)
//        {
//
//        }
//        RequestDispatcher dispatcher;
//        response.setContentType(CONTENT_TYPE_JSON);
//        response.setHeader("pragma-", "no-cache");
//        dispatcher = getServletContext().getRequestDispatcher("/state" + i + ".json");
//        dispatcher.forward(request, response);
//        i = ++i % 5;
    }

    private void HandleAction(String action, HttpServletResponse response) throws ServletException, IOException
    {
        if (action.compareTo(Utils.ACTION_CREATE) == 0)
        {
            
        }
        else if (action.compareTo(Utils.ACTION_JOIN) == 0)
        {

        }
        else
        {

        }
    }

    private void replyServerFailed(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //RequestDispatcher dispatcher;
        
        //request.setAttribute("error_message", errorMessage);
        InputStream stream = getServletContext().getResourceAsStream("/json/server_failed.json");
        String contentStr = Utils.convertStreamToString(stream);
        contentStr = contentStr.replace(Utils.ERROR_PLACEHOLDER, errorMessage);
        sendReplyToClient(contentStr, request, response);

        //dispatcher = getServletContext().getRequestDispatcher("/json/server_failed.json");
        //dispatcher.forward(request, response);
    }

    private void sendReplyToClient(String replyContent, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setHeader("pragma-", "no-cache");
        response.setContentLength(replyContent.length());
        response.getWriter().print(replyContent);
    }

    private WebClient getClient(HttpServletRequest request, boolean shouldCreate)
    {
        WebClient webClient = (WebClient)request.getSession().getAttribute("client");
        if(webClient == null && shouldCreate)
        {
            webClient = new WebClient();
            request.getSession(true).setAttribute("client", webClient);
        }
        return webClient;
    }
}



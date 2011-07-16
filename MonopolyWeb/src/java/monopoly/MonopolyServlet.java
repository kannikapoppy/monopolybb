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
    private HttpServletRequest currentRequest;
    private HttpServletResponse currentResponse;
    private WebClient client;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String action = request.getParameter("action");
        currentRequest = request;
        currentResponse = response;
        HandleAction(action);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        currentRequest = request;
        currentResponse = response;
        HandleAction(action);//, request, response);
//        RequestDispatcher dispatcher;
//        response.setContentType(CONTENT_TYPE_JSON);
//        response.setHeader("pragma-", "no-cache");
//        dispatcher = getServletContext().getRequestDispatcher("/state" + i + ".json");
//        dispatcher.forward(request, response);
//        i = ++i % 5;
    }

    private void HandleAction(String action) throws ServletException, IOException
    {
        client = getClient(true);
        client.setContext(getServletContext());

        if (action.compareTo(Utils.ACTION_CREATE) == 0)
        {
            String gameName = currentRequest.getParameter("gameName");
            String playersNum = currentRequest.getParameter("playersNum");
            String humansNum = currentRequest.getParameter("humansNum");
            String autoDice = currentRequest.getParameter("autoDice");
            int status = client.createGame(gameName, playersNum, humansNum, autoDice);
            if (status == SERVER_FAILED)
                replyServerFailed(client.getServerMessage());
            else if (status < 0)
                replyServerPrompt("/json/create_game.json", true);
            else
                replyServerPrompt("/json/join_game.json", false);
        }
        else if (action.compareTo(Utils.ACTION_JOIN) == 0)
        {
            String playerName = currentRequest.getParameter("playerName");
            int status = client.joinGame(playerName);
            if (status == SERVER_FAILED)
                replyServerFailed(client.getServerMessage());
            else if (status < 0)
                replyServerPrompt("/json/join_game.json", true);
            else
                replyWaiting();
        }
        else if (action.compareTo(Utils.GET_STATE) == 0)
        {
            // not initiated yet
            if(client.getClientState() == WebClient.ClientState.Init)
            {
                if(client.initClient() == SERVER_FAILED)
                    replyServerFailed(client.getServerMessage());
            }
            if(client.getClientState() == WebClient.ClientState.Creating)
            {
                replyServerPrompt("/json/create_game.json", false);
            }
            else if(client.getClientState() == WebClient.ClientState.Joining)
            {
                replyServerPrompt("/json/join_game.json", false);
            }
            else if(client.getClientState() == WebClient.ClientState.WaitingStart)
            {
                replyWaiting();
            }
            else if(client.getClientState() == WebClient.ClientState.Running)
            {
                sendReplyToClient(client.getServerMessage());
            }
            else
            {
                replyServerFailed(WebClient.SERVER_CONNECTION_ERROR_MSG);
            }
        }
    }

    private void replyServerFailed(String errorMessage) throws ServletException, IOException
    {
        //RequestDispatcher dispatcher;
        
        //request.setAttribute("error_message", errorMessage);
        InputStream stream = getServletContext().getResourceAsStream("/json/server_failed.json");
        String contentStr = Utils.convertStreamToString(stream);
        stream.close();
        contentStr = contentStr.replace(Utils.ERROR_PLACEHOLDER, errorMessage);
        sendReplyToClient(contentStr);

        //dispatcher = getServletContext().getRequestDispatcher("/json/server_failed.json");
        //dispatcher.forward(request, response);
    }

    private void replyServerPrompt(String jsonFile, boolean showError) throws ServletException, IOException
    {
        InputStream stream = getServletContext().getResourceAsStream(jsonFile);
        String contentStr = Utils.convertStreamToString(stream);
        stream.close();
        
        if (client.getServerMessage() != null && showError)
            contentStr = contentStr.replace(Utils.ERROR_PLACEHOLDER, client.getServerMessage());
        else
            contentStr = contentStr.replace(Utils.ERROR_PLACEHOLDER, "");

        sendReplyToClient(contentStr);
    }

    private void replyWaiting()  throws ServletException, IOException
    {
        InputStream stream = getServletContext().getResourceAsStream("/json/waiting.json");
        String contentStr = Utils.convertStreamToString(stream);
        stream.close();
        contentStr = contentStr.replace(Utils.WAITING_PLACEHOLDER, client.getWaitingText());

        sendReplyToClient(contentStr);
    }

    private void sendReplyToClient(String replyContent) throws ServletException, IOException
    {
        currentResponse.setContentType(CONTENT_TYPE_JSON);
        currentResponse.setHeader("pragma-", "no-cache");
        currentResponse.setStatus(currentResponse.SC_OK);
        currentResponse.setContentLength(replyContent.length());
        currentResponse.getWriter().print(replyContent);
        currentResponse.flushBuffer();
    }

    private WebClient getClient(boolean shouldCreate)
    {
        WebClient webClient = (WebClient)currentRequest.getSession().getAttribute("client");
        if(webClient == null && shouldCreate)
        {
            webClient = new WebClient();
            currentRequest.getSession(true).setAttribute("client", webClient);
        }
        return webClient;
    }
}



package servlet;


import Enums.RcCommand;
import Enums.Remote;
import remotes.AVRRC;
import remotes.LIRC;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


@WebServlet(name = "servlet.RCServlet")
public class RCServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    public RCServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Todo change to JSON
        response.setContentType("text/html");

        Remote remote = Remote.valueOf(request.getParameter("remote").toUpperCase(Locale.ENGLISH));

        switch (remote) {
            case TV:
                buildLirc(request, response, remote);
                break;
            case AVR:
                buildAvrrc(request, response);
                break;

        }
    }

    private void buildLirc(HttpServletRequest request, HttpServletResponse response, Remote remote){

        LIRC tv;

        RcCommand lircCommand = RcCommand.valueOf(request.getParameter("command").toUpperCase(Locale.ENGLISH));

        tv = new LIRC.Builder(response, lircCommand, remote).build();
        tv.sendCommand();

    }

    private void buildAvrrc(HttpServletRequest request, HttpServletResponse response) {

        AVRRC receiver;

        RcCommand avrCommand = RcCommand.valueOf(request.getParameter("command").toUpperCase(Locale.ENGLISH));

        if (avrCommand.equals(RcCommand.VOL_SET)) {

            int level = Integer.parseInt(request.getParameter("level"));

            receiver = new AVRRC.Builder(response, avrCommand).level(level).build();
            receiver.sendCommand();

        } else {

            receiver = new AVRRC.Builder(response, avrCommand).build();
            receiver.sendCommand();

        }
    }

//    private void LircSend(HttpServletRequest request, HttpServletResponse response, String remote) throws IOException {
//
//        String command = request.getParameter("command");
//
//        try {
//            LircClient lirc = getLircClient();
//
//            if (listContainsItem(lirc.getRemotes(), remote)) {
//
//                if (listContainsItem(lirc.getCommands(remote), command)) {
//
//                    lirc.sendIrCommand(remote, command, 1);
//
//                    response.getWriter().write(HttpServletResponse.SC_OK);
//
//                } else {
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cmd " + command + " does not exist");
//
//                }
//            } else {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Remote " + remote + " does not exist");
//
//            }
//        } catch (IOException ex) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Connection Fail");
//
//            throw new RuntimeException(ex.getMessage(), ex);
//        }
//    }
//
//    private boolean listContainsItem(List<String> list, String item) {
//        // Todo look into changing this to a Hashmap
//        return list.stream().anyMatch(s -> s.equalsIgnoreCase((item)));
//    }
//
//    private LircClient getLircClient() throws IOException {
//        return new TcpLircClient("localhost", 8765, true, 5000);
//    }
}



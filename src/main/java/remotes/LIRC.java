package remotes;

import Enums.RcCommand;
import Enums.Remote;
import org.harctoolbox.lircclient.LircClient;
import org.harctoolbox.lircclient.TcpLircClient;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LIRC {

    private final HttpServletResponse response;
    private final RcCommand lircCommand;
    private final Remote remote;
    private final int level;

    public static class Builder {

        // Required parameters
        private final HttpServletResponse response;
        private final RcCommand lircCommand;
        private final Remote remote;

        // Optional parameters - initialized to default values
        private int level = 0;

        public Builder(HttpServletResponse response, RcCommand lircCommand, Remote remote) {
            this.response = response;
            this.remote = remote;
            this.lircCommand = lircCommand;
        }

        public Builder level(int val) {
            level = val;
            return this;
        }

        public LIRC build() {
            return new LIRC(this);
        }
    }

    private LIRC(Builder builder) {
        response = builder.response;
        lircCommand = builder.lircCommand;
        remote = builder.remote;
        level = builder.level;
    }

    public void sendCommand(){

        try {
            LircClient lircClient = getLircClient();

            if (listContainsItem(lircClient.getRemotes(), remote.toString())) {

                if (listContainsItem(lircClient.getCommands(remote.toString()), lircCommand.toString())) {

                    lircClient.sendIrCommand(remote.toString(), lircCommand.toString(), 1);

                    response.getWriter().write(HttpServletResponse.SC_OK);

                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cmd " + lircCommand + " does not exist");

                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Remote " + remote + " does not exist");

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private boolean listContainsItem(List<String> list, String item) {
        // Todo look into changing this to a Hashmap
        return list.stream().anyMatch(s -> s.equalsIgnoreCase((item)));
    }

    private LircClient getLircClient() throws IOException {
        return new TcpLircClient("localhost", 8765, true, 5000);
    }
}

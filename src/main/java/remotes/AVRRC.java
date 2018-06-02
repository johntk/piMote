package remotes;

import Enums.RcCommand;
import io.theves.denon4j.DenonReceiver;

import javax.servlet.http.HttpServletResponse;

public class AVRRC {

    private final HttpServletResponse response;
    private final RcCommand avrCommand;
    private final int level;

    public static class Builder {

        // Required parameters
        private final HttpServletResponse response;
        private final RcCommand avrCommand;

        // Optional parameters - initialized to default values
        private int level = 0;

        public Builder(HttpServletResponse response, RcCommand avrCommand) {
            this.response = response;
            this.avrCommand = avrCommand;
        }

        public Builder level(int val) {
            level = val;
            return this;
        }

        public AVRRC build() {
            return new AVRRC(this);
        }
    }

    private AVRRC(Builder builder) {
        response = builder.response;
        avrCommand = builder.avrCommand;
        level = builder.level;
    }

    public void sendCommand(){

        try (DenonReceiver avr = new DenonReceiver("192.168.0.8", 23)) {

            avr.connect(1000);

            switch(avrCommand) {
                case ON:
                    powerOn(avr);
                    break;
                case VOL_UP:
                    volumeSlide(avr);
                    break;
                case VOL_DOWN:
                    volumeSlide(avr);
                    break;
                case VOL_SET:
                    volumeSet(avr);
                    break;
            }
        }
    }

    private void volumeSlide(DenonReceiver avr) {
        if(avrCommand.equals(RcCommand.VOL_UP)){
            avr.masterVolume().slideUp();
        }else{
            avr.masterVolume().slideDown();
        }
    }

    private void volumeSet(DenonReceiver avr) {
        avr.masterVolume().set(String.valueOf(level));
    }

    private void powerOn(DenonReceiver avr) {

        if (!avr.power().state().equals("ON")) {
            avr.power().toggle();
        }
    }
}


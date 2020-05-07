package event;

import java.util.EventObject;

public class AppEvent extends EventObject {

    public AppEvent(Object source) {
        super(source);
    }
}

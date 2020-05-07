package event;

import java.util.EventListener;

public interface AppListener<E extends AppEvent> extends EventListener {

    String helloWorld(E e);
}

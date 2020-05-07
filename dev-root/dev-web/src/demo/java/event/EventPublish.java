package event;

import java.util.ArrayList;
import java.util.List;

public class EventPublish {
    private static List<AppListener> listeners = new ArrayList<>();

    public void addEventListener(AppListener appListener){
        listeners.add(appListener);
    }

    public void publish(){
        addEventListener(new DefaultListener());
        addEventListener(new DefaultListener());
        addEventListener(new DefaultListener());

        DefaultEvent  defaultEvent = new DefaultEvent(this);
        for (AppListener listener : listeners) {
            listener.helloWorld(defaultEvent);
        }
    }



    public static void main(String[] args) {


        new EventPublish().publish();


    }

}

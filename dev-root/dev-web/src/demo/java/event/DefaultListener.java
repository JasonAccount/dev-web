package event;

public class DefaultListener implements AppListener<DefaultEvent> {

    @Override
    public String helloWorld(DefaultEvent appEvent) {

        System.out.println("hello");
        return "123123123";
    }
}

package ke.co.tech.webfluxmongodemo.reactorplayground;

public class CustomException extends Exception {

    private Throwable throwable;

    public CustomException(Throwable throwable) {
        this.throwable = throwable;
    }
}

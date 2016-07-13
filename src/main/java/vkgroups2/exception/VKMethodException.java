package vkgroups2.exception;

public class VKMethodException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4984597234059139374L;

    public VKMethodException() {
    }

    public VKMethodException(String message) {
        super(message);
    }

    public VKMethodException(Throwable cause) {
        super(cause);
    }

    public VKMethodException(String message, Throwable cause) {
        super(message, cause);
    }


}

package mg.itu.prom16.exception;

public class ReturnTypeNotRecognizedException extends Exception{
    public ReturnTypeNotRecognizedException(String TypeName) {
        super("return type not found : "+TypeName);
    }
}

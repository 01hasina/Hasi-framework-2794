package mg.itu.prom16.exception;

public class DuplicateUrlException extends Exception {
    
    public DuplicateUrlException(String nameUrlDuplicate, String verb) {
        super("Duplicate url exception "+ nameUrlDuplicate + "verb" + verb);
    }

}
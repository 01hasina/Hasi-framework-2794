package mg.itu.prom16.exception;

public class PackageNotFoundException extends Exception {
    public PackageNotFoundException(String PathPackage) {
        super("Package :" + PathPackage + " nom trouve");
    }
}

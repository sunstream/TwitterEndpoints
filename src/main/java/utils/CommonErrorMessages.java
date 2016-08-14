package utils;

public interface CommonErrorMessages {
    String NEW_LINE = System.getProperty("line.separator");
    default String byException(Exception e){
        return "Execution is interrupted by " + e.getClass() + ": " + e.getMessage() + NEW_LINE;
    }

    default String byException(Exception e, String details){
        return byException(e) + details;
    }

    default String byNotFound(String objectName){
        return "[" + objectName + "] was not found.";
    }
    default String byInvalidCount(String objectName){
        return "Invalid count of " + objectName;
    }

    default String byMismatch(String objectName){
        return "[" + objectName + "] mismatch.";
    }

    default String byNotFound(String objectName, String location){
        return "[" + objectName + "] was/were not found in [" + location + "].";
    }
}

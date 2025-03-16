package de.florianmichael.waybackauthlib;

public class InvalidRequestException extends Exception {
   public final String field4;
   public final String field5;
   public final String field6;

   public InvalidRequestException(String error) {
      super(error);
      this.field4 = error;
      this.field5 = null;
      this.field6 = null;
   }

   public InvalidRequestException(String error, String errorMessage, String cause) {
      super(error + ": " + errorMessage + " (" + cause + ")");
      this.field4 = error;
      this.field5 = errorMessage;
      this.field6 = cause;
   }
}

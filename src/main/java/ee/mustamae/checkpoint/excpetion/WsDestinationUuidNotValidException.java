package ee.mustamae.checkpoint.excpetion;

public class WsDestinationUuidNotValidException extends RuntimeException {

  public WsDestinationUuidNotValidException(final String message) {
    super(message);
  }
}

package nextstep.exception;

public class MemberAuthorizationWebException extends AuthorizationWebException {
    public MemberAuthorizationWebException(String expected, String actual, String context, String type) {
        super(expected, actual, context, type);
    }
}
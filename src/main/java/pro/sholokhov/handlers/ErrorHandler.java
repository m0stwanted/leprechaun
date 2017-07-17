package pro.sholokhov.handlers;

import static ratpack.jackson.Jackson.json;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.models.ErrorResponse;
import pro.sholokhov.server.exceptions.ResourceNotFoundException;
import ratpack.error.ServerErrorHandler;
import ratpack.handling.Context;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.util.HashMap;

@Singleton
public class ErrorHandler implements ServerErrorHandler {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void error(Context context, Throwable t) throws Exception {
        log.error("Error while processing request for path: {} with query: {}",
                context.getRequest().getPath(), context.getRequest().getQuery(), t);

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setCause(t.getMessage());

        if (t instanceof IllegalArgumentException || t instanceof MalformedURLException) {
            errResponse.setHttpStatus(HttpErrorCode.BAD_REQUEST);
        }
        else if (t instanceof ResourceNotFoundException) {
            errResponse.setHttpStatus(HttpErrorCode.NOT_FOUND);
        }
        else {
            errResponse.setHttpStatus(HttpErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (errResponse.getCause() == null || errResponse.getCause().isEmpty()) {
            switch (HttpErrorCode.getByCode(errResponse.getHttpStatus())) {
                case BAD_REQUEST:
                    errResponse.setCause("Bad request");
                    break;
                case NOT_FOUND:
                    errResponse.setCause("Resource not found");
                    break;
                case CONFLICT:
                    errResponse.setCause("Request violates constraints for some entity");
                    break;
                case INTERNAL_SERVER_ERROR:
                    errResponse.setCause("Internal server error");
                    break;
                default:
                    errResponse.setCause("Unknown error");
            }
        }

        context.getResponse().status(errResponse.getHttpStatus());
        context.render(json(errResponse));
    }

    public enum HttpErrorCode {
        BAD_REQUEST(400),
        NOT_FOUND(404),
        CONFLICT(409),
        INTERNAL_SERVER_ERROR(500),
        UNKNOWN(-1);

        private final int code;
        private static HashMap<Integer, HttpErrorCode> lookup = new HashMap<>();

        static {
            for (HttpErrorCode c : HttpErrorCode.values()) {
                lookup.put(c.getCode(), c);
            }
        }

        HttpErrorCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static HttpErrorCode getByCode(int code) {
            HttpErrorCode type = lookup.get(code);
            return type != null ? type : UNKNOWN;
        }
    }
}

package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions;

import lombok.Getter;

@Getter
public class AppServerException extends Exception {
    private final String code;

    public AppServerException(String code, String message) {
        super(message);
        this.code = code;
    }
}

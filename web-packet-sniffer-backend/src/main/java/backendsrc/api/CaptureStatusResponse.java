package backendsrc.api;

import java.util.UUID;

import backendsrc.domain.CaptureState;

public class CaptureStatusResponse {
    public UUID sessionID;
    public CaptureState state;
    public String message;

    public CaptureStatusResponse(UUID id, CaptureState stateIn, String messageIn) {
        sessionID = id;
        state = stateIn;
        message = messageIn;
    }
}

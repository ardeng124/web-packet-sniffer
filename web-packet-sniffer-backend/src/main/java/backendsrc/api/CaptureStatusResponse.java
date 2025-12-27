package backendsrc.api;

import java.util.UUID;

import backendsrc.domain.CaptureState;

public class CaptureStatusResponse {
    public UUID sessionID;
    public CaptureState state;

    public CaptureStatusResponse(UUID id, CaptureState stateIn) {
        sessionID = id;
        state = stateIn;
    }
}

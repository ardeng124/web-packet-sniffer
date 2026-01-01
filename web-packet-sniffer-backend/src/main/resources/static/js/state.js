
var selectedInterface;
var session = null;

export const setSelectedInterface = (interfaceName) => {
    selectedInterface = interfaceName;
}

export const getSelectedInterface = () => {
    return selectedInterface;
}

export const setSession = (newSession) => {
    session = newSession;
}

export const getSession = () => {
    return session;
}

export const isRunning = () => {
    return session.state == 'RUNNING'
}
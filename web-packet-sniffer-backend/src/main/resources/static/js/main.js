import * as api from './api.js'
import * as ui from './ui.js'
import * as state from "./state.js"

document.addEventListener("DOMContentLoaded", (event) => {
    initApp();
})
const initApp = async () => {
    let interfaces = await api.getAvailableInterfaces();
    ui.renderInterfaceList(interfaces);
    ui.onStartCaptureClicked(handleStartCapture);
}

const handleStartCapture = async () => {
    ui.clearStatus();
    console.log("click")
    const iface = state.getSelectedInterface();
    console.log(iface)
    if (!iface) {
        ui.updateStatus("Please select an interface")
        return;
    }

    try {
        const status = await api.startCapture(iface);
        state.setSession(status);
        //TODO: startPollingPackets();
    } catch (err) {
        ui.updateStatus(`${err}`)
    }
}


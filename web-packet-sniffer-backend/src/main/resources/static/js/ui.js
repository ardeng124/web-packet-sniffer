import * as state from './state.js'

export const renderInterfaceList = (interfaces) => {
    const interfaceList = document.getElementById("interfaceList");
    console.log(interfaces)
    interfaces.forEach(element => {
        const int = document.createElement("option");
        int.value = element.name;
        int.textContent = element.name;
        interfaceList.appendChild(int);
    });
}

export const selectInterface = (e) => {
    e.preventDefault();
    state.setSelectedInterface(e.target.value);
}

export const onStartCaptureClicked = (handler) => {
    document
        .getElementById("startBtn")
        .addEventListener("click", handler);
}

export const updateStatus = (status) => {
    document.getElementById("statusText").textContent = status;
}

export const clearStatus = () => {
    document.getElementById("statusText").textContent = ""
}

export const disableStartEnableStop = () => {
    let startBtn = document.getElementById("startBtn")
    let stopBtn = document.getElementById("stopBtn")
    startBtn.disabled = true;
    stopBtn.disabled = false;
}

export const enableStartDisableStop = () => {
    let startBtn = document.getElementById("startBtn")
    let stopBtn = document.getElementById("stopBtn")
    startBtn.disabled = false;
    stopBtn.disabled = true;
}

export const renderPackets = (packets) => {
    if (!Array.isArray(packets) || packets.length === 0) return;
    const tbody = document.querySelector("#packetList tbody");
    tbody.innerHTML = "";
    const fragment = document.createDocumentFragment();
        packets.forEach((packet) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${new Date(packet.timestamp).toLocaleTimeString()}</td>
                <td>${packet.protocol}</td>
                <td>${packet.sourceAddress}</td>
                <td>${packet.port}</td>
                <td>${packet.destinationAddress}</td>
                <td>${packet.destPort}</td>
                <td>${packet.packetLength}</td>
            `;
            fragment.appendChild(row);
        })
        tbody.appendChild(fragment);            
}


window.selectInterface = selectInterface;
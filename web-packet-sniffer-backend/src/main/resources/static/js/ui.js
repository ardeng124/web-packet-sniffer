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

window.selectInterface = selectInterface;
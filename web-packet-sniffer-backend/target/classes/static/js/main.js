import * as api from './api.js'
import * as ui from './ui.js'

document.addEventListener("DOMContentLoaded", (event) => {
    initApp();
})
const initApp = async () => {
    let interfaces = await api.getAvailableInterfaces();
    ui.renderInterfaceList(interfaces);
}


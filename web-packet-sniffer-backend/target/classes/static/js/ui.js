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
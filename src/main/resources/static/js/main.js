document.addEventListener("DOMContentLoaded", function () {
    const btnConsultar = document.getElementById("btnConsultar");
    const tablaResultados = document.getElementById("tablaResultados");
    

    btnConsultar.addEventListener("click", function () {
        consultarJira();
    });

    function consultarJira() {
//        alert("Entro al consultarJira");
        const fechaInicio = document.getElementById("fechaInicio").value;
        const fechaFinal = document.getElementById("fechaFin").value;
        

        //const jqlQuery = encodeURIComponent(document.getElementById("jqlQuery"));
        //console.log("jqlQuery: " + jqlQuery);

        fetch("http://localhost:9080/jira/consultar?fechaInicio=" + fechaInicio+ "&fechaFinal="+fechaFinal)
            .then(response => response.json())
            .then(data => {
                console.log("Respuesta de Jira:", data);
                alert("Consulta completada. Revisa la consola.");
                llenarTabla(data);
            })
            .catch(error => {
                console.error("Error en la consulta:", error);
                alert("Error al consultar Jira.");
            });

    }
    
    function llenarTabla(data) {
		const tablaResultados = document.getElementById("tablaResultados");
	}

    function actualizarTabla(datos) {
        // LIMPIAR DATOS DE LA TABLA ANTES DE HACER LA CONSULTA
        tablaResultados.innerHTML = "";
    }
    
})

document.addEventListener("DOMContentLoaded", function () {
    const btnGeneraFile = document.getElementById("btnGeneraFile");   
    
    btnGeneraFile.addEventListener("click", function () {
        generaCSV();
    });
    
    
    function generaCSV(){
		
	}

 })
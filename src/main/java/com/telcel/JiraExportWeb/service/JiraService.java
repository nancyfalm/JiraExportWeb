package com.telcel.JiraExportWeb.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JiraService {

    private static final String JIRA_URL = "https://telcel-informatica.atlassian.net";
    private static final String USER_EMAIL = "EX554127@telcel.com";
    private static final String API_TOKEN = "ATATT3xFfGF0DyVahckTZTCBuDRNgrDrKqxAoqwUbbE4Ds0E_qpEd1gK88PJ-qFu3PZe2YxnQmGauFpHFMMKFFcvT0fdCV33RXt79a46tsRgYTFOzeHcrQP2Jj5wz7f5U1_OFNB5uBgxhm6usb84--SwlPfgN78sqjYJvyJ2rTzUeeuZP8AePII=64BA9772";

    public List<Map<String, String>> consultarJira(String fechaInicio, String fechaFinal) {
        List<Map<String, String>> resultados = new ArrayList<>();

        try {

        // SE CODIFICA EL JQL PARA QUE SE PUEDA MANEJAR
        //
        String jqlQuery = "project = \"GPSVA001\" AND ( (status = \":: TERMINADO\" AND \"ppg Tipo de Folio SISAP[Dropdown]\" != Tarea "
        		+ "AND resolved >= \""+fechaInicio+"\" "
        		+ "AND resolved <= \""+fechaFinal+"\") OR (status changed to (\"::DETENIDA\") DURING (\""+fechaInicio+"\",\""+fechaFinal+"\")) ) "
        		+ "ORDER BY cf[10201] ASC, key DESC"; 
        
        String encodedJQL = URLEncoder.encode(jqlQuery, StandardCharsets.UTF_8.toString());
        String apiUrl = JIRA_URL + "/rest/api/2/search?jql=" + encodedJQL + "&maxResults=100";

        // CONEXIÓN A JIRA
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // SE AUTENTICA EL CORREO CON TOKEN
        String auth = USER_EMAIL + ":" + API_TOKEN;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        connection.setRequestProperty("Accept", "application/json");

        // OBTENEMOS LA RESPUESTA DE JIRA
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray issues = jsonResponse.getJSONArray("issues");

            for (int i = 0; i < issues.length(); i++) {
                JSONObject issue = issues.getJSONObject(i);
                JSONObject fields = issue.getJSONObject("fields");

                // MAPEAR LOS DATOS
                Map<String, String> datos = new HashMap<>();
                datos.put("folioSisap", fields.optString("customfield_10035", "N/A"));
                datos.put("tipoProyecto", fields.optJSONObject("customfield_10425") != null ? fields.getJSONObject("customfield_10425").optString("value", "N/A") : "N/A");
                datos.put("tituloSisap", fields.optString("customfield_10224", "N/A"));
                datos.put("solicitante", fields.optString("customfield_10422", "N/A"));
                datos.put("startDate", fields.optString("customfield_10015", "N/A"));
                datos.put("fechaTermino", fields.optString("customfield_10207", "N/A"));
                datos.put("finAnalisis", fields.optString("customfield_10398", "N/A"));
                datos.put("finDesarrollo", fields.optString("customfield_10399", "N/A"));
                datos.put("finPruebas", fields.optString("customfield_10400", "N/A"));
                datos.put("migracionProductiva", fields.optString("customfield_10401", "N/A"));
                datos.put("fechaLiberacion", fields.optString("customfield_10704", "N/A"));
                datos.put("departamento", fields.optJSONObject("customfield_10690") != null ? fields.getJSONObject("customfield_10690").optString("value", "N/A") : "N/A");
                datos.put("jefeDepartamento", fields.optJSONObject("customfield_10201") != null ? fields.getJSONObject("customfield_10201").optString("displayName", "N/A") : "N/A");
                datos.put("avanceReal", fields.optString("customfield_10206", "N/A"));
                datos.put("avancePlaneado", fields.optString("customfield_10208", "N/A"));
                datos.put("etapa", fields.optString("customfield_10801", "N/A"));
                datos.put("relevanteMes", fields.optString("customfield_10405", "N/A"));

                resultados.add(datos);
            }
        } else {
            System.out.println("Error al consultar JIRA: " + responseCode);
        }

    } catch (Exception e) {
            e.printStackTrace();
        }
        return resultados;
    }
}
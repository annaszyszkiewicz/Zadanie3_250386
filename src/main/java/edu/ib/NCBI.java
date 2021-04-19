package edu.ib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NCBI {

    public static String getResponse(String protein) throws IOException {
        StringBuilder response = new StringBuilder();
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&id=" + protein + "&rettype=fasta&retmode=xml";

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("Response " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();

        String string = response.toString();
        string = string.split("<TSeq_sequence>")[1].split("</TSeq_sequence>")[0];
        return string;
    }
}

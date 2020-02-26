package com.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.model.IndexingImpl;
import com.model.ParseIndexModel;

@MultipartConfig
/**
 * Servlet class.
 * @param request request send to server
 * @param response respond from server
 */
public class ParseIndex extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		out.println("Inverted Indices by Zones");
		out.println("\n");

		Part filePart = request.getPart("fileName"); // Retrieves <input type="file" name="file">
		
		InputStream fileContent = filePart.getInputStream();
		
		
		long startTime = System.currentTimeMillis();
		
		ParseIndexModel pim = new ParseIndexModel();
		IndexingImpl indexing = pim.parseIndex(fileContent);
		long stopTime = System.currentTimeMillis();
		double heapSize = Runtime.getRuntime().totalMemory() / 10000;
		
		displayInvertedIndices(indexing, out);
		
		double eclapsedTime = (double) (stopTime - startTime) / 1000.0;
		out.println("\nTime used for indexing: " + eclapsedTime + " seconds.");
		out.println("Space used for indexing: " + heapSize + " kbs.");		
	
	}

	private static void displayInvertedIndices(IndexingImpl indexing, PrintWriter out) {
		String[] zones = { "Title", "Author", "Abstract", "Source", "MajorSubject", "MinorSubject", "Reference",
				"Citation" };
		Map<String, TreeMap<Integer, TreeSet<Integer>>> invertedIndex;
		for (String zone : zones) {
			out.println("\n" + zone + " Inverted Indices: \n");
			
			switch (zone) {
			case "Title":
				invertedIndex = indexing.getTitleInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			case "Author":
				invertedIndex = indexing.getAuthorInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			case "Abstract":
				invertedIndex = indexing.getAbstractInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			case "Source":
				invertedIndex = indexing.getSourceInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			case "MajorSubject":
				invertedIndex = indexing.getMajorSubInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			case "MinorSubject":
				invertedIndex = indexing.getMinorSubInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			case "Reference":
				invertedIndex = indexing.getReferenceInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			case "Citation":
				invertedIndex = indexing.getCitationInvertedIndex();
				displayIndex(invertedIndex, out);
				break;
			}

		}
		
		
	}
	
	private static void displayIndex(Map<String, TreeMap<Integer, TreeSet<Integer>>> invertedIndex, PrintWriter out) {
		for (Map.Entry<String, TreeMap<Integer, TreeSet<Integer>>> mapElement : invertedIndex.entrySet()) {
			String key = (String) mapElement.getKey();
			TreeMap<Integer, TreeSet<Integer>> docTreeMap = (TreeMap<Integer, TreeSet<Integer>>) mapElement.getValue();
			Set<Integer> docIds = docTreeMap.keySet();
			out.println(key + " : " + docIds + "\n");
		}
	}


}

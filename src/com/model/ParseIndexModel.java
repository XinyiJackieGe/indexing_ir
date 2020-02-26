package com.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parse xml file and do indexing.
 * 
 * @param filecontent Inputstream
 * @return indexing contains inverted indices by zones
 */
public class ParseIndexModel {
	public IndexingImpl parseIndex(InputStream filecontent) throws IOException {
		Map<String, Integer> stopWordMap = new StopWordMap().getStopWordMap();
		TitleTokenize titleToken = new TitleTokenize();
		AbTokenize abstractToken = new AbTokenize();
		AuthorTokenize authorToken = new AuthorTokenize();
		SourceTokenize sourceToken = new SourceTokenize();
		MajorSubjTokenize majorSubjToken = new MajorSubjTokenize();
		MinorSubjTokenize minorSubjToken = new MinorSubjTokenize();
		ReferenceTokenize referenceToken = new ReferenceTokenize();
		CitationTokenize citationToken = new CitationTokenize();

		int docIdWtRN = 0;
//		long startTime = System.currentTimeMillis();

		IndexingImpl indexing = new IndexingImpl();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {

			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(filecontent);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("RECORD");

			for (int temp = 0; temp < nList.getLength(); temp++) { // nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				// System.out.println("\nCurrent Doc Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					// Retrieve doc id.
					int docId;
					String docIdS = eElement.getElementsByTagName("RECORDNUM").item(0).getTextContent();
					if (docIdS == null) {
						docIdWtRN++;
						docId = docIdWtRN;
					} else {
						docId = Integer.parseInt(docIdS);
					}

					// Title tokenization and indexing.
					if (eElement.getElementsByTagName("TITLE").item(0).getTextContent() != null) {
						String title = eElement.getElementsByTagName("TITLE").item(0).getTextContent();
						processTitleTokenizeIndexing(title, stopWordMap, titleToken, indexing, docId);
					}

					// Source tokenization and indexing.
					if (eElement.getElementsByTagName("SOURCE").item(0) != null) {
						String source = eElement.getElementsByTagName("SOURCE").item(0).getTextContent();
						processSourceTokenizeIndexing(source, stopWordMap, sourceToken, indexing, docId);
					}

					// Abstract tokenization and indexing.

					if (eElement.getElementsByTagName("ABSTRACT").item(0) != null) {
						String ab = eElement.getElementsByTagName("ABSTRACT").item(0).getTextContent();
						processAbstractTokenizeIndexing(ab, stopWordMap, abstractToken, indexing, docId);
					}

					// Authors tokenization and indexing.
					if (eElement.getElementsByTagName("AUTHORS").item(0) != null) {
						String authors = eElement.getElementsByTagName("AUTHORS").item(0).getTextContent();
						processAuthorTokenizeIndexing(authors, stopWordMap, authorToken, indexing, docId);
					}

					// MajorSubj tokenization and indexing.
					if (eElement.getElementsByTagName("MAJORSUBJ").item(0) != null) {
						String majorSubj = eElement.getElementsByTagName("MAJORSUBJ").item(0).getTextContent();
						processMajorSubjTokenizeIndexing(majorSubj, stopWordMap, majorSubjToken, indexing, docId);
					}

					// MinorSubj tokenization and indexing.
					if (eElement.getElementsByTagName("MINORSUBJ").item(0) != null) {
						String minorSubj = eElement.getElementsByTagName("MINORSUBJ").item(0).getTextContent();
						processMinorSubjTokenizeIndexing(minorSubj, stopWordMap, minorSubjToken, indexing, docId);
					}

					// Reference tokenization and indexing.
					if (eElement.getElementsByTagName("REFERENCES").item(0) != null) {
						NodeList referenceChildren = eElement.getElementsByTagName("REFERENCES").item(0)
								.getChildNodes();
						for (int i = 0; i < referenceChildren.getLength(); i++) {
							Node childNode = referenceChildren.item(i);
							if (childNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eChildElement = (Element) childNode;
								if (eChildElement != null) {
									String refAuthor = eChildElement.getAttribute("author");
									if (refAuthor != null) {
										// out.println("\n" + refAuthor);
										processReferenceTokenizeIndexing(refAuthor, stopWordMap, referenceToken,
												indexing, docId, "author");
									}
									String refPublication = eChildElement.getAttribute("publication");
									if (refPublication != null) {
										// out.println("\n" + refPublication);
										processReferenceTokenizeIndexing(refPublication, stopWordMap, referenceToken,
												indexing, docId, "publication");
									}
								}
							}
						}
					}

					// Citation tokenization and indexing.
					if (eElement.getElementsByTagName("CITATIONS").item(0) != null) {
						NodeList citationChildren = eElement.getElementsByTagName("CITATIONS").item(0).getChildNodes();
						for (int i = 0; i < citationChildren.getLength(); i++) {
							Node childNode = citationChildren.item(i);
							if (childNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eChildElement = (Element) childNode;
								if (eChildElement != null) {
									String citeAuthor = eChildElement.getAttribute("author");
									if (citeAuthor != null) {
										// out.println("\n" + citeAuthor);
										processCitationTokenizeIndexing(citeAuthor, stopWordMap, citationToken,
												indexing, docId, "author");
									}

									String citePublication = eChildElement.getAttribute("publication");
									if (citePublication != null) {
										// out.println("\n" + citePublication);
										processCitationTokenizeIndexing(citePublication, stopWordMap, citationToken,
												indexing, docId, "publication");
									}
								}
							}
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return indexing;

	}

	private static void processTitleTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			TitleTokenize titleToken, IndexingImpl indexing, int docId) {
		List<String> abbreviationWords = abbreviationExtect(text);
		List<String> words = Arrays
				.asList(text.replaceAll("[.]", ". ").replaceAll("[^a-zA-Z-. ]", "").toLowerCase().trim().split("\\s+"));
		words.addAll(abbreviationWords);
		int position = -1;
		for (String word : words) {
			String token = titleToken.normalize(word);
			if (titleToken.isStopWord(token, stopWordMap)) {
				continue;
			}
			if (token.equals("")) {
				continue;
			}
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getTitleInvertedIndex());
		}
	}

	private static void processAbstractTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			AbTokenize abstractToken, IndexingImpl indexing, int docId) {
		List<String> abbreviationWords = abbreviationExtect(text);
		List<String> words = Arrays
				.asList(text.replaceAll("[.]", ". ").replaceAll("[^a-zA-Z-. ]", "").toLowerCase().trim().split("\\s+"));
		words.addAll(abbreviationWords);
		int position = -1;
		for (String word : words) {
			String token = abstractToken.normalize(word);
			if (abstractToken.isStopWord(token, stopWordMap)) {
				continue;
			}
			if (token.equals("")) {
				continue;
			}
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getAbstractInvertedIndex());
		}

	}

	private static void processAuthorTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			AuthorTokenize authorToken, IndexingImpl indexing, int docId) {
		int position = -1;
		List<String> tokens = authorToken.normalize(text);
		for (String token : tokens) {
			if (token.equals("")) {
				continue;
			}
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getAuthorInvertedIndex());
			String last = token.split("\\s+")[0];
			indexing.insertInvertedIndex(last, docId, position, indexing.getAuthorInvertedIndex());
		}
	}

	private static void processMajorSubjTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			MajorSubjTokenize majorSubjToken, IndexingImpl indexing, int docId) {
		int position = -1;
		List<String> tokens = majorSubjToken.normalize(text);
		for (String token : tokens) {
			if (majorSubjToken.isStopWord(token, stopWordMap)) {
				continue;
			}
			if (token.equals("")) {
				continue;
			}
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getMajorSubInvertedIndex());
		}
	}

	private static void processMinorSubjTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			MinorSubjTokenize minorSubjToken, IndexingImpl indexing, int docId) {
		int position = -1;
		List<String> tokens = minorSubjToken.normalize(text);
		for (String token : tokens) {
			if (minorSubjToken.isStopWord(token, stopWordMap)) {
				continue;
			}
			if (token.equals("")) {
				continue;
			}
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getMinorSubInvertedIndex());
		}
	}

	private static void processSourceTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			SourceTokenize sourceToken, IndexingImpl indexing, int docId) {
		int position = -1;
		List<String> tokens = sourceToken.normalize(text);
		for (String token : tokens) {
			// source do not consider stop word

			if (token.equals("")) {
				continue;
			}
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getSourceInvertedIndex());
		}
	}

	private static void processReferenceTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			ReferenceTokenize referenceToken, IndexingImpl indexing, int docId, String type) {
		int position = -1;
		if (type.contentEquals("author")) {
			String token = text.replaceAll("[-]", " ").toLowerCase();
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getReferenceInvertedIndex());
			String last = token.split("\\s+")[0];
			indexing.insertInvertedIndex(last, docId, position, indexing.getReferenceInvertedIndex());

		} else {
			String[] words = text.replaceAll("[-]", " ").toLowerCase().split("\\s+");
			for (String word : words) {
				String token = referenceToken.normalize(word);
				if (referenceToken.isStopWord(word, stopWordMap)) {
					continue;
				}
				if (token.equals("")) {
					continue;
				}
				position++;
				indexing.insertInvertedIndex(token, docId, position, indexing.getReferenceInvertedIndex());
			}
		}
	}

	private static void processCitationTokenizeIndexing(String text, Map<String, Integer> stopWordMap,
			CitationTokenize citationToken, IndexingImpl indexing, int docId, String type) {
		int position = -1;
		if (type.equals("author")) {
			String token = text.replaceAll("[-]", " ").toLowerCase();
			position++;
			indexing.insertInvertedIndex(token, docId, position, indexing.getCitationInvertedIndex());
			String last = token.split("\\s+")[0];
		    indexing.insertInvertedIndex(last, docId, position, indexing.getCitationInvertedIndex());
		} else {
			String[] words = text.replaceAll("[-]", " ").toLowerCase().split("\\s+");
			for (String word : words) {
				String token = citationToken.normalize(word);
				if (citationToken.isStopWord(word, stopWordMap)) {
					continue;
				}
				if (token.equals("")) {
					continue;
				}
				position++;
				indexing.insertInvertedIndex(token, docId, position, indexing.getCitationInvertedIndex());
			}
		}
	}

	private static List<String> abbreviationExtect(String str) {
		Pattern reStringD = Pattern.compile("\\b(?:[a-zA-Z]\\.){2,}"); // match dotted words H.P.
		Matcher matchStringD = reStringD.matcher(str);
		List<String> abrevs = new ArrayList<String>();
		while (matchStringD.find()) {
			String a = matchStringD.group();
			abrevs.add(a);
		}
		List<String> tokenAbrevs = new ArrayList<String>();
		for (String s : abrevs) {
			s = s.replaceAll("[.]", "").toLowerCase();
			tokenAbrevs.add(s);
		}

		return tokenAbrevs;
	}

}

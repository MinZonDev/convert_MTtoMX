import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MT202ToPACS009Converter {

    public static String mt202ToPacs009(String mt202Message) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Create root element
            Element rootElement = doc.createElementNS("urn:iso:std:iso:20022:tech:xsd:pacs.009.001.08", "Document");
            doc.appendChild(rootElement);

            // Create FIToFICstmrCdtTrf element
            Element fiToFICstmrCdtTrf = doc.createElement("FIToFICstmrCdtTrf");
            rootElement.appendChild(fiToFICstmrCdtTrf);

            // Create GrpHdr element
            Element grpHdr = doc.createElement("GrpHdr");
            fiToFICstmrCdtTrf.appendChild(grpHdr);

            // Create MsgId element
            Element msgId = doc.createElement("MsgId");
            msgId.appendChild(doc.createTextNode("REFERENCE"));
            grpHdr.appendChild(msgId);

            // Create CreDtTm element
            Element creDtTm = doc.createElement("CreDtTm");
            creDtTm.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date())));
            grpHdr.appendChild(creDtTm);

            // Parse MT202 message
            String[] mtLines = mt202Message.split("\\n");
            String sender = "";
            String receiver = "";
            String amount = "";
            String currency = "";
            String date = "";
            String reference = "";

            for (String line : mtLines) {
                if (line.startsWith(":52A:")) {
                    sender = line.substring(6);
                } else if (line.startsWith(":57A:")) {
                    receiver = line.substring(6);
                } else if (line.startsWith(":32B:")) {
                    String[] data = line.substring(6).split(" ");
                    if (data.length >= 2) { // Check if array has at least 2 elements
                        currency = data[0];
                        amount = data[1];
                    } else {
                        // Handle case where not enough information for currency and amount element
                    }
                } else if (line.startsWith(":30:")) {
                    date = line.substring(4, 10);
                } else if (line.startsWith(":20:")) {
                    reference = line.substring(4);
                }
            }

            // Create CdtTrfTxInf element
            Element cdtTrfTxInf = doc.createElement("CdtTrfTxInf");
            fiToFICstmrCdtTrf.appendChild(cdtTrfTxInf);

            // Create PmtId element inside CdtTrfTxInf
            Element pmtId = doc.createElement("PmtId");
            cdtTrfTxInf.appendChild(pmtId);

            // Create EndToEndId element inside PmtId
            Element endToEndId = doc.createElement("EndToEndId");
            endToEndId.appendChild(doc.createTextNode(reference));
            pmtId.appendChild(endToEndId);

            // Create InstrId element inside PmtId
            Element instrId = doc.createElement("InstrId");
            instrId.appendChild(doc.createTextNode(reference));
            pmtId.appendChild(instrId);

            // Create Amt element inside CdtTrfTxInf
            Element amt = doc.createElement("Amt");
            cdtTrfTxInf.appendChild(amt);

            // Create InstdAmt element inside Amt
            Element instdAmt = doc.createElement("InstdAmt");
            instdAmt.setAttribute("Ccy", currency);
            instdAmt.appendChild(doc.createTextNode(amount));
            amt.appendChild(instdAmt);

            // Create ChrgBr element inside CdtTrfTxInf
            Element chrgBr = doc.createElement("ChrgBr");
            chrgBr.appendChild(doc.createTextNode("SLEV"));
            cdtTrfTxInf.appendChild(chrgBr);

            // Create CdtrAgt element inside CdtTrfTxInf
            Element cdtrAgt = doc.createElement("CdtrAgt");
            cdtTrfTxInf.appendChild(cdtrAgt);

            // Create FinInstnId element inside CdtrAgt
            Element finInstnId = doc.createElement("FinInstnId");
            cdtrAgt.appendChild(finInstnId);

            // Create BIC element inside FinInstnId
            Element bic = doc.createElement("BIC");
            bic.appendChild(doc.createTextNode(receiver));
            finInstnId.appendChild(bic);

            // Create Cdtr element inside CdtTrfTxInf
            Element cdtr = doc.createElement("Cdtr");
            cdtTrfTxInf.appendChild(cdtr);

            // Create Nm element inside Cdtr
            Element nm = doc.createElement("Nm");
            nm.appendChild(doc.createTextNode("RECEIVER NAME"));
            cdtr.appendChild(nm);

            // Create CdtrAcct element inside CdtTrfTxInf
            Element cdtrAcct = doc.createElement("CdtrAcct");
            cdtTrfTxInf.appendChild(cdtrAcct);

            // Create Id element inside CdtrAcct
            Element id = doc.createElement("Id");
            cdtrAcct.appendChild(id);

            // Create Othr element inside Id
            Element othr = doc.createElement("Othr");
            id.appendChild(othr);

            // Create Id element inside Othr
            Element othrId = doc.createElement("Id");
            othrId.appendChild(doc.createTextNode(receiver));
            othr.appendChild(othrId);

            // Create RmtInf element inside CdtTrfTxInf
            Element rmtInf = doc.createElement("RmtInf");
            cdtTrfTxInf.appendChild(rmtInf);

            // Create Ustrd element inside RmtInf
            Element ustrd = doc.createElement("Ustrd");
            ustrd.appendChild(doc.createTextNode("TRANSACTION DETAILS"));
            rmtInf.appendChild(ustrd);

            // Convert Document to string
            return documentToString(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String documentToString(Document doc) {
        try {
            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
            java.io.StringWriter sw = new java.io.StringWriter();
            javax.xml.transform.stream.StreamResult sr = new javax.xml.transform.stream.StreamResult(sw);
            javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(doc);
            transformer.transform(domSource, sr);
            return sw.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}


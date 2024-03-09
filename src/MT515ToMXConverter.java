import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MT515ToMXConverter {

    public static String mt515ToMx(String mt515Message) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Tạo phần tử gốc
            Element rootElement = doc.createElementNS("urn:iso:std:iso:20022:tech:xsd:camt.054.001.01", "Doc:Document");
            doc.appendChild(rootElement);

            // Tạo phần tử BkToCstmrStmt
            Element bkToCstmrStmt = doc.createElement("Doc:BkToCstmrStmt");
            rootElement.appendChild(bkToCstmrStmt);

            // Tạo phần tử GrpHdr
            Element grpHdr = doc.createElement("Doc:GrpHdr");
            bkToCstmrStmt.appendChild(grpHdr);

            // Tạo phần tử MsgId
            Element msgId = doc.createElement("Doc:MsgId");
            msgId.appendChild(doc.createTextNode("REFERENCE"));
            grpHdr.appendChild(msgId);

            // Tạo phần tử CreDtTm
            Element creDtTm = doc.createElement("Doc:CreDtTm");
            creDtTm.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date())));
            grpHdr.appendChild(creDtTm);

            // Phân tích tin nhắn MT515
            // Tạo các biến để lưu thông tin từ tin nhắn MT515
            String transactionReference = "";
            String sender = "";
            String receiver = "";
            String amount = "";
            String currency = "";
            String valueDate = "";
            String instructionCode = "";

            // Lặp qua các dòng của tin nhắn MT515 để phân tích
            String[] mtLines = mt515Message.split("\\n");
            for (String line : mtLines) {
                if (line.startsWith(":20:")) {
                    transactionReference = line.substring(4);
                } else if (line.startsWith(":87A:")) {
                    sender = line.substring(5, 15);
                    receiver = line.substring(15);
                } else if (line.startsWith(":32B:")) {
                    String[] data = line.substring(5).split(" ");
                    currency = data[0];
                    amount = data[1];
                    valueDate = data[2];
                } else if (line.startsWith(":34F:")) {
                    instructionCode = line.substring(5);
                }
            }

            // Tạo phần tử Stmt
            Element stmt = doc.createElement("Doc:Stmt");
            bkToCstmrStmt.appendChild(stmt);

            // Tạo phần tử Id
            Element id = doc.createElement("Doc:Id");
            id.appendChild(doc.createTextNode(transactionReference));
            stmt.appendChild(id);

            // Tạo phần tử Acct
            Element acct = doc.createElement("Doc:Acct");
            stmt.appendChild(acct);

            // Tạo phần tử Bal
            Element bal = doc.createElement("Doc:Bal");
            stmt.appendChild(bal);

            // Tạo phần tử Amt
            Element amt = doc.createElement("Doc:Amt");
            amt.setAttribute("Ccy", currency);
            amt.appendChild(doc.createTextNode(amount));
            bal.appendChild(amt);

            // Tạo phần tử Ntry
            Element ntry = doc.createElement("Doc:Ntry");
            stmt.appendChild(ntry);

            // Tạo phần tử Amt cho Ntry
            Element amtNtry = doc.createElement("Doc:Amt");
            amtNtry.setAttribute("Ccy", currency);
            amtNtry.appendChild(doc.createTextNode(amount));
            ntry.appendChild(amtNtry);

            // Tạo phần tử BookgDt
            Element bookgDt = doc.createElement("Doc:BookgDt");
            ntry.appendChild(bookgDt);

            // Tạo phần tử Dt cho BookgDt
            Element dt = doc.createElement("Doc:Dt");
            dt.appendChild(doc.createTextNode(valueDate));
            bookgDt.appendChild(dt);

            // Tạo phần tử AddtlNtryInf
            Element addtlNtryInf = doc.createElement("Doc:AddtlNtryInf");
            addtlNtryInf.appendChild(doc.createTextNode(instructionCode));
            ntry.appendChild(addtlNtryInf);

            // Chuyển đổi Document sang chuỗi
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

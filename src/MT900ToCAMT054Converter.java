import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MT900ToCAMT054Converter {

    public static String mt900ToCamt054(String mt900Message) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Create root element
            Element rootElement = doc.createElementNS("urn:iso:std:iso:20022:tech:xsd:camt.054.001.01", "Doc:Document");
            doc.appendChild(rootElement);

            // Create BkToCstmrStmt element
            Element bkToCstmrStmt = doc.createElement("Doc:BkToCstmrStmt");
            rootElement.appendChild(bkToCstmrStmt);

            // Create GrpHdr element
            Element grpHdr = doc.createElement("Doc:GrpHdr");
            bkToCstmrStmt.appendChild(grpHdr);

            // Create MsgId element
            Element msgId = doc.createElement("Doc:MsgId");
            msgId.appendChild(doc.createTextNode("REFERENCE"));
            grpHdr.appendChild(msgId);

            // Create CreDtTm element
            Element creDtTm = doc.createElement("Doc:CreDtTm");
            creDtTm.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date())));
            grpHdr.appendChild(creDtTm);

            // Parse MT900 message
            String[] mtLines = mt900Message.split("\\n");
            String reference = "";
            String account = "";
            String amount = "";
            String currency = "";
            String date = "";
            String additionalInfo = "";

            for (String line : mtLines) {
                if (line.startsWith(":20:")) {
                    reference = line.substring(4);
                } else if (line.startsWith(":25:")) {
                    account = line.substring(4);
                } else if (line.startsWith(":60F:")) {
                    String[] data = line.substring(5).split(" ");
                    if (data.length >= 2) { // Kiểm tra nếu mảng có ít nhất 2 phần tử
                        currency = data[0].substring(3);
                        amount = data[1];
                        date = data[0].substring(1, 7);
                    } else {
                        // Xử lý trường hợp không tìm thấy dấu cách trong chuỗi
                        // hoặc không đủ thông tin cho phần tử currency và amount
                    }
                } else if (line.startsWith(":86:")) {
                    additionalInfo = line.substring(5);
                }
            }

            // Create Stmt element
            Element stmt = doc.createElement("Doc:Stmt");
            bkToCstmrStmt.appendChild(stmt);

            // Create Id element
            Element id = doc.createElement("Doc:Id");
            id.appendChild(doc.createTextNode(reference));
            stmt.appendChild(id);

            // Create Acct element
            Element acct = doc.createElement("Doc:Acct");
            stmt.appendChild(acct);

            // Create Id element inside Acct
            Element acctId = doc.createElement("Doc:Id");
            acct.appendChild(acctId);

            // Create Othr element inside Id
            Element othr = doc.createElement("Doc:Othr");
            acctId.appendChild(othr);

            // Create Id element inside Othr
            Element othrId = doc.createElement("Doc:Id");
            othrId.appendChild(doc.createTextNode(account));
            othr.appendChild(othrId);

            // Create Bal element
            Element bal = doc.createElement("Doc:Bal");
            stmt.appendChild(bal);

            // Create Tp element inside Bal
            Element tp = doc.createElement("Doc:Tp");
            bal.appendChild(tp);

            // Create CdOrPrtry element inside Tp
            Element cdOrPrtry = doc.createElement("Doc:CdOrPrtry");
            tp.appendChild(cdOrPrtry);

            // Create Cd element inside CdOrPrtry
            Element cd = doc.createElement("Doc:Cd");
            cd.appendChild(doc.createTextNode("CLOS"));
            cdOrPrtry.appendChild(cd);

            // Create Amt element inside Bal
            Element amt = doc.createElement("Doc:Amt");
            amt.setAttribute("Ccy", currency);
            amt.appendChild(doc.createTextNode(amount));
            bal.appendChild(amt);

            // Create Ntry element
            Element ntry = doc.createElement("Doc:Ntry");
            stmt.appendChild(ntry);

            // Create Amt element inside Ntry
            Element amtNtry = doc.createElement("Doc:Amt");
            amtNtry.setAttribute("Ccy", currency);
            amtNtry.appendChild(doc.createTextNode(amount));
            ntry.appendChild(amtNtry);

            // Create Sts element inside Ntry
            Element sts = doc.createElement("Doc:Sts");
            sts.appendChild(doc.createTextNode("NCHK"));
            ntry.appendChild(sts);

            // Create BookgDt element inside Ntry
            Element bookgDt = doc.createElement("Doc:BookgDt");
            ntry.appendChild(bookgDt);

            // Create Dt element inside BookgDt
            Element dt = doc.createElement("Doc:Dt");
            dt.appendChild(doc.createTextNode(date));
            bookgDt.appendChild(dt);

            // Create AddtlNtryInf element inside Ntry
            Element addtlNtryInf = doc.createElement("Doc:AddtlNtryInf");
            addtlNtryInf.appendChild(doc.createTextNode(additionalInfo));
            ntry.appendChild(addtlNtryInf);

            // Create TxDtls element inside Ntry
            Element txDtls = doc.createElement("Doc:TxDtls");
            ntry.appendChild(txDtls);

            // Create RmtInf element inside TxDtls
            Element rmtInf = doc.createElement("Doc:RmtInf");
            txDtls.appendChild(rmtInf);

            // Create Ustrd element inside RmtInf
            Element ustrd = doc.createElement("Doc:Ustrd");
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

//    public static void main(String[] args) {
//        String mt900Message =
//                ":20:REFERENCE\n" +
//                        ":25:ACCOUNT1234567890\n" +
//                        ":28C:12345/1\n" +
//                        ":60F:C240101USD12345,67\n" +
//                        ":61:0101010101C12345,67NCHK1234567890123456\n" +
//                        "DRAFT BANK\n" +
//                        ":86:TRANSACTION DETAILS\n" +
//                        "-";
//
//        String camt054Message = mt900ToCamt054(mt900Message);
//        System.out.println(camt054Message);
//    }
}


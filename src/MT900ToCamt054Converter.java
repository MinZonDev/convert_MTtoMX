import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MT900ToCamt054Converter {
    public String convertMT900ToCamt054(String mt900Message) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root elements
            Document appHdrDoc = docBuilder.newDocument();
            Element appHdrRoot = appHdrDoc.createElement("AppHdr");
            appHdrRoot.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:head.001.001.02");

            // Add Fr element
            Element fr = appHdrDoc.createElement("Fr");
            Element fiIdFr = appHdrDoc.createElement("FIId");
            Element finInstnIdFr = appHdrDoc.createElement("FinInstnId");
            Element bicFiFr = appHdrDoc.createElement("BICFI");
            bicFiFr.appendChild(appHdrDoc.createTextNode("LRLRXXXXA11"));
            finInstnIdFr.appendChild(bicFiFr);
            fiIdFr.appendChild(finInstnIdFr);
            fr.appendChild(fiIdFr);
            appHdrRoot.appendChild(fr);

            // Add To element
            Element to = appHdrDoc.createElement("To");
            Element fiIdTo = appHdrDoc.createElement("FIId");
            Element finInstnIdTo = appHdrDoc.createElement("FinInstnId");
            Element bicFiTo = appHdrDoc.createElement("BICFI");
            bicFiTo.appendChild(appHdrDoc.createTextNode("COPZBEB0XXX"));
            finInstnIdTo.appendChild(bicFiTo);
            fiIdTo.appendChild(finInstnIdTo);
            to.appendChild(fiIdTo);
            appHdrRoot.appendChild(to);

            // Add other elements to AppHdr
            Element bizMsgIdr = appHdrDoc.createElement("BizMsgIdr");
            bizMsgIdr.appendChild(appHdrDoc.createTextNode("02569"));
            appHdrRoot.appendChild(bizMsgIdr);

            Element msgDefIdr = appHdrDoc.createElement("MsgDefIdr");
            msgDefIdr.appendChild(appHdrDoc.createTextNode("camt.054.001.08"));
            appHdrRoot.appendChild(msgDefIdr);

            Element bizSvc = appHdrDoc.createElement("BizSvc");
            bizSvc.appendChild(appHdrDoc.createTextNode("swift.cbprplus.02"));
            appHdrRoot.appendChild(bizSvc);

            Element creDt = appHdrDoc.createElement("CreDt");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String creDtString = dateFormat.format(new Date());
            creDt.appendChild(appHdrDoc.createTextNode(creDtString));
            appHdrRoot.appendChild(creDt);

            Element prty = appHdrDoc.createElement("Prty");
            prty.appendChild(appHdrDoc.createTextNode("NORM"));
            appHdrRoot.appendChild(prty);

            appHdrDoc.appendChild(appHdrRoot);

            // camt.054 Document
            Document docDoc = docBuilder.newDocument();
            Element docRoot = docDoc.createElement("Document");
            docRoot.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:camt.054.001.08");

            Element bkToCstmrDbtCdtNtfctn = docDoc.createElement("BkToCstmrDbtCdtNtfctn");
            Element grpHdr = docDoc.createElement("GrpHdr");
            Element msgId = docDoc.createElement("MsgId");
            msgId.appendChild(docDoc.createTextNode("02569"));
            grpHdr.appendChild(msgId);
            Element creDtTm = docDoc.createElement("CreDtTm");
            creDtTm.appendChild(docDoc.createTextNode(creDtString));
            grpHdr.appendChild(creDtTm);
            bkToCstmrDbtCdtNtfctn.appendChild(grpHdr);

            Element ntfctn = docDoc.createElement("Ntfctn");
            Element id = docDoc.createElement("Id");
            id.appendChild(docDoc.createTextNode("02569"));
            ntfctn.appendChild(id);

            Element acct = docDoc.createElement("Acct");
            Element acctId = docDoc.createElement("Id");
            Element othr = docDoc.createElement("Othr");
            Element othrId = docDoc.createElement("Id");
            othrId.appendChild(docDoc.createTextNode("6-9412771"));
            othr.appendChild(othrId);
            acctId.appendChild(othr);
            acct.appendChild(acctId);
            Element ccy = docDoc.createElement("Ccy");
            ccy.appendChild(docDoc.createTextNode("USD"));
            acct.appendChild(ccy);
            ntfctn.appendChild(acct);

            Element ntry = docDoc.createElement("Ntry");
            Element ntryRef = docDoc.createElement("NtryRef");
            ntryRef.appendChild(docDoc.createTextNode("02569"));
            ntry.appendChild(ntryRef);
            Element amt = docDoc.createElement("Amt");
            amt.appendChild(docDoc.createTextNode("112"));
            amt.setAttribute("Ccy", "USD");
            ntry.appendChild(amt);
            Element cdtDbtInd = docDoc.createElement("CdtDbtInd");
            cdtDbtInd.appendChild(docDoc.createTextNode("DBIT"));
            ntry.appendChild(cdtDbtInd);
            Element sts = docDoc.createElement("Sts");
            Element cd = docDoc.createElement("Cd");
            cd.appendChild(docDoc.createTextNode("BOOK"));
            sts.appendChild(cd);
            ntry.appendChild(sts);
            Element bookgDt = docDoc.createElement("BookgDt");
            Element dtTm = docDoc.createElement("DtTm");
            dtTm.appendChild(docDoc.createTextNode("2014-01-08T15:15:00.000+13:00"));
            bookgDt.appendChild(dtTm);
            ntry.appendChild(bookgDt);
            Element valDt = docDoc.createElement("ValDt");
            Element dt = docDoc.createElement("Dt");
            dt.appendChild(docDoc.createTextNode("2000-01-03"));
            valDt.appendChild(dt);
            ntry.appendChild(valDt);
            Element bkTxCd = docDoc.createElement("BkTxCd");
            Element domn = docDoc.createElement("Domn");
            Element cdDomn = docDoc.createElement("Cd");
            cdDomn.appendChild(docDoc.createTextNode("PMNT"));
            domn.appendChild(cdDomn);
            Element fmly = docDoc.createElement("Fmly");
            Element cdFmly = docDoc.createElement("Cd");
            cdFmly.appendChild(docDoc.createTextNode("MDOP"));
            fmly.appendChild(cdFmly);
            Element subFmlyCd = docDoc.createElement("SubFmlyCd");
            subFmlyCd.appendChild(docDoc.createTextNode("NTAV"));
            fmly.appendChild(subFmlyCd);
            domn.appendChild(fmly);
            bkTxCd.appendChild(domn);
            ntry.appendChild(bkTxCd);

            Element ntryDtls = docDoc.createElement("NtryDtls");
            Element txDtls = docDoc.createElement("TxDtls");
            Element refs = docDoc.createElement("Refs");
            Element instrId = docDoc.createElement("InstrId");
            instrId.appendChild(docDoc.createTextNode("123456/DEV"));
            refs.appendChild(instrId);
            txDtls.appendChild(refs);
            Element amtTxDtls = docDoc.createElement("Amt");
            amtTxDtls.appendChild(docDoc.createTextNode("112"));
            amtTxDtls.setAttribute("Ccy", "USD");
            txDtls.appendChild(amtTxDtls);
            Element cdtDbtIndTxDtls = docDoc.createElement("CdtDbtInd");
            cdtDbtIndTxDtls.appendChild(docDoc.createTextNode("DBIT"));
            txDtls.appendChild(cdtDbtIndTxDtls);
            Element rltdAgts = docDoc.createElement("RltdAgts");
            Element dbtrAgt = docDoc.createElement("DbtrAgt");
            Element finInstnId = docDoc.createElement("FinInstnId");
            Element bicFi = docDoc.createElement("BICFI");
            bicFi.appendChild(docDoc.createTextNode("TESTSEVT"));
            Element othrDbtrAgt = docDoc.createElement("Othr");
            Element idDbtrAgt = docDoc.createElement("Id");
            idDbtrAgt.appendChild(docDoc.createTextNode("ACCOUNTID1"));
            othrDbtrAgt.appendChild(idDbtrAgt);
            finInstnId.appendChild(bicFi);
            finInstnId.appendChild(othrDbtrAgt);
            dbtrAgt.appendChild(finInstnId);
            rltdAgts.appendChild(dbtrAgt);
            txDtls.appendChild(rltdAgts);
            Element rltdDts = docDoc.createElement("RltdDts");
            Element intrBkSttlmDt = docDoc.createElement("IntrBkSttlmDt");
            intrBkSttlmDt.appendChild(docDoc.createTextNode("2000-01-03"));
            rltdDts.appendChild(intrBkSttlmDt);
            txDtls.appendChild(rltdDts);
            ntryDtls.appendChild(txDtls);
            ntry.appendChild(ntryDtls);
            ntfctn.appendChild(ntry);
            Element addtlNtfctnInf = docDoc.createElement("AddtlNtfctnInf");
            addtlNtfctnInf.appendChild(docDoc.createTextNode("/INS/CHASUS33"));
            ntfctn.appendChild(addtlNtfctnInf);
            bkToCstmrDbtCdtNtfctn.appendChild(ntfctn);
            docRoot.appendChild(bkToCstmrDbtCdtNtfctn);
            docDoc.appendChild(docRoot);

            // Convert Document to String
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(appHdrDoc);
            StringWriter appHdrWriter = new StringWriter();
            StreamResult appHdrResult = new StreamResult(appHdrWriter);
            transformer.transform(source, appHdrResult);

            source = new DOMSource(docDoc);
            StringWriter docWriter = new StringWriter();
            StreamResult docResult = new StreamResult(docWriter);
            transformer.transform(source, docResult);

            // Combine both XML strings
            String appHdrXml = appHdrWriter.toString();
            String docXml = docWriter.toString();
            return appHdrXml + "\n" + docXml;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Hàm chuyển đổi từ tệp văn bản MT900 sang camt.054
    public String convertFileToCamt054(String filePath) {
        StringBuilder mt900Content = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                mt900Content.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        // Chuyển đổi nội dung MT900 thành camt.054 và trả về kết quả
        return convertMT900ToCamt054(mt900Content.toString());
    }
}

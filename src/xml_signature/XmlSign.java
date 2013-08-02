package xml_signature;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlSign {

    private static XMLSignatureFactory fac;
    private static KeyInfo ki;
    private static KeyPair kp;
    private static DigestMethod dm;
    private static CanonicalizationMethod can;
    private static SignatureMethod sm;
    
    public static void main(String[] args) throws Exception {
        
        /*step
        1. 取得 XMLSignatureFactory
        2. 產生 KeyInfo (有多種方式)
        3. 產生 SignedInfo
                                      告知正規化演算法, 簽章演算法, 簽章之 refrence (可多組, 並指定 DigestMethod 演算法)
        4. 由 KeyInfo 與 SignedInfo 產生 XMLSignature
        5. 呼叫 XMLSignature.sign 帶入 key 與 XML Document 進行加簽
        */
        
        // First, create the DOM XMLSignatureFactory that will be used to
        // generate the XMLSignature
        fac = XMLSignatureFactory.getInstance("DOM");
        
        // Create a DSA KeyPair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        kp = kpg.generateKeyPair();
        
        // Create a KeyValue containing the DSA PublicKey that was generated
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        KeyValue kv = kif.newKeyValue(kp.getPublic());

        // Create a KeyInfo and add the KeyValue to it
        ki = kif.newKeyInfo(Collections.singletonList(kv));

        dm = fac.newDigestMethod(DigestMethod.SHA256, null);
        
        can = fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null);
        //can = fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null);
        
//        sm = fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
        sm = fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null);
        
        //enveloping(System.out);
        
        Document doc = createDocument(XmlSign.class.getResourceAsStream("raw.xml"));
        enveloped(System.out, doc, "test");
        
    }
    
    /**
     * Signature 在 XML 內文中
     */
    public static void enveloped(OutputStream os, Document doc, String id) throws Exception {

        List<Reference> reflist = new ArrayList<Reference>();
        Reference ref;
        if (id != null) {
            ref = fac.newReference("#" + id, dm);
        } else {
            Transform transform = fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
            List<Transform> transforms = Collections.singletonList(transform);
            ref = fac.newReference("", dm, transforms, null, null);
        }
        reflist.add(ref);
        
        SignedInfo si = fac.newSignedInfo(can, sm, reflist);
        
        // location of the resulting XMLSignature's parent element.
        DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc.getDocumentElement());

        // Create the XMLSignature, but don't sign it yet.
        XMLSignature signature = fac.newXMLSignature(si, ki);

        // Marshal, generate, and sign the enveloped signature.
        signature.sign(dsc);
        
        outputXML(doc, os);
    }
    
    /**
     * 資料在 Signature 標籤內
     */
    public static void enveloping(OutputStream os) throws Exception {

        // Next, create a Reference to a same-document URI that is an Object
        // element and specify the SHA1 digest algorithm
        Reference ref = fac.newReference("#object", dm);

        // Next, create the referenced Object
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().newDocument();
        Node text = doc.createTextNode("some text");
        XMLStructure content = new DOMStructure(text);
        XMLObject obj = fac.newXMLObject(Collections.singletonList(content),
                "object", null, null);

        // Create the SignedInfo
        SignedInfo si = fac
                .newSignedInfo(can, sm,
                        Collections.singletonList(ref));

        // Create the XMLSignature (but don't sign it yet)
        XMLSignature signature = fac.newXMLSignature(si, ki,
                Collections.singletonList(obj), null, null);

        // Create a DOMSignContext and specify the DSA PrivateKey for signing
        // and the document location of the XMLSignature
        DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc);

        // Lastly, generate the enveloping signature using the PrivateKey
        signature.sign(dsc);

        outputXML(doc, os);
    }
    
    private static void outputXML(Document doc, OutputStream os) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(os));
    }
    
    private static Document createDocument(InputStream bais) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(bais);
        return doc;
    }

}

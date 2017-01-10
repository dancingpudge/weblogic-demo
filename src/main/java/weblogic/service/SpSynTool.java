package weblogic.service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import weblogic.entity.DemoEntity;
import weblogic.util.JCIFS_NTLMScheme;
import weblogic.constant.SPConstant;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.URL;


/**
 * Created by Liuh on 2016/10/11.
 */
public class SpSynTool {
    public boolean SpSynCBSTransaction(DemoEntity Entity) {
        String updateXML = "<UpdateListItems xmlns=\"http://schemas.microsoft.com/sharepoint/soap/\">" +
                "<listName>" + SPConstant.LISTNAME + "</listName>" +
                "<updates>" +
                "<Batch ListVersion=\"1\" OnError=\"Continue\">" +
                "<Method Cmd=\"New\" ID=\"1\">" +
                "<Field Name=\"Title\"></Field>" +
                "<Field Name=\"_x7a0e__x53f7__x53d8__x66f4_\">" + Entity.getMsg1() + "</Field>" +
                "<Field Name=\"_x5230__x6b3e__x65e5__x671f_\">" + Entity.getMsg2() + "</Field>" +
                "<Field Name=\"_x5230__x6b3e__x91d1__x989d_\">" + Entity.getMsg3() + "</Field>" +
                "</Method>" +
                "</Batch>" +
                "</updates>" +
                "</UpdateListItems>";
        String res = getSPXML(SPConstant.USER, SPConstant.PWD, updateXML);
        if(res.contains("<ErrorCode>0x00000000")){
            return true;
        }
        return false;
    }

    private String getSPXML(String userName, String pwd, String xmlStr) {

        String xmlHead = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"> <soap12:Body> ";
        String xmlTail = "</soap12:Body></soap12:Envelope>";
        xmlStr = xmlHead + xmlStr + xmlTail;
        URL uri;
        String soapResponseData = "";
        try {
            uri = new URL(SPConstant.WEBLOGICURL);
            PostMethod postMethod = new PostMethod(SPConstant.WEBLOGICURL);
            AuthPolicy.registerAuthScheme(AuthPolicy.NTLM, JCIFS_NTLMScheme.class);
            byte[] b = xmlStr.getBytes("utf-8");
            InputStream is = new ByteArrayInputStream(b, 0, b.length);
            RequestEntity re = new InputStreamRequestEntity(is, b.length, "application/soap+xml; charset=utf-8");
            postMethod.setRequestEntity(re);
            postMethod.setDoAuthentication(true);
            AuthScope authscope = new AuthScope(uri.getHost(), AuthScope.ANY_PORT);
            HttpClient httpClient = new HttpClient();
            httpClient.getState().setCredentials(authscope, new NTCredentials(userName, pwd, Inet4Address.getLocalHost().getHostName(), "domain"));

            httpClient.executeMethod(new GetMethod(SPConstant.WEBDOMIN));
            httpClient.executeMethod(postMethod);
            soapResponseData = postMethod.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soapResponseData;
    }



}
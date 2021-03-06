// =====================================================================
//  This file is part of the Microsoft Dynamics CRM SDK code samples.
//
//  Copyright (C) Microsoft Corporation.  All rights reserved.
//
//  This source code is intended only as a supplement to Microsoft
//  Development Tools and/or on-line documentation.  See these other
//  materials for detailed information regarding Microsoft code samples.
//
//  THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
//  KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
//  IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
//  PARTICULAR PURPOSE.
// =====================================================================
package telesul.crm.controller;

import telesul.crm.DiscoveryServiceStub;
import java.rmi.RemoteException;
import java.util.UUID;

import telesul.crm.DiscoveryServiceStub.EndpointType;
import telesul.crm.DiscoveryServiceStub.KeyValuePairOfEndpointTypestringztYlk6OT;
import telesul.crm.DiscoveryServiceStub.OrganizationDetail;
import telesul.crm.DiscoveryServiceStub.RetrieveOrganizationResponse;
import telesul.crm.IDiscoveryService_Execute_DiscoveryServiceFaultFault_FaultMessage;
import telesul.crm.IOrganizationService_Create_OrganizationServiceFaultFault_FaultMessage;
import telesul.crm.IOrganizationService_Delete_OrganizationServiceFaultFault_FaultMessage;
import telesul.crm.IOrganizationService_RetrieveMultiple_OrganizationServiceFaultFault_FaultMessage;
import telesul.crm.IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage;
import telesul.crm.IOrganizationService_Update_OrganizationServiceFaultFault_FaultMessage;
import telesul.crm.OrganizationServiceStub;
import telesul.crm.OrganizationServiceStub.ConditionExpression;
import telesul.crm.OrganizationServiceStub.ConditionOperator;
import telesul.crm.OrganizationServiceStub.Entity;
import telesul.crm.OrganizationServiceStub.EntityCollection;
import telesul.crm.OrganizationServiceStub.FilterExpression;
import telesul.crm.OrganizationServiceStub.QueryExpression;
import telesul.crm.helper.DeviceRegistrationFailedException;
import telesul.crm.helper.OnlineAuthenticationPolicy;
import telesul.crm.helper.RequestDateTimeData;
import telesul.crm.helper.SecurityData;
import telesul.crm.helper.WsdlTokenManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.wsdl.WSDLException;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

@Controller
@RequestMapping(value = "/crm")
public class CRMController {

    static Logger logger = Logger.getLogger(CRMController.class.getName());
    @Autowired
    ServletContext servletContext = null;
    static String appPath;
    /**
     * Microsoft account (e.g. youremail@live.com) or Microsoft Office 365 (Org
     * ID e.g. youremail@yourorg.onmicrosoft.com) User Name.
     */
    private static final String UserName = "crm_atendimento@zapcorp.com.br";
    /**
     * Microsoft account or Microsoft Office 365 (Org ID) Password.
     */
    private static final String UserPassword = "Dozu2509";
    /**
     * Unique Name of the organization
     */
    private static final String OrganizationUniqueName = "orge7a60768";
    /**
     * URL for the Discovery Service For North America Microsoft account,
     * discovery service url is
     * https://dev.crm.dynamics.com/XRMServices/2011/Discovery.svc Microsoft
     * office 365, discovery service url is
     * https://disco.crm.dynamics.com/XRMServices/2011/Discovery.svc To use
     * appropriate discovery service url for other environments refer
     * http://technet.microsoft.com/en-us/library/gg309401.aspx
     */
    private static final String DiscoveryServiceURL = "https://disco.crm.dynamics.com/XRMServices/2011/Discovery.svc";
    /**
     * Suffix for the Flat WSDL
     */
    private static final String FlatWSDLSuffix = "?wsdl";

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String Index(Map<String, Object> model, HttpServletRequest request) {

        try {
            System.setProperty("appPath", servletContext.getRealPath("/"));

            Map<String, Object> testIntegration = this.IndexHelper();
            logger.log(Priority.INFO, "Came here");
            //String workingDirectory = System.getProperty("user.dir");
            //logging.publish(IndexController.class.getName(), Level.OFF, "Working Directory: "+workingDirectory);

        } catch (IOrganizationService_Delete_OrganizationServiceFaultFault_FaultMessage ex) {
            logger.log(Priority.ERROR, "IOrganizationService_Delete_OrganizationServiceFaultFault_FaultMessage", ex);
        } catch (IDiscoveryService_Execute_DiscoveryServiceFaultFault_FaultMessage ex) {
            logger.log(Priority.ERROR, "IDiscoveryService_Execute_DiscoveryServiceFaultFault_FaultMessage", ex);
        } catch (XMLStreamException ex) {
            logger.log(Priority.ERROR, "XMLStreamException", ex);
        } catch (IOrganizationService_Create_OrganizationServiceFaultFault_FaultMessage ex) {
            logger.log(Priority.ERROR, "IOrganizationService_Create_OrganizationServiceFaultFault_FaultMessage", ex);
        } catch (IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage ex) {
            logger.log(Priority.ERROR, "IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage", ex);
        } catch (IOrganizationService_Update_OrganizationServiceFaultFault_FaultMessage ex) {
            logger.log(Priority.ERROR, "IOrganizationService_Update_OrganizationServiceFaultFault_FaultMessage", ex);
        } catch (RemoteException ex) {
            logger.log(Priority.ERROR, "RemoteException", ex);
        } catch (IOrganizationService_RetrieveMultiple_OrganizationServiceFaultFault_FaultMessage ex) {
            logger.log(Priority.ERROR, "IOrganizationService_RetrieveMultiple_OrganizationServiceFaultFault_FaultMessage", ex);
        }
        return "index";
    }

    /**
     * Standard Main() method used by most SDK samples.
     *
     * @param args
     * @throws
     * IOrganizationService_Delete_OrganizationServiceFaultFault_FaultMessage
     * @throws IDiscoveryService_Execute_DiscoveryServiceFaultFault_FaultMessage
     * @throws XMLStreamException
     * @throws
     * IOrganizationService_Create_OrganizationServiceFaultFault_FaultMessage
     * @throws
     * IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage
     * @throws
     * IOrganizationService_Update_OrganizationServiceFaultFault_FaultMessage
     */
    public Map<String, Object> IndexHelper() throws IOrganizationService_Delete_OrganizationServiceFaultFault_FaultMessage, IDiscoveryService_Execute_DiscoveryServiceFaultFault_FaultMessage, XMLStreamException, IOrganizationService_Create_OrganizationServiceFaultFault_FaultMessage, IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage, IOrganizationService_Update_OrganizationServiceFaultFault_FaultMessage, RemoteException, IOrganizationService_RetrieveMultiple_OrganizationServiceFaultFault_FaultMessage {
        try {

            /*           // Retrieve the authentication policy for the discovery service.
            OnlineAuthenticationPolicy discoveryPolicy
                    = new OnlineAuthenticationPolicy(DiscoveryServiceURL + FlatWSDLSuffix);
            WsdlTokenManager discoeryTokenManager = new WsdlTokenManager();
            // Authenticate the user using the discovery authentication policy.
            SecurityData discoverySecurityData = discoeryTokenManager.authenticate(DiscoveryServiceURL,
                    UserName,
                    UserPassword,
                    discoveryPolicy.getAppliesTo(),
                    discoveryPolicy.getPolicy(),
                    discoveryPolicy.getIssuerUri());

            // Retrieve discovery stub using organization URL with the security data.
            DiscoveryServiceStub discoveryServiceStub = createDiscoveryServiceStub(DiscoveryServiceURL,
                    discoverySecurityData);

            // Retrieve organization service url using discovery stub.
            String orgUrl = discoverOrganizationUrl(discoveryServiceStub, OrganizationUniqueName);*/
            String orgUrl = "https://zapcrmdesenvolvimento.api.crm2.dynamics.com/XRMServices/2011/Organization.svc";
            // The discovery service stub cannot be reused against the organization service 
            // as the Issuer and AppliesTo may differ between the discovery and organization services.
            // Retrieve the authentication policy for the organization service.
            OnlineAuthenticationPolicy organizationPolicy
                    = new OnlineAuthenticationPolicy(orgUrl + FlatWSDLSuffix);
            WsdlTokenManager orgTokenManager = new WsdlTokenManager();
            // Authenticate the user using the organization authentication policy.
            SecurityData securityData = orgTokenManager.authenticate(orgUrl,
                    UserName,
                    UserPassword,
                    organizationPolicy.getAppliesTo(),
                    organizationPolicy.getPolicy(),
                    organizationPolicy.getIssuerUri());

            // Retrieve organization stub using organization URL with the security data.
            OrganizationServiceStub serviceStub = createOrganizationServiceStub(
                    orgUrl,
                    securityData);

            // Create an sample account record.
//            OrganizationServiceStub.Guid newAccountGUID = createAccount(serviceStub);
//            logger.log(Priority.INFO, "Account created ::"+newAccountGUID);
//             Retrieve the sample account record.
            OrganizationServiceStub.Guid newAccountGUID = new OrganizationServiceStub.Guid();
//            readAccount(serviceStub, newAccountGUID);
//            logger.log(Priority.INFO, "Account retrived ::" + newAccountGUID);
            readMultipleAccount(serviceStub);
            // Update the sample account record.
//            updateAccount(serviceStub, newAccountGUID);
            // Retrieve updated sample account record.
//            readAccount(serviceStub, newAccountGUID);
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("\nDo you want this sample account record deleted? (y/n) [y]: ");
//            String answer = scanner.nextLine();
//
//            if (!answer.startsWith("n") || !answer.startsWith("N")) {
//                // Delete the sample account record.
//                deleteAccount(serviceStub, newAccountGUID);
//            }
        } catch (IllegalStateException e) {
            logger.error(e.getMessage(),e);
        } catch (SAXException e) {
            logger.error(e.getMessage(),e);
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage(),e);
        } catch (DeviceRegistrationFailedException e) {
            logger.error(e.getMessage(),e);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } catch (XPathExpressionException e) {
            logger.error(e.getMessage(),e);
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(),e);
        } catch (WSDLException e) {
            logger.error(e.getMessage(),e);
        }
        return new HashMap<String, Object>();
    }

    private static OrganizationServiceStub createOrganizationServiceStub(String organizationServiceURL, SecurityData securityData)
            throws RemoteException, XMLStreamException {
        try {
            OrganizationServiceStub stub = new OrganizationServiceStub(getConfigurationContext(), organizationServiceURL);
            setServiceClientOptions(stub._getServiceClient(), securityData);
            return stub;
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static DiscoveryServiceStub createDiscoveryServiceStub(String discoveryServiceURL, SecurityData securityData)
            throws RemoteException, XMLStreamException {
        try {
            DiscoveryServiceStub stub = new DiscoveryServiceStub(getConfigurationContext(), discoveryServiceURL);
            setServiceClientOptions(stub._getServiceClient(), securityData);
            return stub;
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static void setServiceClientOptions(ServiceClient sc, SecurityData securityData)
            throws AxisFault, XMLStreamException {
        Options options = sc.getOptions();

        options.setMessageId("urn:uuid:" + UUID.randomUUID().toString());

        EndpointReference endPoint = new EndpointReference("http://www.w3.org/2005/08/addressing/anonymous");
        options.setReplyTo(endPoint);

        sc.setOptions(options);
        sc.addHeader(createCRMSecurityHeaderBlock(securityData));
        try {
            sc.engageModule("addressing");
        } catch (AxisFault e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static ConfigurationContext getConfigurationContext() throws AxisFault {
        String workingDirectory = System.getProperty("appPath");

        String fileSeperator = System.getProperty("file.separator");
        String pathToAxis2File = workingDirectory + fileSeperator + "WEB-INF" + fileSeperator + "axis2.xml";

        logger.debug("Working directory: " + workingDirectory);
        logger.debug("Path to Axis2.xml file: " + pathToAxis2File);

        ConfigurationContext ctx;
        try {
            ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(workingDirectory, pathToAxis2File);
        } catch (AxisFault e) {
            logger.error(e.getMessage());
            throw e;
        }
        return ctx;
    }

    private static SOAPHeaderBlock createCRMSecurityHeaderBlock(SecurityData securityData)
            throws XMLStreamException {
        RequestDateTimeData dateTimeData = WsdlTokenManager.getRequestDateTime();

        String currentDateTime = dateTimeData.getCreatedDateTime();
        String expireDateTime = dateTimeData.getExpiresDateTime();

        String securityHeaderTemplate = "<EncryptedData "
                + "    xmlns=\"http://www.w3.org/2001/04/xmlenc#\""
                + "     Id=\"Assertion0\" "
                + "    Type=\"http://www.w3.org/2001/04/xmlenc#Element\">"
                + "    <EncryptionMethod "
                + "        Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\"/>"
                + "    <ds:KeyInfo "
                + "        xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"
                + "        <EncryptedKey>"
                + "            <EncryptionMethod "
                + "                Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\"/>"
                + "            <ds:KeyInfo Id=\"keyinfo\">"
                + "                <wsse:SecurityTokenReference "
                + "                    xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"
                + "                    <wsse:KeyIdentifier "
                + "                        EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" "
                + "                        ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier\">%s</wsse:KeyIdentifier>"
                + "                </wsse:SecurityTokenReference>"
                + "            </ds:KeyInfo>"
                + "            <CipherData>"
                + "                <CipherValue>%s</CipherValue>"
                + "            </CipherData>"
                + "        </EncryptedKey>"
                + "    </ds:KeyInfo>"
                + "    <CipherData>"
                + "        <CipherValue>%s</CipherValue>"
                + "    </CipherData>"
                + "</EncryptedData>";

        String securityHeader = String.format(
                securityHeaderTemplate,
                securityData.getKeyIdentifier(),
                securityData.getSecurityToken0(),
                securityData.getSecurityToken1()
        );

        try {

            OMFactory factory = OMAbstractFactory.getOMFactory();
            OMNamespace securityNS = factory.createOMNamespace("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "o");
            OMNamespace utitlityNS = factory.createOMNamespace("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "u");

            OMElement timeStamp = factory.createOMElement("Timestamp", utitlityNS);
            timeStamp.addAttribute("Id", "_0", utitlityNS);

            OMElement created = factory.createOMElement("Created", utitlityNS);
            OMText createdTime = factory.createOMText(currentDateTime + "Z");
            created.addChild(createdTime);

            OMElement expires = factory.createOMElement("Expires", utitlityNS);
            OMText expiresTime = factory.createOMText(expireDateTime + "Z");
            expires.addChild(expiresTime);

            timeStamp.addChild(created);
            timeStamp.addChild(expires);

            SOAPHeaderBlock wsseHeader = OMAbstractFactory.getSOAP12Factory().createSOAPHeaderBlock("Security", securityNS);
            wsseHeader.setMustUnderstand(true);

            wsseHeader.addChild(timeStamp);
            wsseHeader.addChild(AXIOMUtil.stringToOM(factory, securityHeader));

            return wsseHeader;
        } catch (XMLStreamException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static OrganizationServiceStub.Guid createAccount(OrganizationServiceStub serviceStub)
            throws RemoteException, IOrganizationService_Create_OrganizationServiceFaultFault_FaultMessage {
        try {
            OrganizationServiceStub.Create entry = new OrganizationServiceStub.Create();
            OrganizationServiceStub.Entity newEntryInfo = new OrganizationServiceStub.Entity();

            OrganizationServiceStub.AttributeCollection collection = new OrganizationServiceStub.AttributeCollection();
            OrganizationServiceStub.KeyValuePairOfstringanyType values = new OrganizationServiceStub.KeyValuePairOfstringanyType();
            values.setKey("name");
            values.setValue("Fabrikam");

            collection.addKeyValuePairOfstringanyType(values);
            newEntryInfo.setAttributes(collection);
            newEntryInfo.setLogicalName("account");

            entry.setEntity(newEntryInfo);

            OrganizationServiceStub.CreateResponse createResponse = serviceStub.create(entry);
            OrganizationServiceStub.Guid createResultGuid = createResponse.getCreateResult();

            System.out.println("New Account GUID: " + createResultGuid.getGuid());

            return createResultGuid;
        } catch (IOrganizationService_Create_OrganizationServiceFaultFault_FaultMessage e) {
            logger.error(e.getMessage());
            throw e;
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static void readAccount(OrganizationServiceStub serviceStub, OrganizationServiceStub.Guid guid)
            throws IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage, RemoteException {
        try {
            OrganizationServiceStub.Retrieve read = new OrganizationServiceStub.Retrieve();
            OrganizationServiceStub.ColumnSet colSet = new OrganizationServiceStub.ColumnSet();
            OrganizationServiceStub.ArrayOfstring cols = new OrganizationServiceStub.ArrayOfstring();

            cols.setString(new String[]{"zap_cpf", "zap_codigocliente"});
            colSet.setColumns(cols);
            guid.setGuid("A4EB22B2-5B1E-E411-B4BE-6C3BE5A8862C");
            read.setId(guid);
            read.setEntityName("account");
            read.setColumnSet(colSet);

            logger.log(Priority.INFO, "Reading account: " + guid.getGuid());

            OrganizationServiceStub.RetrieveResponse response = serviceStub.retrieve(read);

            Entity result = response.getRetrieveResult();
            OrganizationServiceStub.AttributeCollection attributes = result.getAttributes();

            OrganizationServiceStub.KeyValuePairOfstringanyType[] keyValuePairs = attributes.getKeyValuePairOfstringanyType();

            for (int i = 0; i < keyValuePairs.length; i++) {
                logger.log(Priority.INFO, keyValuePairs[i].getKey() + ": ");
                logger.log(Priority.INFO, keyValuePairs[i].getValue());
            }
        } catch (IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage e) {
            logger.error(e.getMessage());
            throw e;
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static void readMultipleAccount(OrganizationServiceStub serviceStub)
            throws IOrganizationService_Retrieve_OrganizationServiceFaultFault_FaultMessage, RemoteException, IOrganizationService_RetrieveMultiple_OrganizationServiceFaultFault_FaultMessage {
        try {
            //  Query using ConditionExpression  
            ConditionExpression condition1 = new ConditionExpression();
            condition1.setAttributeName("zap_cpf");
            condition1.setOperator(ConditionOperator.Equal);
            OrganizationServiceStub.ArrayOfanyType vals = new OrganizationServiceStub.ArrayOfanyType();
            vals.addAnyType("069.867.578-91");
            condition1.setValues(vals);
            OrganizationServiceStub.ArrayOfConditionExpression conds = new OrganizationServiceStub.ArrayOfConditionExpression();
            FilterExpression filter1 = new FilterExpression();
            conds.addConditionExpression(condition1);
            filter1.setConditions(conds);

            QueryExpression qe = new QueryExpression();
            qe.setEntityName("account");
            OrganizationServiceStub.ColumnSet colSet = new OrganizationServiceStub.ColumnSet();

            OrganizationServiceStub.ArrayOfstring cols = new OrganizationServiceStub.ArrayOfstring();
            cols.setString(new String[]{"zap_cpf", "zap_codigocliente","zap_qtdofertascontratadas","telephone1"});
            colSet.setColumns(cols);

            qe.setColumnSet(colSet);
            OrganizationServiceStub.ArrayOfFilterExpression arrayOfFilterExpression = new OrganizationServiceStub.ArrayOfFilterExpression();
            arrayOfFilterExpression.addFilterExpression(filter1);
            FilterExpression expression = new FilterExpression();
            expression.setFilters(arrayOfFilterExpression);
            qe.setCriteria(expression);

            OrganizationServiceStub.RetrieveMultiple readMultiple = new OrganizationServiceStub.RetrieveMultiple();
            readMultiple.setQuery(qe);

            OrganizationServiceStub.RetrieveMultipleResponse response = serviceStub.retrieveMultiple(readMultiple);

//            OrganizationServiceStub.Retrieve read = new OrganizationServiceStub.Retrieve();
//
//            guid.setGuid("A4EB22B2-5B1E-E411-B4BE-6C3BE5A8862C");
//            read.setId(guid);
//            read.setEntityName("account");
//            read.setColumnSet(colSet);
//            logger.log(Priority.INFO, "Reading account: " + guid.getGuid());
//            OrganizationServiceStub.RetrieveResponse response = serviceStub.retrieve(read);
            EntityCollection resultCollection = response.getRetrieveMultipleResult();
            OrganizationServiceStub.ArrayOfEntity entities = resultCollection.getEntities();
            Entity[] ents = entities.getEntity();
            for (Entity result : ents) {
                OrganizationServiceStub.AttributeCollection attributes = result.getAttributes();
                OrganizationServiceStub.KeyValuePairOfstringanyType[] keyValuePairs = attributes.getKeyValuePairOfstringanyType();
                for (int i = 0; i < keyValuePairs.length; i++) {
                    logger.log(Priority.INFO, keyValuePairs[i].getKey() + ": ");
                    logger.log(Priority.INFO, keyValuePairs[i].getValue());
                }
            }

        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (IOrganizationService_RetrieveMultiple_OrganizationServiceFaultFault_FaultMessage e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static void updateAccount(OrganizationServiceStub serviceStub, OrganizationServiceStub.Guid guid)
            throws IOrganizationService_Update_OrganizationServiceFaultFault_FaultMessage, RemoteException {
        try {
            OrganizationServiceStub.Update updateEntry = new OrganizationServiceStub.Update();
            Entity updateInfo = new Entity();

            OrganizationServiceStub.AttributeCollection updateCollection = new OrganizationServiceStub.AttributeCollection();

            OrganizationServiceStub.KeyValuePairOfstringanyType telephone = new OrganizationServiceStub.KeyValuePairOfstringanyType();
            telephone.setKey("telephone1");
            telephone.setValue("425-555-1212");

            OrganizationServiceStub.KeyValuePairOfstringanyType city = new OrganizationServiceStub.KeyValuePairOfstringanyType();
            city.setKey("address1_city");
            city.setValue("Bellevue");

            OrganizationServiceStub.KeyValuePairOfstringanyType email = new OrganizationServiceStub.KeyValuePairOfstringanyType();
            email.setKey("emailaddress1");
            email.setValue("someone@example.com");

            updateCollection.addKeyValuePairOfstringanyType(telephone);
            updateCollection.addKeyValuePairOfstringanyType(city);
            updateCollection.addKeyValuePairOfstringanyType(email);

            updateInfo.setAttributes(updateCollection);
            updateInfo.setId(guid);
            updateInfo.setLogicalName("account");

            updateEntry.setEntity(updateInfo);

            System.out.println("Updating account: " + guid.getGuid());

            OrganizationServiceStub.UpdateResponse updateResponse = serviceStub.update(updateEntry);
        } catch (IOrganizationService_Update_OrganizationServiceFaultFault_FaultMessage e) {
            logger.error(e.getMessage());
            throw e;
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static void deleteAccount(OrganizationServiceStub serviceStub, OrganizationServiceStub.Guid guid)
            throws IOrganizationService_Delete_OrganizationServiceFaultFault_FaultMessage, RemoteException {
        try {
            OrganizationServiceStub.Delete deleteEntry = new OrganizationServiceStub.Delete();

            deleteEntry.setId(guid);
            deleteEntry.setEntityName("account");

            serviceStub.delete(deleteEntry);
            System.out.println("Deleted account: " + guid.getGuid());
        } catch (IOrganizationService_Delete_OrganizationServiceFaultFault_FaultMessage e) {
            logger.error(e.getMessage());
            throw e;
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static String discoverOrganizationUrl(DiscoveryServiceStub serviceStub, String organizationUniqueName)
            throws RemoteException, IDiscoveryService_Execute_DiscoveryServiceFaultFault_FaultMessage {
        try {
            DiscoveryServiceStub.RetrieveOrganizationRequest request = new DiscoveryServiceStub.RetrieveOrganizationRequest();

            request.setUniqueName(organizationUniqueName);

            DiscoveryServiceStub.Execute exe = new DiscoveryServiceStub.Execute();
            exe.setRequest(request);

            DiscoveryServiceStub.ExecuteResponse response = serviceStub.execute(exe);

            DiscoveryServiceStub.RetrieveOrganizationResponse result = (RetrieveOrganizationResponse) response.getExecuteResult();

            OrganizationDetail orgDetail = result.getDetail();

            KeyValuePairOfEndpointTypestringztYlk6OT[] keyValuePairs = orgDetail.getEndpoints().getKeyValuePairOfEndpointTypestringztYlk6OT();

            for (int i = 0; i < keyValuePairs.length; i++) {
                if (keyValuePairs[i].getKey() == EndpointType.OrganizationService) {
                    return keyValuePairs[i].getValue();
                }
            }
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (IDiscoveryService_Execute_DiscoveryServiceFaultFault_FaultMessage e) {
            logger.error(e.getMessage());
            throw e;
        }
        return null;
    }

}

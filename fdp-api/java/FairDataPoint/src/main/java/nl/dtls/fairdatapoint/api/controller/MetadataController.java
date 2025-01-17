/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.dtls.fairdatapoint.api.controller;



import nl.dtls.fairdatapoint.service.FairMetadataServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.dtls.fairdatapoint.api.controller.utils.HttpHeadersUtils;
import nl.dtls.fairdatapoint.api.controller.utils.LoggerUtils;
import nl.dtls.fairdatapoint.service.FairMetaDataService;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openrdf.rio.RDFFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@Api(description = "FDP metadata")
@RequestMapping(value = "/")
public class MetadataController {  
    private final static Logger LOGGER 
            = LogManager.getLogger(MetadataController.class);
    @Autowired
    private FairMetaDataService fairMetaDataService;

    /**
     * To hander GET fdp metadata request.
     * (Note:) The first value in the produces annotation is used as a fallback
     * value, for the request with the accept header value (* / *),
     * manually setting the contentType of the response is not working.
     * 
     * @param request   Http request
     * @param response  Http response   
     * @return  On success return FDP metadata
     */
    @ApiOperation(value = "FDP metadata")
    @RequestMapping(method = RequestMethod.GET,
            produces = { "text/turtle", 
        "application/ld+json", "application/rdf+xml", "text/n3"} 
    )
    public String getFDAMetaData(final HttpServletRequest request,
                    HttpServletResponse response) { 
        String responseBody;
        LOGGER.info("Request to get FDP metadata");
        LOGGER.info("GET : " + request.getRequestURL()); 
        String contentType = request.getHeader(HttpHeaders.ACCEPT);
        RDFFormat requesetedContentType = HttpHeadersUtils.
                getRequestedAcceptHeader(contentType);        
        try { 
            responseBody = fairMetaDataService.retrieveFDPMetaData(
                    requesetedContentType);
            HttpHeadersUtils.set200ResponseHeaders(responseBody, response, 
                    requesetedContentType);        
            } catch (FairMetadataServiceException ex) {            
                responseBody = HttpHeadersUtils.set500ResponseHeaders(
                        response, ex);
            }
        LoggerUtils.logRequest(LOGGER, request, response);
        return responseBody;
    }
        
    @ApiOperation(value = "Catalog metadata")
    @RequestMapping(value = "/{catalogID:[^.]+}", method = RequestMethod.GET,
            produces = { "text/turtle", 
        "application/ld+json", "application/rdf+xml", "text/n3"}
    )
    public String getCatalogMetaData(
            @PathVariable final String catalogID, HttpServletRequest request,
                    HttpServletResponse response) {
        LOGGER.info("Request to get CATALOG metadata {}", catalogID);
        LOGGER.info("GET : " + request.getRequestURL());
        String responseBody;
        String contentType = request.getHeader(HttpHeaders.ACCEPT);
        RDFFormat requesetedContentType = HttpHeadersUtils.getRequestedAcceptHeader(contentType);   
        try {                
            responseBody = fairMetaDataService.                        
                    retrieveCatalogMetaData(catalogID, requesetedContentType);
                HttpHeadersUtils.set200ResponseHeaders(responseBody, response, 
                        requesetedContentType);
            } catch (FairMetadataServiceException ex) {
                responseBody = HttpHeadersUtils.set500ResponseHeaders(
                        response, ex);
            }
        LoggerUtils.logRequest(LOGGER, request, response);
        return responseBody;
    }
    
    @ApiOperation(value = "Dataset metadata")
    @RequestMapping(value = "/{catalogID}/{datasetID}", 
            method = RequestMethod.GET,
            produces = { "text/turtle", 
        "application/ld+json", "application/rdf+xml", "text/n3"}
    )
    public String getDatasetMetaData(@PathVariable final String catalogID,
            @PathVariable final String datasetID, HttpServletRequest request,
                    HttpServletResponse response) {  
        LOGGER.info("Request to get DATASET metadata {}", catalogID);
        LOGGER.info("GET : " + request.getRequestURL());
        String responseBody;
        String contentType = request.getHeader(HttpHeaders.ACCEPT);
        RDFFormat requesetedContentType = HttpHeadersUtils.getRequestedAcceptHeader(contentType);    
        try {   
            responseBody = fairMetaDataService.retrieveDatasetMetaData(
                    catalogID, datasetID, requesetedContentType);                
            HttpHeadersUtils.set200ResponseHeaders(responseBody, response, 
                    requesetedContentType);
            } catch (FairMetadataServiceException ex) {                
                responseBody = HttpHeadersUtils.set500ResponseHeaders(
                        response, ex);
            }
        LoggerUtils.logRequest(LOGGER, request, response);
        return responseBody;
    }
    
}

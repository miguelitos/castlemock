/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.web.mock.soap.web.view.controller.mockresponse;


import com.castlemock.core.mock.soap.model.project.domain.SoapMockResponseStatus;
import com.castlemock.core.mock.soap.model.project.domain.SoapMockResponse;
import com.castlemock.core.mock.soap.model.project.domain.SoapOperation;
import com.castlemock.core.mock.soap.service.project.input.CreateSoapMockResponseInput;
import com.castlemock.core.mock.soap.service.project.input.ReadSoapOperationInput;
import com.castlemock.core.mock.soap.service.project.output.ReadSoapOperationOutput;
import com.castlemock.web.mock.soap.web.view.controller.AbstractSoapViewController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * The CreateMockResponseController controller is responsible for providing functionality to create
 * mocked responses for a specific operation
 * @author Karl Dahlgren
 * @since 1.0
 */
@Controller
@Scope("request")
@RequestMapping("/web/soap/project")
@ConditionalOnExpression("${server.mode.demo} == false")
public class CreateSoapMockResponseController extends AbstractSoapViewController {

    private static final String PAGE = "mock/soap/mockresponse/createSoapMockResponse";
    private static final Integer DEFAULT_HTTP_STATUS_CODE = 200;

    /**
     * The method returns a view used for creating a new mocked response
     * @param soapProjectId The id of the project which the operation belongs to
     * @param soapPortId The id of the port which the operation belongs to
     * @param soapOperationId The id of the operation that should get a new mocked response
     * @return Returns a view that displays all the required attributes needed for creating a mocked response
     */
    @PreAuthorize("hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    @RequestMapping(value = "/{soapProjectId}/port/{soapPortId}/operation/{soapOperationId}/create/response", method = RequestMethod.GET)
    public ModelAndView dispayCreatePage(@PathVariable final String soapProjectId, @PathVariable final String soapPortId, @PathVariable final String soapOperationId) {
        final ReadSoapOperationOutput output = serviceProcessor.process(ReadSoapOperationInput.builder()
                .projectId(soapProjectId)
                .portId(soapPortId)
                .operationId(soapOperationId)
                .build());
        final SoapOperation soapOperation = output.getOperation();
        final SoapMockResponse mockResponse = new SoapMockResponse();
        mockResponse.setBody(soapOperation.getDefaultBody());
        mockResponse.setHttpStatusCode(DEFAULT_HTTP_STATUS_CODE);
        final ModelAndView model = createPartialModelAndView(PAGE);
        model.addObject(SOAP_OPERATION, soapOperation);
        model.addObject(SOAP_PROJECT_ID, soapProjectId);
        model.addObject(SOAP_PORT_ID, soapPortId);
        model.addObject(COMMAND, mockResponse);
        model.addObject(SOAP_MOCK_RESPONSE_STATUSES, SoapMockResponseStatus.values());
        return model;
    }

    /**
     * The method provides functionality to create a new mocked response
     * @param soapProjectId The id of the project which the operation belongs to
     * @param soapPortId The id of the port which the operation belongs to
     * @param soapOperationId The id of the operation that should get a new mocked response
     * @param soapMockResponse The  instance of the mocked response that will be created
     * @return A view and model that redirects the user to the operation main page
     */
    @PreAuthorize("hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    @RequestMapping(value = "/{soapProjectId}/port/{soapPortId}/operation/{soapOperationId}/create/response", method = RequestMethod.POST)
    public ModelAndView createResponse(@PathVariable final String soapProjectId,
                                       @PathVariable final String soapPortId,
                                       @PathVariable final String soapOperationId,
                                       @ModelAttribute(name = "soapMockResponse") final SoapMockResponse soapMockResponse) {
        serviceProcessor.process(CreateSoapMockResponseInput.builder()
                .projectId(soapProjectId)
                .portId(soapPortId)
                .operationId(soapOperationId)
                .mockResponse(soapMockResponse)
                .build());
        return redirect("/soap/project/" + soapProjectId + "/port/" + soapPortId + "/operation/" + soapOperationId);
    }

}
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

package com.castlemock.web.mock.rest.service.project;

import com.castlemock.core.basis.model.Service;
import com.castlemock.core.basis.model.ServiceResult;
import com.castlemock.core.basis.model.ServiceTask;
import com.castlemock.core.mock.rest.model.project.domain.RestApplication;
import com.castlemock.core.mock.rest.model.project.domain.RestMethod;
import com.castlemock.core.mock.rest.model.project.domain.RestResource;
import com.castlemock.core.mock.rest.model.project.service.message.input.UpdateRestApplicationsForwardedEndpointInput;
import com.castlemock.core.mock.rest.model.project.service.message.output.UpdateRestApplicationsForwardedEndpointOutput;

import java.util.List;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@org.springframework.stereotype.Service
public class UpdateRestApplicationsForwardedEndpointService extends AbstractRestProjectService implements Service<UpdateRestApplicationsForwardedEndpointInput, UpdateRestApplicationsForwardedEndpointOutput> {

    /**
     * The process message is responsible for processing an incoming serviceTask and generate
     * a response based on the incoming serviceTask input
     * @param serviceTask The serviceTask that will be processed by the service
     * @return A result based on the processed incoming serviceTask
     * @see ServiceTask
     * @see ServiceResult
     */
    @Override
    public ServiceResult<UpdateRestApplicationsForwardedEndpointOutput> process(final ServiceTask<UpdateRestApplicationsForwardedEndpointInput> serviceTask) {
        final UpdateRestApplicationsForwardedEndpointInput input = serviceTask.getInput();
        for(RestApplication restApplication : input.getRestApplications()){
            List<RestResource> resources = this.resourceRepository.findWithApplicationId(restApplication.getId());
            for(RestResource restResource : resources){
                List<RestMethod> methods = this.methodRepository.findWithResourceId(restResource.getId());
                for(RestMethod restMethod : methods){
                    restMethod.setForwardedEndpoint(input.getForwardedEndpoint());
                    this.methodRepository.update(restMethod.getId(), restMethod);
                }
            }
        }
        return createServiceResult(new UpdateRestApplicationsForwardedEndpointOutput());
    }
}